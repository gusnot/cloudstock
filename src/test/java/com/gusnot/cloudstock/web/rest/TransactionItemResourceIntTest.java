package com.gusnot.cloudstock.web.rest;

import com.gusnot.cloudstock.CloudstockApp;

import com.gusnot.cloudstock.domain.TransactionItem;
import com.gusnot.cloudstock.repository.TransactionItemRepository;
import com.gusnot.cloudstock.service.TransactionItemService;
import com.gusnot.cloudstock.repository.search.TransactionItemSearchRepository;
import com.gusnot.cloudstock.service.dto.TransactionItemDTO;
import com.gusnot.cloudstock.service.mapper.TransactionItemMapper;
import com.gusnot.cloudstock.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the TransactionItemResource REST controller.
 *
 * @see TransactionItemResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CloudstockApp.class)
public class TransactionItemResourceIntTest {

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);

    @Autowired
    private TransactionItemRepository transactionItemRepository;

    @Autowired
    private TransactionItemMapper transactionItemMapper;

    @Autowired
    private TransactionItemService transactionItemService;

    @Autowired
    private TransactionItemSearchRepository transactionItemSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTransactionItemMockMvc;

    private TransactionItem transactionItem;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TransactionItemResource transactionItemResource = new TransactionItemResource(transactionItemService);
        this.restTransactionItemMockMvc = MockMvcBuilders.standaloneSetup(transactionItemResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TransactionItem createEntity(EntityManager em) {
        TransactionItem transactionItem = new TransactionItem()
            .amount(DEFAULT_AMOUNT);
        return transactionItem;
    }

    @Before
    public void initTest() {
        transactionItemSearchRepository.deleteAll();
        transactionItem = createEntity(em);
    }

    @Test
    @Transactional
    public void createTransactionItem() throws Exception {
        int databaseSizeBeforeCreate = transactionItemRepository.findAll().size();

        // Create the TransactionItem
        TransactionItemDTO transactionItemDTO = transactionItemMapper.toDto(transactionItem);
        restTransactionItemMockMvc.perform(post("/api/transaction-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transactionItemDTO)))
            .andExpect(status().isCreated());

        // Validate the TransactionItem in the database
        List<TransactionItem> transactionItemList = transactionItemRepository.findAll();
        assertThat(transactionItemList).hasSize(databaseSizeBeforeCreate + 1);
        TransactionItem testTransactionItem = transactionItemList.get(transactionItemList.size() - 1);
        assertThat(testTransactionItem.getAmount()).isEqualTo(DEFAULT_AMOUNT);

        // Validate the TransactionItem in Elasticsearch
        TransactionItem transactionItemEs = transactionItemSearchRepository.findOne(testTransactionItem.getId());
        assertThat(transactionItemEs).isEqualToComparingFieldByField(testTransactionItem);
    }

    @Test
    @Transactional
    public void createTransactionItemWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = transactionItemRepository.findAll().size();

        // Create the TransactionItem with an existing ID
        transactionItem.setId(1L);
        TransactionItemDTO transactionItemDTO = transactionItemMapper.toDto(transactionItem);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTransactionItemMockMvc.perform(post("/api/transaction-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transactionItemDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TransactionItem in the database
        List<TransactionItem> transactionItemList = transactionItemRepository.findAll();
        assertThat(transactionItemList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = transactionItemRepository.findAll().size();
        // set the field null
        transactionItem.setAmount(null);

        // Create the TransactionItem, which fails.
        TransactionItemDTO transactionItemDTO = transactionItemMapper.toDto(transactionItem);

        restTransactionItemMockMvc.perform(post("/api/transaction-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transactionItemDTO)))
            .andExpect(status().isBadRequest());

        List<TransactionItem> transactionItemList = transactionItemRepository.findAll();
        assertThat(transactionItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTransactionItems() throws Exception {
        // Initialize the database
        transactionItemRepository.saveAndFlush(transactionItem);

        // Get all the transactionItemList
        restTransactionItemMockMvc.perform(get("/api/transaction-items?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transactionItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.intValue())));
    }

    @Test
    @Transactional
    public void getTransactionItem() throws Exception {
        // Initialize the database
        transactionItemRepository.saveAndFlush(transactionItem);

        // Get the transactionItem
        restTransactionItemMockMvc.perform(get("/api/transaction-items/{id}", transactionItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(transactionItem.getId().intValue()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingTransactionItem() throws Exception {
        // Get the transactionItem
        restTransactionItemMockMvc.perform(get("/api/transaction-items/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTransactionItem() throws Exception {
        // Initialize the database
        transactionItemRepository.saveAndFlush(transactionItem);
        transactionItemSearchRepository.save(transactionItem);
        int databaseSizeBeforeUpdate = transactionItemRepository.findAll().size();

        // Update the transactionItem
        TransactionItem updatedTransactionItem = transactionItemRepository.findOne(transactionItem.getId());
        updatedTransactionItem
            .amount(UPDATED_AMOUNT);
        TransactionItemDTO transactionItemDTO = transactionItemMapper.toDto(updatedTransactionItem);

        restTransactionItemMockMvc.perform(put("/api/transaction-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transactionItemDTO)))
            .andExpect(status().isOk());

        // Validate the TransactionItem in the database
        List<TransactionItem> transactionItemList = transactionItemRepository.findAll();
        assertThat(transactionItemList).hasSize(databaseSizeBeforeUpdate);
        TransactionItem testTransactionItem = transactionItemList.get(transactionItemList.size() - 1);
        assertThat(testTransactionItem.getAmount()).isEqualTo(UPDATED_AMOUNT);

        // Validate the TransactionItem in Elasticsearch
        TransactionItem transactionItemEs = transactionItemSearchRepository.findOne(testTransactionItem.getId());
        assertThat(transactionItemEs).isEqualToComparingFieldByField(testTransactionItem);
    }

    @Test
    @Transactional
    public void updateNonExistingTransactionItem() throws Exception {
        int databaseSizeBeforeUpdate = transactionItemRepository.findAll().size();

        // Create the TransactionItem
        TransactionItemDTO transactionItemDTO = transactionItemMapper.toDto(transactionItem);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTransactionItemMockMvc.perform(put("/api/transaction-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transactionItemDTO)))
            .andExpect(status().isCreated());

        // Validate the TransactionItem in the database
        List<TransactionItem> transactionItemList = transactionItemRepository.findAll();
        assertThat(transactionItemList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTransactionItem() throws Exception {
        // Initialize the database
        transactionItemRepository.saveAndFlush(transactionItem);
        transactionItemSearchRepository.save(transactionItem);
        int databaseSizeBeforeDelete = transactionItemRepository.findAll().size();

        // Get the transactionItem
        restTransactionItemMockMvc.perform(delete("/api/transaction-items/{id}", transactionItem.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean transactionItemExistsInEs = transactionItemSearchRepository.exists(transactionItem.getId());
        assertThat(transactionItemExistsInEs).isFalse();

        // Validate the database is empty
        List<TransactionItem> transactionItemList = transactionItemRepository.findAll();
        assertThat(transactionItemList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchTransactionItem() throws Exception {
        // Initialize the database
        transactionItemRepository.saveAndFlush(transactionItem);
        transactionItemSearchRepository.save(transactionItem);

        // Search the transactionItem
        restTransactionItemMockMvc.perform(get("/api/_search/transaction-items?query=id:" + transactionItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transactionItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.intValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TransactionItem.class);
        TransactionItem transactionItem1 = new TransactionItem();
        transactionItem1.setId(1L);
        TransactionItem transactionItem2 = new TransactionItem();
        transactionItem2.setId(transactionItem1.getId());
        assertThat(transactionItem1).isEqualTo(transactionItem2);
        transactionItem2.setId(2L);
        assertThat(transactionItem1).isNotEqualTo(transactionItem2);
        transactionItem1.setId(null);
        assertThat(transactionItem1).isNotEqualTo(transactionItem2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TransactionItemDTO.class);
        TransactionItemDTO transactionItemDTO1 = new TransactionItemDTO();
        transactionItemDTO1.setId(1L);
        TransactionItemDTO transactionItemDTO2 = new TransactionItemDTO();
        assertThat(transactionItemDTO1).isNotEqualTo(transactionItemDTO2);
        transactionItemDTO2.setId(transactionItemDTO1.getId());
        assertThat(transactionItemDTO1).isEqualTo(transactionItemDTO2);
        transactionItemDTO2.setId(2L);
        assertThat(transactionItemDTO1).isNotEqualTo(transactionItemDTO2);
        transactionItemDTO1.setId(null);
        assertThat(transactionItemDTO1).isNotEqualTo(transactionItemDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(transactionItemMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(transactionItemMapper.fromId(null)).isNull();
    }
}
