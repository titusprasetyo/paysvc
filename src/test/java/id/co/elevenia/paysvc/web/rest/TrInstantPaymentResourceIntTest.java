package id.co.elevenia.paysvc.web.rest;

import id.co.elevenia.paysvc.PaysvcApp;

import id.co.elevenia.paysvc.config.SecurityBeanOverrideConfiguration;

import id.co.elevenia.paysvc.domain.TrInstantPayment;
import id.co.elevenia.paysvc.repository.TrInstantPaymentRepository;
import id.co.elevenia.paysvc.repository.search.TrInstantPaymentSearchRepository;
import id.co.elevenia.paysvc.service.TrInstantPaymentService;
import id.co.elevenia.paysvc.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;


import static id.co.elevenia.paysvc.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the TrInstantPaymentResource REST controller.
 *
 * @see TrInstantPaymentResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SecurityBeanOverrideConfiguration.class, PaysvcApp.class})
public class TrInstantPaymentResourceIntTest {

    private static final Long DEFAULT_ORD_NO = 1L;
    private static final Long UPDATED_ORD_NO = 2L;

    private static final Long DEFAULT_AMOUNT = 1L;
    private static final Long UPDATED_AMOUNT = 2L;

    @Autowired
    private TrInstantPaymentRepository trInstantPaymentRepository;

    

    @Autowired
    private TrInstantPaymentService trInstantPaymentService;

    /**
     * This repository is mocked in the id.co.elevenia.paysvc.repository.search test package.
     *
     * @see id.co.elevenia.paysvc.repository.search.TrInstantPaymentSearchRepositoryMockConfiguration
     */
    @Autowired
    private TrInstantPaymentSearchRepository mockTrInstantPaymentSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTrInstantPaymentMockMvc;

    private TrInstantPayment trInstantPayment;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TrInstantPaymentResource trInstantPaymentResource = new TrInstantPaymentResource(trInstantPaymentService);
        this.restTrInstantPaymentMockMvc = MockMvcBuilders.standaloneSetup(trInstantPaymentResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TrInstantPayment createEntity(EntityManager em) {
        TrInstantPayment trInstantPayment = new TrInstantPayment()
            .ordNo(DEFAULT_ORD_NO)
            .amount(DEFAULT_AMOUNT);
        return trInstantPayment;
    }

    @Before
    public void initTest() {
        trInstantPayment = createEntity(em);
    }

    @Test
    @Transactional
    public void createTrInstantPayment() throws Exception {
        int databaseSizeBeforeCreate = trInstantPaymentRepository.findAll().size();

        // Create the TrInstantPayment
        restTrInstantPaymentMockMvc.perform(post("/api/tr-instant-payments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(trInstantPayment)))
            .andExpect(status().isCreated());

        // Validate the TrInstantPayment in the database
        List<TrInstantPayment> trInstantPaymentList = trInstantPaymentRepository.findAll();
        assertThat(trInstantPaymentList).hasSize(databaseSizeBeforeCreate + 1);
        TrInstantPayment testTrInstantPayment = trInstantPaymentList.get(trInstantPaymentList.size() - 1);
        assertThat(testTrInstantPayment.getOrdNo()).isEqualTo(DEFAULT_ORD_NO);
        assertThat(testTrInstantPayment.getAmount()).isEqualTo(DEFAULT_AMOUNT);

        // Validate the TrInstantPayment in Elasticsearch
        verify(mockTrInstantPaymentSearchRepository, times(1)).save(testTrInstantPayment);
    }

    @Test
    @Transactional
    public void createTrInstantPaymentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = trInstantPaymentRepository.findAll().size();

        // Create the TrInstantPayment with an existing ID
        trInstantPayment.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTrInstantPaymentMockMvc.perform(post("/api/tr-instant-payments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(trInstantPayment)))
            .andExpect(status().isBadRequest());

        // Validate the TrInstantPayment in the database
        List<TrInstantPayment> trInstantPaymentList = trInstantPaymentRepository.findAll();
        assertThat(trInstantPaymentList).hasSize(databaseSizeBeforeCreate);

        // Validate the TrInstantPayment in Elasticsearch
        verify(mockTrInstantPaymentSearchRepository, times(0)).save(trInstantPayment);
    }

    @Test
    @Transactional
    public void checkOrdNoIsRequired() throws Exception {
        int databaseSizeBeforeTest = trInstantPaymentRepository.findAll().size();
        // set the field null
        trInstantPayment.setOrdNo(null);

        // Create the TrInstantPayment, which fails.

        restTrInstantPaymentMockMvc.perform(post("/api/tr-instant-payments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(trInstantPayment)))
            .andExpect(status().isBadRequest());

        List<TrInstantPayment> trInstantPaymentList = trInstantPaymentRepository.findAll();
        assertThat(trInstantPaymentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTrInstantPayments() throws Exception {
        // Initialize the database
        trInstantPaymentRepository.saveAndFlush(trInstantPayment);

        // Get all the trInstantPaymentList
        restTrInstantPaymentMockMvc.perform(get("/api/tr-instant-payments?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(trInstantPayment.getId().intValue())))
            .andExpect(jsonPath("$.[*].ordNo").value(hasItem(DEFAULT_ORD_NO.intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.intValue())));
    }
    

    @Test
    @Transactional
    public void getTrInstantPayment() throws Exception {
        // Initialize the database
        trInstantPaymentRepository.saveAndFlush(trInstantPayment);

        // Get the trInstantPayment
        restTrInstantPaymentMockMvc.perform(get("/api/tr-instant-payments/{id}", trInstantPayment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(trInstantPayment.getId().intValue()))
            .andExpect(jsonPath("$.ordNo").value(DEFAULT_ORD_NO.intValue()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.intValue()));
    }
    @Test
    @Transactional
    public void getNonExistingTrInstantPayment() throws Exception {
        // Get the trInstantPayment
        restTrInstantPaymentMockMvc.perform(get("/api/tr-instant-payments/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTrInstantPayment() throws Exception {
        // Initialize the database
        trInstantPaymentService.save(trInstantPayment);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockTrInstantPaymentSearchRepository);

        int databaseSizeBeforeUpdate = trInstantPaymentRepository.findAll().size();

        // Update the trInstantPayment
        TrInstantPayment updatedTrInstantPayment = trInstantPaymentRepository.findById(trInstantPayment.getId()).get();
        // Disconnect from session so that the updates on updatedTrInstantPayment are not directly saved in db
        em.detach(updatedTrInstantPayment);
        updatedTrInstantPayment
            .ordNo(UPDATED_ORD_NO)
            .amount(UPDATED_AMOUNT);

        restTrInstantPaymentMockMvc.perform(put("/api/tr-instant-payments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTrInstantPayment)))
            .andExpect(status().isOk());

        // Validate the TrInstantPayment in the database
        List<TrInstantPayment> trInstantPaymentList = trInstantPaymentRepository.findAll();
        assertThat(trInstantPaymentList).hasSize(databaseSizeBeforeUpdate);
        TrInstantPayment testTrInstantPayment = trInstantPaymentList.get(trInstantPaymentList.size() - 1);
        assertThat(testTrInstantPayment.getOrdNo()).isEqualTo(UPDATED_ORD_NO);
        assertThat(testTrInstantPayment.getAmount()).isEqualTo(UPDATED_AMOUNT);

        // Validate the TrInstantPayment in Elasticsearch
        verify(mockTrInstantPaymentSearchRepository, times(1)).save(testTrInstantPayment);
    }

    @Test
    @Transactional
    public void updateNonExistingTrInstantPayment() throws Exception {
        int databaseSizeBeforeUpdate = trInstantPaymentRepository.findAll().size();

        // Create the TrInstantPayment

        // If the entity doesn't have an ID, it will throw BadRequestAlertException 
        restTrInstantPaymentMockMvc.perform(put("/api/tr-instant-payments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(trInstantPayment)))
            .andExpect(status().isBadRequest());

        // Validate the TrInstantPayment in the database
        List<TrInstantPayment> trInstantPaymentList = trInstantPaymentRepository.findAll();
        assertThat(trInstantPaymentList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TrInstantPayment in Elasticsearch
        verify(mockTrInstantPaymentSearchRepository, times(0)).save(trInstantPayment);
    }

    @Test
    @Transactional
    public void deleteTrInstantPayment() throws Exception {
        // Initialize the database
        trInstantPaymentService.save(trInstantPayment);

        int databaseSizeBeforeDelete = trInstantPaymentRepository.findAll().size();

        // Get the trInstantPayment
        restTrInstantPaymentMockMvc.perform(delete("/api/tr-instant-payments/{id}", trInstantPayment.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<TrInstantPayment> trInstantPaymentList = trInstantPaymentRepository.findAll();
        assertThat(trInstantPaymentList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the TrInstantPayment in Elasticsearch
        verify(mockTrInstantPaymentSearchRepository, times(1)).deleteById(trInstantPayment.getId());
    }

    @Test
    @Transactional
    public void searchTrInstantPayment() throws Exception {
        // Initialize the database
        trInstantPaymentService.save(trInstantPayment);
        when(mockTrInstantPaymentSearchRepository.search(queryStringQuery("id:" + trInstantPayment.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(trInstantPayment), PageRequest.of(0, 1), 1));
        // Search the trInstantPayment
        restTrInstantPaymentMockMvc.perform(get("/api/_search/tr-instant-payments?query=id:" + trInstantPayment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(trInstantPayment.getId().intValue())))
            .andExpect(jsonPath("$.[*].ordNo").value(hasItem(DEFAULT_ORD_NO.intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.intValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TrInstantPayment.class);
        TrInstantPayment trInstantPayment1 = new TrInstantPayment();
        trInstantPayment1.setId(1L);
        TrInstantPayment trInstantPayment2 = new TrInstantPayment();
        trInstantPayment2.setId(trInstantPayment1.getId());
        assertThat(trInstantPayment1).isEqualTo(trInstantPayment2);
        trInstantPayment2.setId(2L);
        assertThat(trInstantPayment1).isNotEqualTo(trInstantPayment2);
        trInstantPayment1.setId(null);
        assertThat(trInstantPayment1).isNotEqualTo(trInstantPayment2);
    }
}
