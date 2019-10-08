package com.alphadevs.com.web.rest;

import com.alphadevs.com.StockMngApp;
import com.alphadevs.com.domain.CashBook;
import com.alphadevs.com.domain.Location;
import com.alphadevs.com.domain.DocumentType;
import com.alphadevs.com.domain.Items;
import com.alphadevs.com.repository.CashBookRepository;
import com.alphadevs.com.service.CashBookService;
import com.alphadevs.com.web.rest.errors.ExceptionTranslator;
import com.alphadevs.com.service.dto.CashBookCriteria;
import com.alphadevs.com.service.CashBookQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static com.alphadevs.com.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link CashBookResource} REST controller.
 */
@SpringBootTest(classes = StockMngApp.class)
public class CashBookResourceIT {

    private static final LocalDate DEFAULT_CASHBOOK_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CASHBOOK_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_CASHBOOK_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_CASHBOOK_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_CASHBOOK_DESCRIPTION = "BBBBBBBBBB";

    private static final Double DEFAULT_CASHBOOK_AMOUNT_CR = 1D;
    private static final Double UPDATED_CASHBOOK_AMOUNT_CR = 2D;
    private static final Double SMALLER_CASHBOOK_AMOUNT_CR = 1D - 1D;

    private static final Double DEFAULT_CASHBOOK_AMOUNT_DR = 1D;
    private static final Double UPDATED_CASHBOOK_AMOUNT_DR = 2D;
    private static final Double SMALLER_CASHBOOK_AMOUNT_DR = 1D - 1D;

    private static final Double DEFAULT_CASHBOOK_BALANCE = 1D;
    private static final Double UPDATED_CASHBOOK_BALANCE = 2D;
    private static final Double SMALLER_CASHBOOK_BALANCE = 1D - 1D;

    @Autowired
    private CashBookRepository cashBookRepository;

    @Autowired
    private CashBookService cashBookService;

    @Autowired
    private CashBookQueryService cashBookQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restCashBookMockMvc;

    private CashBook cashBook;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CashBookResource cashBookResource = new CashBookResource(cashBookService, cashBookQueryService);
        this.restCashBookMockMvc = MockMvcBuilders.standaloneSetup(cashBookResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CashBook createEntity(EntityManager em) {
        CashBook cashBook = new CashBook()
            .cashbookDate(DEFAULT_CASHBOOK_DATE)
            .cashbookDescription(DEFAULT_CASHBOOK_DESCRIPTION)
            .cashbookAmountCR(DEFAULT_CASHBOOK_AMOUNT_CR)
            .cashbookAmountDR(DEFAULT_CASHBOOK_AMOUNT_DR)
            .cashbookBalance(DEFAULT_CASHBOOK_BALANCE);
        return cashBook;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CashBook createUpdatedEntity(EntityManager em) {
        CashBook cashBook = new CashBook()
            .cashbookDate(UPDATED_CASHBOOK_DATE)
            .cashbookDescription(UPDATED_CASHBOOK_DESCRIPTION)
            .cashbookAmountCR(UPDATED_CASHBOOK_AMOUNT_CR)
            .cashbookAmountDR(UPDATED_CASHBOOK_AMOUNT_DR)
            .cashbookBalance(UPDATED_CASHBOOK_BALANCE);
        return cashBook;
    }

    @BeforeEach
    public void initTest() {
        cashBook = createEntity(em);
    }

    @Test
    @Transactional
    public void createCashBook() throws Exception {
        int databaseSizeBeforeCreate = cashBookRepository.findAll().size();

        // Create the CashBook
        restCashBookMockMvc.perform(post("/api/cash-books")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cashBook)))
            .andExpect(status().isCreated());

        // Validate the CashBook in the database
        List<CashBook> cashBookList = cashBookRepository.findAll();
        assertThat(cashBookList).hasSize(databaseSizeBeforeCreate + 1);
        CashBook testCashBook = cashBookList.get(cashBookList.size() - 1);
        assertThat(testCashBook.getCashbookDate()).isEqualTo(DEFAULT_CASHBOOK_DATE);
        assertThat(testCashBook.getCashbookDescription()).isEqualTo(DEFAULT_CASHBOOK_DESCRIPTION);
        assertThat(testCashBook.getCashbookAmountCR()).isEqualTo(DEFAULT_CASHBOOK_AMOUNT_CR);
        assertThat(testCashBook.getCashbookAmountDR()).isEqualTo(DEFAULT_CASHBOOK_AMOUNT_DR);
        assertThat(testCashBook.getCashbookBalance()).isEqualTo(DEFAULT_CASHBOOK_BALANCE);
    }

    @Test
    @Transactional
    public void createCashBookWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = cashBookRepository.findAll().size();

        // Create the CashBook with an existing ID
        cashBook.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCashBookMockMvc.perform(post("/api/cash-books")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cashBook)))
            .andExpect(status().isBadRequest());

        // Validate the CashBook in the database
        List<CashBook> cashBookList = cashBookRepository.findAll();
        assertThat(cashBookList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkCashbookDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = cashBookRepository.findAll().size();
        // set the field null
        cashBook.setCashbookDate(null);

        // Create the CashBook, which fails.

        restCashBookMockMvc.perform(post("/api/cash-books")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cashBook)))
            .andExpect(status().isBadRequest());

        List<CashBook> cashBookList = cashBookRepository.findAll();
        assertThat(cashBookList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCashbookDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = cashBookRepository.findAll().size();
        // set the field null
        cashBook.setCashbookDescription(null);

        // Create the CashBook, which fails.

        restCashBookMockMvc.perform(post("/api/cash-books")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cashBook)))
            .andExpect(status().isBadRequest());

        List<CashBook> cashBookList = cashBookRepository.findAll();
        assertThat(cashBookList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCashbookAmountCRIsRequired() throws Exception {
        int databaseSizeBeforeTest = cashBookRepository.findAll().size();
        // set the field null
        cashBook.setCashbookAmountCR(null);

        // Create the CashBook, which fails.

        restCashBookMockMvc.perform(post("/api/cash-books")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cashBook)))
            .andExpect(status().isBadRequest());

        List<CashBook> cashBookList = cashBookRepository.findAll();
        assertThat(cashBookList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCashbookAmountDRIsRequired() throws Exception {
        int databaseSizeBeforeTest = cashBookRepository.findAll().size();
        // set the field null
        cashBook.setCashbookAmountDR(null);

        // Create the CashBook, which fails.

        restCashBookMockMvc.perform(post("/api/cash-books")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cashBook)))
            .andExpect(status().isBadRequest());

        List<CashBook> cashBookList = cashBookRepository.findAll();
        assertThat(cashBookList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCashbookBalanceIsRequired() throws Exception {
        int databaseSizeBeforeTest = cashBookRepository.findAll().size();
        // set the field null
        cashBook.setCashbookBalance(null);

        // Create the CashBook, which fails.

        restCashBookMockMvc.perform(post("/api/cash-books")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cashBook)))
            .andExpect(status().isBadRequest());

        List<CashBook> cashBookList = cashBookRepository.findAll();
        assertThat(cashBookList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCashBooks() throws Exception {
        // Initialize the database
        cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList
        restCashBookMockMvc.perform(get("/api/cash-books?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cashBook.getId().intValue())))
            .andExpect(jsonPath("$.[*].cashbookDate").value(hasItem(DEFAULT_CASHBOOK_DATE.toString())))
            .andExpect(jsonPath("$.[*].cashbookDescription").value(hasItem(DEFAULT_CASHBOOK_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].cashbookAmountCR").value(hasItem(DEFAULT_CASHBOOK_AMOUNT_CR.doubleValue())))
            .andExpect(jsonPath("$.[*].cashbookAmountDR").value(hasItem(DEFAULT_CASHBOOK_AMOUNT_DR.doubleValue())))
            .andExpect(jsonPath("$.[*].cashbookBalance").value(hasItem(DEFAULT_CASHBOOK_BALANCE.doubleValue())));
    }
    
    @Test
    @Transactional
    public void getCashBook() throws Exception {
        // Initialize the database
        cashBookRepository.saveAndFlush(cashBook);

        // Get the cashBook
        restCashBookMockMvc.perform(get("/api/cash-books/{id}", cashBook.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(cashBook.getId().intValue()))
            .andExpect(jsonPath("$.cashbookDate").value(DEFAULT_CASHBOOK_DATE.toString()))
            .andExpect(jsonPath("$.cashbookDescription").value(DEFAULT_CASHBOOK_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.cashbookAmountCR").value(DEFAULT_CASHBOOK_AMOUNT_CR.doubleValue()))
            .andExpect(jsonPath("$.cashbookAmountDR").value(DEFAULT_CASHBOOK_AMOUNT_DR.doubleValue()))
            .andExpect(jsonPath("$.cashbookBalance").value(DEFAULT_CASHBOOK_BALANCE.doubleValue()));
    }

    @Test
    @Transactional
    public void getAllCashBooksByCashbookDateIsEqualToSomething() throws Exception {
        // Initialize the database
        cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where cashbookDate equals to DEFAULT_CASHBOOK_DATE
        defaultCashBookShouldBeFound("cashbookDate.equals=" + DEFAULT_CASHBOOK_DATE);

        // Get all the cashBookList where cashbookDate equals to UPDATED_CASHBOOK_DATE
        defaultCashBookShouldNotBeFound("cashbookDate.equals=" + UPDATED_CASHBOOK_DATE);
    }

    @Test
    @Transactional
    public void getAllCashBooksByCashbookDateIsInShouldWork() throws Exception {
        // Initialize the database
        cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where cashbookDate in DEFAULT_CASHBOOK_DATE or UPDATED_CASHBOOK_DATE
        defaultCashBookShouldBeFound("cashbookDate.in=" + DEFAULT_CASHBOOK_DATE + "," + UPDATED_CASHBOOK_DATE);

        // Get all the cashBookList where cashbookDate equals to UPDATED_CASHBOOK_DATE
        defaultCashBookShouldNotBeFound("cashbookDate.in=" + UPDATED_CASHBOOK_DATE);
    }

    @Test
    @Transactional
    public void getAllCashBooksByCashbookDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where cashbookDate is not null
        defaultCashBookShouldBeFound("cashbookDate.specified=true");

        // Get all the cashBookList where cashbookDate is null
        defaultCashBookShouldNotBeFound("cashbookDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllCashBooksByCashbookDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where cashbookDate is greater than or equal to DEFAULT_CASHBOOK_DATE
        defaultCashBookShouldBeFound("cashbookDate.greaterThanOrEqual=" + DEFAULT_CASHBOOK_DATE);

        // Get all the cashBookList where cashbookDate is greater than or equal to UPDATED_CASHBOOK_DATE
        defaultCashBookShouldNotBeFound("cashbookDate.greaterThanOrEqual=" + UPDATED_CASHBOOK_DATE);
    }

    @Test
    @Transactional
    public void getAllCashBooksByCashbookDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where cashbookDate is less than or equal to DEFAULT_CASHBOOK_DATE
        defaultCashBookShouldBeFound("cashbookDate.lessThanOrEqual=" + DEFAULT_CASHBOOK_DATE);

        // Get all the cashBookList where cashbookDate is less than or equal to SMALLER_CASHBOOK_DATE
        defaultCashBookShouldNotBeFound("cashbookDate.lessThanOrEqual=" + SMALLER_CASHBOOK_DATE);
    }

    @Test
    @Transactional
    public void getAllCashBooksByCashbookDateIsLessThanSomething() throws Exception {
        // Initialize the database
        cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where cashbookDate is less than DEFAULT_CASHBOOK_DATE
        defaultCashBookShouldNotBeFound("cashbookDate.lessThan=" + DEFAULT_CASHBOOK_DATE);

        // Get all the cashBookList where cashbookDate is less than UPDATED_CASHBOOK_DATE
        defaultCashBookShouldBeFound("cashbookDate.lessThan=" + UPDATED_CASHBOOK_DATE);
    }

    @Test
    @Transactional
    public void getAllCashBooksByCashbookDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where cashbookDate is greater than DEFAULT_CASHBOOK_DATE
        defaultCashBookShouldNotBeFound("cashbookDate.greaterThan=" + DEFAULT_CASHBOOK_DATE);

        // Get all the cashBookList where cashbookDate is greater than SMALLER_CASHBOOK_DATE
        defaultCashBookShouldBeFound("cashbookDate.greaterThan=" + SMALLER_CASHBOOK_DATE);
    }


    @Test
    @Transactional
    public void getAllCashBooksByCashbookDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where cashbookDescription equals to DEFAULT_CASHBOOK_DESCRIPTION
        defaultCashBookShouldBeFound("cashbookDescription.equals=" + DEFAULT_CASHBOOK_DESCRIPTION);

        // Get all the cashBookList where cashbookDescription equals to UPDATED_CASHBOOK_DESCRIPTION
        defaultCashBookShouldNotBeFound("cashbookDescription.equals=" + UPDATED_CASHBOOK_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllCashBooksByCashbookDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where cashbookDescription in DEFAULT_CASHBOOK_DESCRIPTION or UPDATED_CASHBOOK_DESCRIPTION
        defaultCashBookShouldBeFound("cashbookDescription.in=" + DEFAULT_CASHBOOK_DESCRIPTION + "," + UPDATED_CASHBOOK_DESCRIPTION);

        // Get all the cashBookList where cashbookDescription equals to UPDATED_CASHBOOK_DESCRIPTION
        defaultCashBookShouldNotBeFound("cashbookDescription.in=" + UPDATED_CASHBOOK_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllCashBooksByCashbookDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where cashbookDescription is not null
        defaultCashBookShouldBeFound("cashbookDescription.specified=true");

        // Get all the cashBookList where cashbookDescription is null
        defaultCashBookShouldNotBeFound("cashbookDescription.specified=false");
    }

    @Test
    @Transactional
    public void getAllCashBooksByCashbookAmountCRIsEqualToSomething() throws Exception {
        // Initialize the database
        cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where cashbookAmountCR equals to DEFAULT_CASHBOOK_AMOUNT_CR
        defaultCashBookShouldBeFound("cashbookAmountCR.equals=" + DEFAULT_CASHBOOK_AMOUNT_CR);

        // Get all the cashBookList where cashbookAmountCR equals to UPDATED_CASHBOOK_AMOUNT_CR
        defaultCashBookShouldNotBeFound("cashbookAmountCR.equals=" + UPDATED_CASHBOOK_AMOUNT_CR);
    }

    @Test
    @Transactional
    public void getAllCashBooksByCashbookAmountCRIsInShouldWork() throws Exception {
        // Initialize the database
        cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where cashbookAmountCR in DEFAULT_CASHBOOK_AMOUNT_CR or UPDATED_CASHBOOK_AMOUNT_CR
        defaultCashBookShouldBeFound("cashbookAmountCR.in=" + DEFAULT_CASHBOOK_AMOUNT_CR + "," + UPDATED_CASHBOOK_AMOUNT_CR);

        // Get all the cashBookList where cashbookAmountCR equals to UPDATED_CASHBOOK_AMOUNT_CR
        defaultCashBookShouldNotBeFound("cashbookAmountCR.in=" + UPDATED_CASHBOOK_AMOUNT_CR);
    }

    @Test
    @Transactional
    public void getAllCashBooksByCashbookAmountCRIsNullOrNotNull() throws Exception {
        // Initialize the database
        cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where cashbookAmountCR is not null
        defaultCashBookShouldBeFound("cashbookAmountCR.specified=true");

        // Get all the cashBookList where cashbookAmountCR is null
        defaultCashBookShouldNotBeFound("cashbookAmountCR.specified=false");
    }

    @Test
    @Transactional
    public void getAllCashBooksByCashbookAmountCRIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where cashbookAmountCR is greater than or equal to DEFAULT_CASHBOOK_AMOUNT_CR
        defaultCashBookShouldBeFound("cashbookAmountCR.greaterThanOrEqual=" + DEFAULT_CASHBOOK_AMOUNT_CR);

        // Get all the cashBookList where cashbookAmountCR is greater than or equal to UPDATED_CASHBOOK_AMOUNT_CR
        defaultCashBookShouldNotBeFound("cashbookAmountCR.greaterThanOrEqual=" + UPDATED_CASHBOOK_AMOUNT_CR);
    }

    @Test
    @Transactional
    public void getAllCashBooksByCashbookAmountCRIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where cashbookAmountCR is less than or equal to DEFAULT_CASHBOOK_AMOUNT_CR
        defaultCashBookShouldBeFound("cashbookAmountCR.lessThanOrEqual=" + DEFAULT_CASHBOOK_AMOUNT_CR);

        // Get all the cashBookList where cashbookAmountCR is less than or equal to SMALLER_CASHBOOK_AMOUNT_CR
        defaultCashBookShouldNotBeFound("cashbookAmountCR.lessThanOrEqual=" + SMALLER_CASHBOOK_AMOUNT_CR);
    }

    @Test
    @Transactional
    public void getAllCashBooksByCashbookAmountCRIsLessThanSomething() throws Exception {
        // Initialize the database
        cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where cashbookAmountCR is less than DEFAULT_CASHBOOK_AMOUNT_CR
        defaultCashBookShouldNotBeFound("cashbookAmountCR.lessThan=" + DEFAULT_CASHBOOK_AMOUNT_CR);

        // Get all the cashBookList where cashbookAmountCR is less than UPDATED_CASHBOOK_AMOUNT_CR
        defaultCashBookShouldBeFound("cashbookAmountCR.lessThan=" + UPDATED_CASHBOOK_AMOUNT_CR);
    }

    @Test
    @Transactional
    public void getAllCashBooksByCashbookAmountCRIsGreaterThanSomething() throws Exception {
        // Initialize the database
        cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where cashbookAmountCR is greater than DEFAULT_CASHBOOK_AMOUNT_CR
        defaultCashBookShouldNotBeFound("cashbookAmountCR.greaterThan=" + DEFAULT_CASHBOOK_AMOUNT_CR);

        // Get all the cashBookList where cashbookAmountCR is greater than SMALLER_CASHBOOK_AMOUNT_CR
        defaultCashBookShouldBeFound("cashbookAmountCR.greaterThan=" + SMALLER_CASHBOOK_AMOUNT_CR);
    }


    @Test
    @Transactional
    public void getAllCashBooksByCashbookAmountDRIsEqualToSomething() throws Exception {
        // Initialize the database
        cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where cashbookAmountDR equals to DEFAULT_CASHBOOK_AMOUNT_DR
        defaultCashBookShouldBeFound("cashbookAmountDR.equals=" + DEFAULT_CASHBOOK_AMOUNT_DR);

        // Get all the cashBookList where cashbookAmountDR equals to UPDATED_CASHBOOK_AMOUNT_DR
        defaultCashBookShouldNotBeFound("cashbookAmountDR.equals=" + UPDATED_CASHBOOK_AMOUNT_DR);
    }

    @Test
    @Transactional
    public void getAllCashBooksByCashbookAmountDRIsInShouldWork() throws Exception {
        // Initialize the database
        cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where cashbookAmountDR in DEFAULT_CASHBOOK_AMOUNT_DR or UPDATED_CASHBOOK_AMOUNT_DR
        defaultCashBookShouldBeFound("cashbookAmountDR.in=" + DEFAULT_CASHBOOK_AMOUNT_DR + "," + UPDATED_CASHBOOK_AMOUNT_DR);

        // Get all the cashBookList where cashbookAmountDR equals to UPDATED_CASHBOOK_AMOUNT_DR
        defaultCashBookShouldNotBeFound("cashbookAmountDR.in=" + UPDATED_CASHBOOK_AMOUNT_DR);
    }

    @Test
    @Transactional
    public void getAllCashBooksByCashbookAmountDRIsNullOrNotNull() throws Exception {
        // Initialize the database
        cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where cashbookAmountDR is not null
        defaultCashBookShouldBeFound("cashbookAmountDR.specified=true");

        // Get all the cashBookList where cashbookAmountDR is null
        defaultCashBookShouldNotBeFound("cashbookAmountDR.specified=false");
    }

    @Test
    @Transactional
    public void getAllCashBooksByCashbookAmountDRIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where cashbookAmountDR is greater than or equal to DEFAULT_CASHBOOK_AMOUNT_DR
        defaultCashBookShouldBeFound("cashbookAmountDR.greaterThanOrEqual=" + DEFAULT_CASHBOOK_AMOUNT_DR);

        // Get all the cashBookList where cashbookAmountDR is greater than or equal to UPDATED_CASHBOOK_AMOUNT_DR
        defaultCashBookShouldNotBeFound("cashbookAmountDR.greaterThanOrEqual=" + UPDATED_CASHBOOK_AMOUNT_DR);
    }

    @Test
    @Transactional
    public void getAllCashBooksByCashbookAmountDRIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where cashbookAmountDR is less than or equal to DEFAULT_CASHBOOK_AMOUNT_DR
        defaultCashBookShouldBeFound("cashbookAmountDR.lessThanOrEqual=" + DEFAULT_CASHBOOK_AMOUNT_DR);

        // Get all the cashBookList where cashbookAmountDR is less than or equal to SMALLER_CASHBOOK_AMOUNT_DR
        defaultCashBookShouldNotBeFound("cashbookAmountDR.lessThanOrEqual=" + SMALLER_CASHBOOK_AMOUNT_DR);
    }

    @Test
    @Transactional
    public void getAllCashBooksByCashbookAmountDRIsLessThanSomething() throws Exception {
        // Initialize the database
        cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where cashbookAmountDR is less than DEFAULT_CASHBOOK_AMOUNT_DR
        defaultCashBookShouldNotBeFound("cashbookAmountDR.lessThan=" + DEFAULT_CASHBOOK_AMOUNT_DR);

        // Get all the cashBookList where cashbookAmountDR is less than UPDATED_CASHBOOK_AMOUNT_DR
        defaultCashBookShouldBeFound("cashbookAmountDR.lessThan=" + UPDATED_CASHBOOK_AMOUNT_DR);
    }

    @Test
    @Transactional
    public void getAllCashBooksByCashbookAmountDRIsGreaterThanSomething() throws Exception {
        // Initialize the database
        cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where cashbookAmountDR is greater than DEFAULT_CASHBOOK_AMOUNT_DR
        defaultCashBookShouldNotBeFound("cashbookAmountDR.greaterThan=" + DEFAULT_CASHBOOK_AMOUNT_DR);

        // Get all the cashBookList where cashbookAmountDR is greater than SMALLER_CASHBOOK_AMOUNT_DR
        defaultCashBookShouldBeFound("cashbookAmountDR.greaterThan=" + SMALLER_CASHBOOK_AMOUNT_DR);
    }


    @Test
    @Transactional
    public void getAllCashBooksByCashbookBalanceIsEqualToSomething() throws Exception {
        // Initialize the database
        cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where cashbookBalance equals to DEFAULT_CASHBOOK_BALANCE
        defaultCashBookShouldBeFound("cashbookBalance.equals=" + DEFAULT_CASHBOOK_BALANCE);

        // Get all the cashBookList where cashbookBalance equals to UPDATED_CASHBOOK_BALANCE
        defaultCashBookShouldNotBeFound("cashbookBalance.equals=" + UPDATED_CASHBOOK_BALANCE);
    }

    @Test
    @Transactional
    public void getAllCashBooksByCashbookBalanceIsInShouldWork() throws Exception {
        // Initialize the database
        cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where cashbookBalance in DEFAULT_CASHBOOK_BALANCE or UPDATED_CASHBOOK_BALANCE
        defaultCashBookShouldBeFound("cashbookBalance.in=" + DEFAULT_CASHBOOK_BALANCE + "," + UPDATED_CASHBOOK_BALANCE);

        // Get all the cashBookList where cashbookBalance equals to UPDATED_CASHBOOK_BALANCE
        defaultCashBookShouldNotBeFound("cashbookBalance.in=" + UPDATED_CASHBOOK_BALANCE);
    }

    @Test
    @Transactional
    public void getAllCashBooksByCashbookBalanceIsNullOrNotNull() throws Exception {
        // Initialize the database
        cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where cashbookBalance is not null
        defaultCashBookShouldBeFound("cashbookBalance.specified=true");

        // Get all the cashBookList where cashbookBalance is null
        defaultCashBookShouldNotBeFound("cashbookBalance.specified=false");
    }

    @Test
    @Transactional
    public void getAllCashBooksByCashbookBalanceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where cashbookBalance is greater than or equal to DEFAULT_CASHBOOK_BALANCE
        defaultCashBookShouldBeFound("cashbookBalance.greaterThanOrEqual=" + DEFAULT_CASHBOOK_BALANCE);

        // Get all the cashBookList where cashbookBalance is greater than or equal to UPDATED_CASHBOOK_BALANCE
        defaultCashBookShouldNotBeFound("cashbookBalance.greaterThanOrEqual=" + UPDATED_CASHBOOK_BALANCE);
    }

    @Test
    @Transactional
    public void getAllCashBooksByCashbookBalanceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where cashbookBalance is less than or equal to DEFAULT_CASHBOOK_BALANCE
        defaultCashBookShouldBeFound("cashbookBalance.lessThanOrEqual=" + DEFAULT_CASHBOOK_BALANCE);

        // Get all the cashBookList where cashbookBalance is less than or equal to SMALLER_CASHBOOK_BALANCE
        defaultCashBookShouldNotBeFound("cashbookBalance.lessThanOrEqual=" + SMALLER_CASHBOOK_BALANCE);
    }

    @Test
    @Transactional
    public void getAllCashBooksByCashbookBalanceIsLessThanSomething() throws Exception {
        // Initialize the database
        cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where cashbookBalance is less than DEFAULT_CASHBOOK_BALANCE
        defaultCashBookShouldNotBeFound("cashbookBalance.lessThan=" + DEFAULT_CASHBOOK_BALANCE);

        // Get all the cashBookList where cashbookBalance is less than UPDATED_CASHBOOK_BALANCE
        defaultCashBookShouldBeFound("cashbookBalance.lessThan=" + UPDATED_CASHBOOK_BALANCE);
    }

    @Test
    @Transactional
    public void getAllCashBooksByCashbookBalanceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where cashbookBalance is greater than DEFAULT_CASHBOOK_BALANCE
        defaultCashBookShouldNotBeFound("cashbookBalance.greaterThan=" + DEFAULT_CASHBOOK_BALANCE);

        // Get all the cashBookList where cashbookBalance is greater than SMALLER_CASHBOOK_BALANCE
        defaultCashBookShouldBeFound("cashbookBalance.greaterThan=" + SMALLER_CASHBOOK_BALANCE);
    }


    @Test
    @Transactional
    public void getAllCashBooksByLocationIsEqualToSomething() throws Exception {
        // Initialize the database
        cashBookRepository.saveAndFlush(cashBook);
        Location location = LocationResourceIT.createEntity(em);
        em.persist(location);
        em.flush();
        cashBook.setLocation(location);
        cashBookRepository.saveAndFlush(cashBook);
        Long locationId = location.getId();

        // Get all the cashBookList where location equals to locationId
        defaultCashBookShouldBeFound("locationId.equals=" + locationId);

        // Get all the cashBookList where location equals to locationId + 1
        defaultCashBookShouldNotBeFound("locationId.equals=" + (locationId + 1));
    }


    @Test
    @Transactional
    public void getAllCashBooksByDocumentTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        cashBookRepository.saveAndFlush(cashBook);
        DocumentType documentType = DocumentTypeResourceIT.createEntity(em);
        em.persist(documentType);
        em.flush();
        cashBook.setDocumentType(documentType);
        cashBookRepository.saveAndFlush(cashBook);
        Long documentTypeId = documentType.getId();

        // Get all the cashBookList where documentType equals to documentTypeId
        defaultCashBookShouldBeFound("documentTypeId.equals=" + documentTypeId);

        // Get all the cashBookList where documentType equals to documentTypeId + 1
        defaultCashBookShouldNotBeFound("documentTypeId.equals=" + (documentTypeId + 1));
    }


    @Test
    @Transactional
    public void getAllCashBooksByItemIsEqualToSomething() throws Exception {
        // Initialize the database
        cashBookRepository.saveAndFlush(cashBook);
        Items item = ItemsResourceIT.createEntity(em);
        em.persist(item);
        em.flush();
        cashBook.setItem(item);
        cashBookRepository.saveAndFlush(cashBook);
        Long itemId = item.getId();

        // Get all the cashBookList where item equals to itemId
        defaultCashBookShouldBeFound("itemId.equals=" + itemId);

        // Get all the cashBookList where item equals to itemId + 1
        defaultCashBookShouldNotBeFound("itemId.equals=" + (itemId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCashBookShouldBeFound(String filter) throws Exception {
        restCashBookMockMvc.perform(get("/api/cash-books?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cashBook.getId().intValue())))
            .andExpect(jsonPath("$.[*].cashbookDate").value(hasItem(DEFAULT_CASHBOOK_DATE.toString())))
            .andExpect(jsonPath("$.[*].cashbookDescription").value(hasItem(DEFAULT_CASHBOOK_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].cashbookAmountCR").value(hasItem(DEFAULT_CASHBOOK_AMOUNT_CR.doubleValue())))
            .andExpect(jsonPath("$.[*].cashbookAmountDR").value(hasItem(DEFAULT_CASHBOOK_AMOUNT_DR.doubleValue())))
            .andExpect(jsonPath("$.[*].cashbookBalance").value(hasItem(DEFAULT_CASHBOOK_BALANCE.doubleValue())));

        // Check, that the count call also returns 1
        restCashBookMockMvc.perform(get("/api/cash-books/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCashBookShouldNotBeFound(String filter) throws Exception {
        restCashBookMockMvc.perform(get("/api/cash-books?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCashBookMockMvc.perform(get("/api/cash-books/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingCashBook() throws Exception {
        // Get the cashBook
        restCashBookMockMvc.perform(get("/api/cash-books/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCashBook() throws Exception {
        // Initialize the database
        cashBookService.save(cashBook);

        int databaseSizeBeforeUpdate = cashBookRepository.findAll().size();

        // Update the cashBook
        CashBook updatedCashBook = cashBookRepository.findById(cashBook.getId()).get();
        // Disconnect from session so that the updates on updatedCashBook are not directly saved in db
        em.detach(updatedCashBook);
        updatedCashBook
            .cashbookDate(UPDATED_CASHBOOK_DATE)
            .cashbookDescription(UPDATED_CASHBOOK_DESCRIPTION)
            .cashbookAmountCR(UPDATED_CASHBOOK_AMOUNT_CR)
            .cashbookAmountDR(UPDATED_CASHBOOK_AMOUNT_DR)
            .cashbookBalance(UPDATED_CASHBOOK_BALANCE);

        restCashBookMockMvc.perform(put("/api/cash-books")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCashBook)))
            .andExpect(status().isOk());

        // Validate the CashBook in the database
        List<CashBook> cashBookList = cashBookRepository.findAll();
        assertThat(cashBookList).hasSize(databaseSizeBeforeUpdate);
        CashBook testCashBook = cashBookList.get(cashBookList.size() - 1);
        assertThat(testCashBook.getCashbookDate()).isEqualTo(UPDATED_CASHBOOK_DATE);
        assertThat(testCashBook.getCashbookDescription()).isEqualTo(UPDATED_CASHBOOK_DESCRIPTION);
        assertThat(testCashBook.getCashbookAmountCR()).isEqualTo(UPDATED_CASHBOOK_AMOUNT_CR);
        assertThat(testCashBook.getCashbookAmountDR()).isEqualTo(UPDATED_CASHBOOK_AMOUNT_DR);
        assertThat(testCashBook.getCashbookBalance()).isEqualTo(UPDATED_CASHBOOK_BALANCE);
    }

    @Test
    @Transactional
    public void updateNonExistingCashBook() throws Exception {
        int databaseSizeBeforeUpdate = cashBookRepository.findAll().size();

        // Create the CashBook

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCashBookMockMvc.perform(put("/api/cash-books")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cashBook)))
            .andExpect(status().isBadRequest());

        // Validate the CashBook in the database
        List<CashBook> cashBookList = cashBookRepository.findAll();
        assertThat(cashBookList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCashBook() throws Exception {
        // Initialize the database
        cashBookService.save(cashBook);

        int databaseSizeBeforeDelete = cashBookRepository.findAll().size();

        // Delete the cashBook
        restCashBookMockMvc.perform(delete("/api/cash-books/{id}", cashBook.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CashBook> cashBookList = cashBookRepository.findAll();
        assertThat(cashBookList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CashBook.class);
        CashBook cashBook1 = new CashBook();
        cashBook1.setId(1L);
        CashBook cashBook2 = new CashBook();
        cashBook2.setId(cashBook1.getId());
        assertThat(cashBook1).isEqualTo(cashBook2);
        cashBook2.setId(2L);
        assertThat(cashBook1).isNotEqualTo(cashBook2);
        cashBook1.setId(null);
        assertThat(cashBook1).isNotEqualTo(cashBook2);
    }
}
