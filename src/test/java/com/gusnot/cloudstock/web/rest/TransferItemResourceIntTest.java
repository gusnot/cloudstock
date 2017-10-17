package com.gusnot.cloudstock.web.rest;

import com.gusnot.cloudstock.CloudstockApp;

import com.gusnot.cloudstock.domain.TransferItem;
import com.gusnot.cloudstock.repository.TransferItemRepository;
import com.gusnot.cloudstock.service.TransferItemService;
import com.gusnot.cloudstock.repository.search.TransferItemSearchRepository;
import com.gusnot.cloudstock.service.dto.TransferItemDTO;
import com.gusnot.cloudstock.service.mapper.TransferItemMapper;
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
 * Test class for the TransferItemResource REST controller.
 *
 * @see TransferItemResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CloudstockApp.class)
public class TransferItemResourceIntTest {

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);

    @Autowired
    private TransferItemRepository transferItemRepository;

    @Autowired
    private TransferItemMapper transferItemMapper;

    @Autowired
    private TransferItemService transferItemService;

    @Autowired
    private TransferItemSearchRepository transferItemSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTransferItemMockMvc;

    private TransferItem transferItem;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TransferItemResource transferItemResource = new TransferItemResource(transferItemService);
        this.restTransferItemMockMvc = MockMvcBuilders.standaloneSetup(transferItemResource)
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
    public static TransferItem createEntity(EntityManager em) {
        TransferItem transferItem = new TransferItem()
            .amount(DEFAULT_AMOUNT);
        return transferItem;
    }

    @Before
    public void initTest() {
        transferItemSearchRepository.deleteAll();
        transferItem = createEntity(em);
    }

    @Test
    @Transactional
    public void createTransferItem() throws Exception {
        int databaseSizeBeforeCreate = transferItemRepository.findAll().size();

        // Create the TransferItem
        TransferItemDTO transferItemDTO = transferItemMapper.toDto(transferItem);
        restTransferItemMockMvc.perform(post("/api/transfer-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transferItemDTO)))
            .andExpect(status().isCreated());

        // Validate the TransferItem in the database
        List<TransferItem> transferItemList = transferItemRepository.findAll();
        assertThat(transferItemList).hasSize(databaseSizeBeforeCreate + 1);
        TransferItem testTransferItem = transferItemList.get(transferItemList.size() - 1);
        assertThat(testTransferItem.getAmount()).isEqualTo(DEFAULT_AMOUNT);

        // Validate the TransferItem in Elasticsearch
        TransferItem transferItemEs = transferItemSearchRepository.findOne(testTransferItem.getId());
        assertThat(transferItemEs).isEqualToComparingFieldByField(testTransferItem);
    }

    @Test
    @Transactional
    public void createTransferItemWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = transferItemRepository.findAll().size();

        // Create the TransferItem with an existing ID
        transferItem.setId(1L);
        TransferItemDTO transferItemDTO = transferItemMapper.toDto(transferItem);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTransferItemMockMvc.perform(post("/api/transfer-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transferItemDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TransferItem in the database
        List<TransferItem> transferItemList = transferItemRepository.findAll();
        assertThat(transferItemList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = transferItemRepository.findAll().size();
        // set the field null
        transferItem.setAmount(null);

        // Create the TransferItem, which fails.
        TransferItemDTO transferItemDTO = transferItemMapper.toDto(transferItem);

        restTransferItemMockMvc.perform(post("/api/transfer-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transferItemDTO)))
            .andExpect(status().isBadRequest());

        List<TransferItem> transferItemList = transferItemRepository.findAll();
        assertThat(transferItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTransferItems() throws Exception {
        // Initialize the database
        transferItemRepository.saveAndFlush(transferItem);

        // Get all the transferItemList
        restTransferItemMockMvc.perform(get("/api/transfer-items?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transferItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.intValue())));
    }

    @Test
    @Transactional
    public void getTransferItem() throws Exception {
        // Initialize the database
        transferItemRepository.saveAndFlush(transferItem);

        // Get the transferItem
        restTransferItemMockMvc.perform(get("/api/transfer-items/{id}", transferItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(transferItem.getId().intValue()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingTransferItem() throws Exception {
        // Get the transferItem
        restTransferItemMockMvc.perform(get("/api/transfer-items/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTransferItem() throws Exception {
        // Initialize the database
        transferItemRepository.saveAndFlush(transferItem);
        transferItemSearchRepository.save(transferItem);
        int databaseSizeBeforeUpdate = transferItemRepository.findAll().size();

        // Update the transferItem
        TransferItem updatedTransferItem = transferItemRepository.findOne(transferItem.getId());
        updatedTransferItem
            .amount(UPDATED_AMOUNT);
        TransferItemDTO transferItemDTO = transferItemMapper.toDto(updatedTransferItem);

        restTransferItemMockMvc.perform(put("/api/transfer-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transferItemDTO)))
            .andExpect(status().isOk());

        // Validate the TransferItem in the database
        List<TransferItem> transferItemList = transferItemRepository.findAll();
        assertThat(transferItemList).hasSize(databaseSizeBeforeUpdate);
        TransferItem testTransferItem = transferItemList.get(transferItemList.size() - 1);
        assertThat(testTransferItem.getAmount()).isEqualTo(UPDATED_AMOUNT);

        // Validate the TransferItem in Elasticsearch
        TransferItem transferItemEs = transferItemSearchRepository.findOne(testTransferItem.getId());
        assertThat(transferItemEs).isEqualToComparingFieldByField(testTransferItem);
    }

    @Test
    @Transactional
    public void updateNonExistingTransferItem() throws Exception {
        int databaseSizeBeforeUpdate = transferItemRepository.findAll().size();

        // Create the TransferItem
        TransferItemDTO transferItemDTO = transferItemMapper.toDto(transferItem);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTransferItemMockMvc.perform(put("/api/transfer-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transferItemDTO)))
            .andExpect(status().isCreated());

        // Validate the TransferItem in the database
        List<TransferItem> transferItemList = transferItemRepository.findAll();
        assertThat(transferItemList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTransferItem() throws Exception {
        // Initialize the database
        transferItemRepository.saveAndFlush(transferItem);
        transferItemSearchRepository.save(transferItem);
        int databaseSizeBeforeDelete = transferItemRepository.findAll().size();

        // Get the transferItem
        restTransferItemMockMvc.perform(delete("/api/transfer-items/{id}", transferItem.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean transferItemExistsInEs = transferItemSearchRepository.exists(transferItem.getId());
        assertThat(transferItemExistsInEs).isFalse();

        // Validate the database is empty
        List<TransferItem> transferItemList = transferItemRepository.findAll();
        assertThat(transferItemList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchTransferItem() throws Exception {
        // Initialize the database
        transferItemRepository.saveAndFlush(transferItem);
        transferItemSearchRepository.save(transferItem);

        // Search the transferItem
        restTransferItemMockMvc.perform(get("/api/_search/transfer-items?query=id:" + transferItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transferItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.intValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TransferItem.class);
        TransferItem transferItem1 = new TransferItem();
        transferItem1.setId(1L);
        TransferItem transferItem2 = new TransferItem();
        transferItem2.setId(transferItem1.getId());
        assertThat(transferItem1).isEqualTo(transferItem2);
        transferItem2.setId(2L);
        assertThat(transferItem1).isNotEqualTo(transferItem2);
        transferItem1.setId(null);
        assertThat(transferItem1).isNotEqualTo(transferItem2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TransferItemDTO.class);
        TransferItemDTO transferItemDTO1 = new TransferItemDTO();
        transferItemDTO1.setId(1L);
        TransferItemDTO transferItemDTO2 = new TransferItemDTO();
        assertThat(transferItemDTO1).isNotEqualTo(transferItemDTO2);
        transferItemDTO2.setId(transferItemDTO1.getId());
        assertThat(transferItemDTO1).isEqualTo(transferItemDTO2);
        transferItemDTO2.setId(2L);
        assertThat(transferItemDTO1).isNotEqualTo(transferItemDTO2);
        transferItemDTO1.setId(null);
        assertThat(transferItemDTO1).isNotEqualTo(transferItemDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(transferItemMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(transferItemMapper.fromId(null)).isNull();
    }
}
