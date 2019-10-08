package com.alphadevs.com.web.rest;

import com.alphadevs.com.StockMngApp;
import com.alphadevs.com.domain.Desings;
import com.alphadevs.com.domain.Products;
import com.alphadevs.com.domain.Location;
import com.alphadevs.com.repository.DesingsRepository;
import com.alphadevs.com.service.DesingsService;
import com.alphadevs.com.web.rest.errors.ExceptionTranslator;
import com.alphadevs.com.service.dto.DesingsCriteria;
import com.alphadevs.com.service.DesingsQueryService;

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
import java.util.List;

import static com.alphadevs.com.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link DesingsResource} REST controller.
 */
@SpringBootTest(classes = StockMngApp.class)
public class DesingsResourceIT {

    private static final String DEFAULT_DESING_CODE = "AAAAAAAAAA";
    private static final String UPDATED_DESING_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_DESING_NAME = "AAAAAAAAAA";
    private static final String UPDATED_DESING_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESING_PREFIX = "AAAAAAAAAA";
    private static final String UPDATED_DESING_PREFIX = "BBBBBBBBBB";

    private static final Double DEFAULT_DESING_PROF_MARGIN = 1D;
    private static final Double UPDATED_DESING_PROF_MARGIN = 2D;
    private static final Double SMALLER_DESING_PROF_MARGIN = 1D - 1D;

    @Autowired
    private DesingsRepository desingsRepository;

    @Autowired
    private DesingsService desingsService;

    @Autowired
    private DesingsQueryService desingsQueryService;

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

    private MockMvc restDesingsMockMvc;

    private Desings desings;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DesingsResource desingsResource = new DesingsResource(desingsService, desingsQueryService);
        this.restDesingsMockMvc = MockMvcBuilders.standaloneSetup(desingsResource)
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
    public static Desings createEntity(EntityManager em) {
        Desings desings = new Desings()
            .desingCode(DEFAULT_DESING_CODE)
            .desingName(DEFAULT_DESING_NAME)
            .desingPrefix(DEFAULT_DESING_PREFIX)
            .desingProfMargin(DEFAULT_DESING_PROF_MARGIN);
        return desings;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Desings createUpdatedEntity(EntityManager em) {
        Desings desings = new Desings()
            .desingCode(UPDATED_DESING_CODE)
            .desingName(UPDATED_DESING_NAME)
            .desingPrefix(UPDATED_DESING_PREFIX)
            .desingProfMargin(UPDATED_DESING_PROF_MARGIN);
        return desings;
    }

    @BeforeEach
    public void initTest() {
        desings = createEntity(em);
    }

    @Test
    @Transactional
    public void createDesings() throws Exception {
        int databaseSizeBeforeCreate = desingsRepository.findAll().size();

        // Create the Desings
        restDesingsMockMvc.perform(post("/api/desings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(desings)))
            .andExpect(status().isCreated());

        // Validate the Desings in the database
        List<Desings> desingsList = desingsRepository.findAll();
        assertThat(desingsList).hasSize(databaseSizeBeforeCreate + 1);
        Desings testDesings = desingsList.get(desingsList.size() - 1);
        assertThat(testDesings.getDesingCode()).isEqualTo(DEFAULT_DESING_CODE);
        assertThat(testDesings.getDesingName()).isEqualTo(DEFAULT_DESING_NAME);
        assertThat(testDesings.getDesingPrefix()).isEqualTo(DEFAULT_DESING_PREFIX);
        assertThat(testDesings.getDesingProfMargin()).isEqualTo(DEFAULT_DESING_PROF_MARGIN);
    }

    @Test
    @Transactional
    public void createDesingsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = desingsRepository.findAll().size();

        // Create the Desings with an existing ID
        desings.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDesingsMockMvc.perform(post("/api/desings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(desings)))
            .andExpect(status().isBadRequest());

        // Validate the Desings in the database
        List<Desings> desingsList = desingsRepository.findAll();
        assertThat(desingsList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkDesingCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = desingsRepository.findAll().size();
        // set the field null
        desings.setDesingCode(null);

        // Create the Desings, which fails.

        restDesingsMockMvc.perform(post("/api/desings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(desings)))
            .andExpect(status().isBadRequest());

        List<Desings> desingsList = desingsRepository.findAll();
        assertThat(desingsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDesingNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = desingsRepository.findAll().size();
        // set the field null
        desings.setDesingName(null);

        // Create the Desings, which fails.

        restDesingsMockMvc.perform(post("/api/desings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(desings)))
            .andExpect(status().isBadRequest());

        List<Desings> desingsList = desingsRepository.findAll();
        assertThat(desingsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDesingPrefixIsRequired() throws Exception {
        int databaseSizeBeforeTest = desingsRepository.findAll().size();
        // set the field null
        desings.setDesingPrefix(null);

        // Create the Desings, which fails.

        restDesingsMockMvc.perform(post("/api/desings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(desings)))
            .andExpect(status().isBadRequest());

        List<Desings> desingsList = desingsRepository.findAll();
        assertThat(desingsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDesingProfMarginIsRequired() throws Exception {
        int databaseSizeBeforeTest = desingsRepository.findAll().size();
        // set the field null
        desings.setDesingProfMargin(null);

        // Create the Desings, which fails.

        restDesingsMockMvc.perform(post("/api/desings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(desings)))
            .andExpect(status().isBadRequest());

        List<Desings> desingsList = desingsRepository.findAll();
        assertThat(desingsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDesings() throws Exception {
        // Initialize the database
        desingsRepository.saveAndFlush(desings);

        // Get all the desingsList
        restDesingsMockMvc.perform(get("/api/desings?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(desings.getId().intValue())))
            .andExpect(jsonPath("$.[*].desingCode").value(hasItem(DEFAULT_DESING_CODE.toString())))
            .andExpect(jsonPath("$.[*].desingName").value(hasItem(DEFAULT_DESING_NAME.toString())))
            .andExpect(jsonPath("$.[*].desingPrefix").value(hasItem(DEFAULT_DESING_PREFIX.toString())))
            .andExpect(jsonPath("$.[*].desingProfMargin").value(hasItem(DEFAULT_DESING_PROF_MARGIN.doubleValue())));
    }
    
    @Test
    @Transactional
    public void getDesings() throws Exception {
        // Initialize the database
        desingsRepository.saveAndFlush(desings);

        // Get the desings
        restDesingsMockMvc.perform(get("/api/desings/{id}", desings.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(desings.getId().intValue()))
            .andExpect(jsonPath("$.desingCode").value(DEFAULT_DESING_CODE.toString()))
            .andExpect(jsonPath("$.desingName").value(DEFAULT_DESING_NAME.toString()))
            .andExpect(jsonPath("$.desingPrefix").value(DEFAULT_DESING_PREFIX.toString()))
            .andExpect(jsonPath("$.desingProfMargin").value(DEFAULT_DESING_PROF_MARGIN.doubleValue()));
    }

    @Test
    @Transactional
    public void getAllDesingsByDesingCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        desingsRepository.saveAndFlush(desings);

        // Get all the desingsList where desingCode equals to DEFAULT_DESING_CODE
        defaultDesingsShouldBeFound("desingCode.equals=" + DEFAULT_DESING_CODE);

        // Get all the desingsList where desingCode equals to UPDATED_DESING_CODE
        defaultDesingsShouldNotBeFound("desingCode.equals=" + UPDATED_DESING_CODE);
    }

    @Test
    @Transactional
    public void getAllDesingsByDesingCodeIsInShouldWork() throws Exception {
        // Initialize the database
        desingsRepository.saveAndFlush(desings);

        // Get all the desingsList where desingCode in DEFAULT_DESING_CODE or UPDATED_DESING_CODE
        defaultDesingsShouldBeFound("desingCode.in=" + DEFAULT_DESING_CODE + "," + UPDATED_DESING_CODE);

        // Get all the desingsList where desingCode equals to UPDATED_DESING_CODE
        defaultDesingsShouldNotBeFound("desingCode.in=" + UPDATED_DESING_CODE);
    }

    @Test
    @Transactional
    public void getAllDesingsByDesingCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        desingsRepository.saveAndFlush(desings);

        // Get all the desingsList where desingCode is not null
        defaultDesingsShouldBeFound("desingCode.specified=true");

        // Get all the desingsList where desingCode is null
        defaultDesingsShouldNotBeFound("desingCode.specified=false");
    }

    @Test
    @Transactional
    public void getAllDesingsByDesingNameIsEqualToSomething() throws Exception {
        // Initialize the database
        desingsRepository.saveAndFlush(desings);

        // Get all the desingsList where desingName equals to DEFAULT_DESING_NAME
        defaultDesingsShouldBeFound("desingName.equals=" + DEFAULT_DESING_NAME);

        // Get all the desingsList where desingName equals to UPDATED_DESING_NAME
        defaultDesingsShouldNotBeFound("desingName.equals=" + UPDATED_DESING_NAME);
    }

    @Test
    @Transactional
    public void getAllDesingsByDesingNameIsInShouldWork() throws Exception {
        // Initialize the database
        desingsRepository.saveAndFlush(desings);

        // Get all the desingsList where desingName in DEFAULT_DESING_NAME or UPDATED_DESING_NAME
        defaultDesingsShouldBeFound("desingName.in=" + DEFAULT_DESING_NAME + "," + UPDATED_DESING_NAME);

        // Get all the desingsList where desingName equals to UPDATED_DESING_NAME
        defaultDesingsShouldNotBeFound("desingName.in=" + UPDATED_DESING_NAME);
    }

    @Test
    @Transactional
    public void getAllDesingsByDesingNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        desingsRepository.saveAndFlush(desings);

        // Get all the desingsList where desingName is not null
        defaultDesingsShouldBeFound("desingName.specified=true");

        // Get all the desingsList where desingName is null
        defaultDesingsShouldNotBeFound("desingName.specified=false");
    }

    @Test
    @Transactional
    public void getAllDesingsByDesingPrefixIsEqualToSomething() throws Exception {
        // Initialize the database
        desingsRepository.saveAndFlush(desings);

        // Get all the desingsList where desingPrefix equals to DEFAULT_DESING_PREFIX
        defaultDesingsShouldBeFound("desingPrefix.equals=" + DEFAULT_DESING_PREFIX);

        // Get all the desingsList where desingPrefix equals to UPDATED_DESING_PREFIX
        defaultDesingsShouldNotBeFound("desingPrefix.equals=" + UPDATED_DESING_PREFIX);
    }

    @Test
    @Transactional
    public void getAllDesingsByDesingPrefixIsInShouldWork() throws Exception {
        // Initialize the database
        desingsRepository.saveAndFlush(desings);

        // Get all the desingsList where desingPrefix in DEFAULT_DESING_PREFIX or UPDATED_DESING_PREFIX
        defaultDesingsShouldBeFound("desingPrefix.in=" + DEFAULT_DESING_PREFIX + "," + UPDATED_DESING_PREFIX);

        // Get all the desingsList where desingPrefix equals to UPDATED_DESING_PREFIX
        defaultDesingsShouldNotBeFound("desingPrefix.in=" + UPDATED_DESING_PREFIX);
    }

    @Test
    @Transactional
    public void getAllDesingsByDesingPrefixIsNullOrNotNull() throws Exception {
        // Initialize the database
        desingsRepository.saveAndFlush(desings);

        // Get all the desingsList where desingPrefix is not null
        defaultDesingsShouldBeFound("desingPrefix.specified=true");

        // Get all the desingsList where desingPrefix is null
        defaultDesingsShouldNotBeFound("desingPrefix.specified=false");
    }

    @Test
    @Transactional
    public void getAllDesingsByDesingProfMarginIsEqualToSomething() throws Exception {
        // Initialize the database
        desingsRepository.saveAndFlush(desings);

        // Get all the desingsList where desingProfMargin equals to DEFAULT_DESING_PROF_MARGIN
        defaultDesingsShouldBeFound("desingProfMargin.equals=" + DEFAULT_DESING_PROF_MARGIN);

        // Get all the desingsList where desingProfMargin equals to UPDATED_DESING_PROF_MARGIN
        defaultDesingsShouldNotBeFound("desingProfMargin.equals=" + UPDATED_DESING_PROF_MARGIN);
    }

    @Test
    @Transactional
    public void getAllDesingsByDesingProfMarginIsInShouldWork() throws Exception {
        // Initialize the database
        desingsRepository.saveAndFlush(desings);

        // Get all the desingsList where desingProfMargin in DEFAULT_DESING_PROF_MARGIN or UPDATED_DESING_PROF_MARGIN
        defaultDesingsShouldBeFound("desingProfMargin.in=" + DEFAULT_DESING_PROF_MARGIN + "," + UPDATED_DESING_PROF_MARGIN);

        // Get all the desingsList where desingProfMargin equals to UPDATED_DESING_PROF_MARGIN
        defaultDesingsShouldNotBeFound("desingProfMargin.in=" + UPDATED_DESING_PROF_MARGIN);
    }

    @Test
    @Transactional
    public void getAllDesingsByDesingProfMarginIsNullOrNotNull() throws Exception {
        // Initialize the database
        desingsRepository.saveAndFlush(desings);

        // Get all the desingsList where desingProfMargin is not null
        defaultDesingsShouldBeFound("desingProfMargin.specified=true");

        // Get all the desingsList where desingProfMargin is null
        defaultDesingsShouldNotBeFound("desingProfMargin.specified=false");
    }

    @Test
    @Transactional
    public void getAllDesingsByDesingProfMarginIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        desingsRepository.saveAndFlush(desings);

        // Get all the desingsList where desingProfMargin is greater than or equal to DEFAULT_DESING_PROF_MARGIN
        defaultDesingsShouldBeFound("desingProfMargin.greaterThanOrEqual=" + DEFAULT_DESING_PROF_MARGIN);

        // Get all the desingsList where desingProfMargin is greater than or equal to UPDATED_DESING_PROF_MARGIN
        defaultDesingsShouldNotBeFound("desingProfMargin.greaterThanOrEqual=" + UPDATED_DESING_PROF_MARGIN);
    }

    @Test
    @Transactional
    public void getAllDesingsByDesingProfMarginIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        desingsRepository.saveAndFlush(desings);

        // Get all the desingsList where desingProfMargin is less than or equal to DEFAULT_DESING_PROF_MARGIN
        defaultDesingsShouldBeFound("desingProfMargin.lessThanOrEqual=" + DEFAULT_DESING_PROF_MARGIN);

        // Get all the desingsList where desingProfMargin is less than or equal to SMALLER_DESING_PROF_MARGIN
        defaultDesingsShouldNotBeFound("desingProfMargin.lessThanOrEqual=" + SMALLER_DESING_PROF_MARGIN);
    }

    @Test
    @Transactional
    public void getAllDesingsByDesingProfMarginIsLessThanSomething() throws Exception {
        // Initialize the database
        desingsRepository.saveAndFlush(desings);

        // Get all the desingsList where desingProfMargin is less than DEFAULT_DESING_PROF_MARGIN
        defaultDesingsShouldNotBeFound("desingProfMargin.lessThan=" + DEFAULT_DESING_PROF_MARGIN);

        // Get all the desingsList where desingProfMargin is less than UPDATED_DESING_PROF_MARGIN
        defaultDesingsShouldBeFound("desingProfMargin.lessThan=" + UPDATED_DESING_PROF_MARGIN);
    }

    @Test
    @Transactional
    public void getAllDesingsByDesingProfMarginIsGreaterThanSomething() throws Exception {
        // Initialize the database
        desingsRepository.saveAndFlush(desings);

        // Get all the desingsList where desingProfMargin is greater than DEFAULT_DESING_PROF_MARGIN
        defaultDesingsShouldNotBeFound("desingProfMargin.greaterThan=" + DEFAULT_DESING_PROF_MARGIN);

        // Get all the desingsList where desingProfMargin is greater than SMALLER_DESING_PROF_MARGIN
        defaultDesingsShouldBeFound("desingProfMargin.greaterThan=" + SMALLER_DESING_PROF_MARGIN);
    }


    @Test
    @Transactional
    public void getAllDesingsByRelatedProductIsEqualToSomething() throws Exception {
        // Initialize the database
        desingsRepository.saveAndFlush(desings);
        Products relatedProduct = ProductsResourceIT.createEntity(em);
        em.persist(relatedProduct);
        em.flush();
        desings.setRelatedProduct(relatedProduct);
        desingsRepository.saveAndFlush(desings);
        Long relatedProductId = relatedProduct.getId();

        // Get all the desingsList where relatedProduct equals to relatedProductId
        defaultDesingsShouldBeFound("relatedProductId.equals=" + relatedProductId);

        // Get all the desingsList where relatedProduct equals to relatedProductId + 1
        defaultDesingsShouldNotBeFound("relatedProductId.equals=" + (relatedProductId + 1));
    }


    @Test
    @Transactional
    public void getAllDesingsByLocationIsEqualToSomething() throws Exception {
        // Initialize the database
        desingsRepository.saveAndFlush(desings);
        Location location = LocationResourceIT.createEntity(em);
        em.persist(location);
        em.flush();
        desings.setLocation(location);
        desingsRepository.saveAndFlush(desings);
        Long locationId = location.getId();

        // Get all the desingsList where location equals to locationId
        defaultDesingsShouldBeFound("locationId.equals=" + locationId);

        // Get all the desingsList where location equals to locationId + 1
        defaultDesingsShouldNotBeFound("locationId.equals=" + (locationId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDesingsShouldBeFound(String filter) throws Exception {
        restDesingsMockMvc.perform(get("/api/desings?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(desings.getId().intValue())))
            .andExpect(jsonPath("$.[*].desingCode").value(hasItem(DEFAULT_DESING_CODE)))
            .andExpect(jsonPath("$.[*].desingName").value(hasItem(DEFAULT_DESING_NAME)))
            .andExpect(jsonPath("$.[*].desingPrefix").value(hasItem(DEFAULT_DESING_PREFIX)))
            .andExpect(jsonPath("$.[*].desingProfMargin").value(hasItem(DEFAULT_DESING_PROF_MARGIN.doubleValue())));

        // Check, that the count call also returns 1
        restDesingsMockMvc.perform(get("/api/desings/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDesingsShouldNotBeFound(String filter) throws Exception {
        restDesingsMockMvc.perform(get("/api/desings?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDesingsMockMvc.perform(get("/api/desings/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingDesings() throws Exception {
        // Get the desings
        restDesingsMockMvc.perform(get("/api/desings/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDesings() throws Exception {
        // Initialize the database
        desingsService.save(desings);

        int databaseSizeBeforeUpdate = desingsRepository.findAll().size();

        // Update the desings
        Desings updatedDesings = desingsRepository.findById(desings.getId()).get();
        // Disconnect from session so that the updates on updatedDesings are not directly saved in db
        em.detach(updatedDesings);
        updatedDesings
            .desingCode(UPDATED_DESING_CODE)
            .desingName(UPDATED_DESING_NAME)
            .desingPrefix(UPDATED_DESING_PREFIX)
            .desingProfMargin(UPDATED_DESING_PROF_MARGIN);

        restDesingsMockMvc.perform(put("/api/desings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDesings)))
            .andExpect(status().isOk());

        // Validate the Desings in the database
        List<Desings> desingsList = desingsRepository.findAll();
        assertThat(desingsList).hasSize(databaseSizeBeforeUpdate);
        Desings testDesings = desingsList.get(desingsList.size() - 1);
        assertThat(testDesings.getDesingCode()).isEqualTo(UPDATED_DESING_CODE);
        assertThat(testDesings.getDesingName()).isEqualTo(UPDATED_DESING_NAME);
        assertThat(testDesings.getDesingPrefix()).isEqualTo(UPDATED_DESING_PREFIX);
        assertThat(testDesings.getDesingProfMargin()).isEqualTo(UPDATED_DESING_PROF_MARGIN);
    }

    @Test
    @Transactional
    public void updateNonExistingDesings() throws Exception {
        int databaseSizeBeforeUpdate = desingsRepository.findAll().size();

        // Create the Desings

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDesingsMockMvc.perform(put("/api/desings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(desings)))
            .andExpect(status().isBadRequest());

        // Validate the Desings in the database
        List<Desings> desingsList = desingsRepository.findAll();
        assertThat(desingsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteDesings() throws Exception {
        // Initialize the database
        desingsService.save(desings);

        int databaseSizeBeforeDelete = desingsRepository.findAll().size();

        // Delete the desings
        restDesingsMockMvc.perform(delete("/api/desings/{id}", desings.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Desings> desingsList = desingsRepository.findAll();
        assertThat(desingsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Desings.class);
        Desings desings1 = new Desings();
        desings1.setId(1L);
        Desings desings2 = new Desings();
        desings2.setId(desings1.getId());
        assertThat(desings1).isEqualTo(desings2);
        desings2.setId(2L);
        assertThat(desings1).isNotEqualTo(desings2);
        desings1.setId(null);
        assertThat(desings1).isNotEqualTo(desings2);
    }
}
