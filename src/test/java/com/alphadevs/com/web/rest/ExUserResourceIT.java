package com.alphadevs.com.web.rest;

import com.alphadevs.com.StockMngApp;
import com.alphadevs.com.domain.ExUser;
import com.alphadevs.com.domain.User;
import com.alphadevs.com.domain.Company;
import com.alphadevs.com.repository.ExUserRepository;
import com.alphadevs.com.service.ExUserService;
import com.alphadevs.com.web.rest.errors.ExceptionTranslator;
import com.alphadevs.com.service.dto.ExUserCriteria;
import com.alphadevs.com.service.ExUserQueryService;

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
 * Integration tests for the {@link ExUserResource} REST controller.
 */
@SpringBootTest(classes = StockMngApp.class)
public class ExUserResourceIT {

    private static final String DEFAULT_USER_KEY = "AAAAAAAAAA";
    private static final String UPDATED_USER_KEY = "BBBBBBBBBB";

    @Autowired
    private ExUserRepository exUserRepository;

    @Autowired
    private ExUserService exUserService;

    @Autowired
    private ExUserQueryService exUserQueryService;

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

    private MockMvc restExUserMockMvc;

    private ExUser exUser;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ExUserResource exUserResource = new ExUserResource(exUserService, exUserQueryService);
        this.restExUserMockMvc = MockMvcBuilders.standaloneSetup(exUserResource)
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
    public static ExUser createEntity(EntityManager em) {
        ExUser exUser = new ExUser()
            .userKey(DEFAULT_USER_KEY);
        return exUser;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExUser createUpdatedEntity(EntityManager em) {
        ExUser exUser = new ExUser()
            .userKey(UPDATED_USER_KEY);
        return exUser;
    }

    @BeforeEach
    public void initTest() {
        exUser = createEntity(em);
    }

    @Test
    @Transactional
    public void createExUser() throws Exception {
        int databaseSizeBeforeCreate = exUserRepository.findAll().size();

        // Create the ExUser
        restExUserMockMvc.perform(post("/api/ex-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(exUser)))
            .andExpect(status().isCreated());

        // Validate the ExUser in the database
        List<ExUser> exUserList = exUserRepository.findAll();
        assertThat(exUserList).hasSize(databaseSizeBeforeCreate + 1);
        ExUser testExUser = exUserList.get(exUserList.size() - 1);
        assertThat(testExUser.getUserKey()).isEqualTo(DEFAULT_USER_KEY);
    }

    @Test
    @Transactional
    public void createExUserWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = exUserRepository.findAll().size();

        // Create the ExUser with an existing ID
        exUser.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restExUserMockMvc.perform(post("/api/ex-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(exUser)))
            .andExpect(status().isBadRequest());

        // Validate the ExUser in the database
        List<ExUser> exUserList = exUserRepository.findAll();
        assertThat(exUserList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllExUsers() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList
        restExUserMockMvc.perform(get("/api/ex-users?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(exUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].userKey").value(hasItem(DEFAULT_USER_KEY.toString())));
    }
    
    @Test
    @Transactional
    public void getExUser() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get the exUser
        restExUserMockMvc.perform(get("/api/ex-users/{id}", exUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(exUser.getId().intValue()))
            .andExpect(jsonPath("$.userKey").value(DEFAULT_USER_KEY.toString()));
    }

    @Test
    @Transactional
    public void getAllExUsersByUserKeyIsEqualToSomething() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where userKey equals to DEFAULT_USER_KEY
        defaultExUserShouldBeFound("userKey.equals=" + DEFAULT_USER_KEY);

        // Get all the exUserList where userKey equals to UPDATED_USER_KEY
        defaultExUserShouldNotBeFound("userKey.equals=" + UPDATED_USER_KEY);
    }

    @Test
    @Transactional
    public void getAllExUsersByUserKeyIsInShouldWork() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where userKey in DEFAULT_USER_KEY or UPDATED_USER_KEY
        defaultExUserShouldBeFound("userKey.in=" + DEFAULT_USER_KEY + "," + UPDATED_USER_KEY);

        // Get all the exUserList where userKey equals to UPDATED_USER_KEY
        defaultExUserShouldNotBeFound("userKey.in=" + UPDATED_USER_KEY);
    }

    @Test
    @Transactional
    public void getAllExUsersByUserKeyIsNullOrNotNull() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where userKey is not null
        defaultExUserShouldBeFound("userKey.specified=true");

        // Get all the exUserList where userKey is null
        defaultExUserShouldNotBeFound("userKey.specified=false");
    }

    @Test
    @Transactional
    public void getAllExUsersByRelatedUserIsEqualToSomething() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);
        User relatedUser = UserResourceIT.createEntity(em);
        em.persist(relatedUser);
        em.flush();
        exUser.setRelatedUser(relatedUser);
        exUserRepository.saveAndFlush(exUser);
        Long relatedUserId = relatedUser.getId();

        // Get all the exUserList where relatedUser equals to relatedUserId
        defaultExUserShouldBeFound("relatedUserId.equals=" + relatedUserId);

        // Get all the exUserList where relatedUser equals to relatedUserId + 1
        defaultExUserShouldNotBeFound("relatedUserId.equals=" + (relatedUserId + 1));
    }


    @Test
    @Transactional
    public void getAllExUsersByCompanyIsEqualToSomething() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);
        Company company = CompanyResourceIT.createEntity(em);
        em.persist(company);
        em.flush();
        exUser.setCompany(company);
        exUserRepository.saveAndFlush(exUser);
        Long companyId = company.getId();

        // Get all the exUserList where company equals to companyId
        defaultExUserShouldBeFound("companyId.equals=" + companyId);

        // Get all the exUserList where company equals to companyId + 1
        defaultExUserShouldNotBeFound("companyId.equals=" + (companyId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultExUserShouldBeFound(String filter) throws Exception {
        restExUserMockMvc.perform(get("/api/ex-users?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(exUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].userKey").value(hasItem(DEFAULT_USER_KEY)));

        // Check, that the count call also returns 1
        restExUserMockMvc.perform(get("/api/ex-users/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultExUserShouldNotBeFound(String filter) throws Exception {
        restExUserMockMvc.perform(get("/api/ex-users?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restExUserMockMvc.perform(get("/api/ex-users/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingExUser() throws Exception {
        // Get the exUser
        restExUserMockMvc.perform(get("/api/ex-users/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateExUser() throws Exception {
        // Initialize the database
        exUserService.save(exUser);

        int databaseSizeBeforeUpdate = exUserRepository.findAll().size();

        // Update the exUser
        ExUser updatedExUser = exUserRepository.findById(exUser.getId()).get();
        // Disconnect from session so that the updates on updatedExUser are not directly saved in db
        em.detach(updatedExUser);
        updatedExUser
            .userKey(UPDATED_USER_KEY);

        restExUserMockMvc.perform(put("/api/ex-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedExUser)))
            .andExpect(status().isOk());

        // Validate the ExUser in the database
        List<ExUser> exUserList = exUserRepository.findAll();
        assertThat(exUserList).hasSize(databaseSizeBeforeUpdate);
        ExUser testExUser = exUserList.get(exUserList.size() - 1);
        assertThat(testExUser.getUserKey()).isEqualTo(UPDATED_USER_KEY);
    }

    @Test
    @Transactional
    public void updateNonExistingExUser() throws Exception {
        int databaseSizeBeforeUpdate = exUserRepository.findAll().size();

        // Create the ExUser

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExUserMockMvc.perform(put("/api/ex-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(exUser)))
            .andExpect(status().isBadRequest());

        // Validate the ExUser in the database
        List<ExUser> exUserList = exUserRepository.findAll();
        assertThat(exUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteExUser() throws Exception {
        // Initialize the database
        exUserService.save(exUser);

        int databaseSizeBeforeDelete = exUserRepository.findAll().size();

        // Delete the exUser
        restExUserMockMvc.perform(delete("/api/ex-users/{id}", exUser.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ExUser> exUserList = exUserRepository.findAll();
        assertThat(exUserList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExUser.class);
        ExUser exUser1 = new ExUser();
        exUser1.setId(1L);
        ExUser exUser2 = new ExUser();
        exUser2.setId(exUser1.getId());
        assertThat(exUser1).isEqualTo(exUser2);
        exUser2.setId(2L);
        assertThat(exUser1).isNotEqualTo(exUser2);
        exUser1.setId(null);
        assertThat(exUser1).isNotEqualTo(exUser2);
    }
}
