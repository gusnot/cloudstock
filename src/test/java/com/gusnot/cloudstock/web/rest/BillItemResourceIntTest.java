package com.gusnot.cloudstock.web.rest;

import com.gusnot.cloudstock.CloudstockApp;

import com.gusnot.cloudstock.domain.BillItem;
import com.gusnot.cloudstock.repository.BillItemRepository;
import com.gusnot.cloudstock.service.BillItemService;
import com.gusnot.cloudstock.repository.search.BillItemSearchRepository;
import com.gusnot.cloudstock.service.dto.BillItemDTO;
import com.gusnot.cloudstock.service.mapper.BillItemMapper;
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
 * Test class for the BillItemResource REST controller.
 *
 * @see BillItemResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CloudstockApp.class)
public class BillItemResourceIntTest {

    private static final BigDecimal DEFAULT_SELL_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_SELL_PRICE = new BigDecimal(2);

    private static final BigDecimal DEFAULT_SELL_COST = new BigDecimal(1);
    private static final BigDecimal UPDATED_SELL_COST = new BigDecimal(2);

    @Autowired
    private BillItemRepository billItemRepository;

    @Autowired
    private BillItemMapper billItemMapper;

    @Autowired
    private BillItemService billItemService;

    @Autowired
    private BillItemSearchRepository billItemSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restBillItemMockMvc;

    private BillItem billItem;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final BillItemResource billItemResource = new BillItemResource(billItemService);
        this.restBillItemMockMvc = MockMvcBuilders.standaloneSetup(billItemResource)
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
    public static BillItem createEntity(EntityManager em) {
        BillItem billItem = new BillItem()
            .sellPrice(DEFAULT_SELL_PRICE)
            .sellCost(DEFAULT_SELL_COST);
        return billItem;
    }

    @Before
    public void initTest() {
        billItemSearchRepository.deleteAll();
        billItem = createEntity(em);
    }

    @Test
    @Transactional
    public void createBillItem() throws Exception {
        int databaseSizeBeforeCreate = billItemRepository.findAll().size();

        // Create the BillItem
        BillItemDTO billItemDTO = billItemMapper.toDto(billItem);
        restBillItemMockMvc.perform(post("/api/bill-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(billItemDTO)))
            .andExpect(status().isCreated());

        // Validate the BillItem in the database
        List<BillItem> billItemList = billItemRepository.findAll();
        assertThat(billItemList).hasSize(databaseSizeBeforeCreate + 1);
        BillItem testBillItem = billItemList.get(billItemList.size() - 1);
        assertThat(testBillItem.getSellPrice()).isEqualTo(DEFAULT_SELL_PRICE);
        assertThat(testBillItem.getSellCost()).isEqualTo(DEFAULT_SELL_COST);

        // Validate the BillItem in Elasticsearch
        BillItem billItemEs = billItemSearchRepository.findOne(testBillItem.getId());
        assertThat(billItemEs).isEqualToComparingFieldByField(testBillItem);
    }

    @Test
    @Transactional
    public void createBillItemWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = billItemRepository.findAll().size();

        // Create the BillItem with an existing ID
        billItem.setId(1L);
        BillItemDTO billItemDTO = billItemMapper.toDto(billItem);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBillItemMockMvc.perform(post("/api/bill-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(billItemDTO)))
            .andExpect(status().isBadRequest());

        // Validate the BillItem in the database
        List<BillItem> billItemList = billItemRepository.findAll();
        assertThat(billItemList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkSellPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = billItemRepository.findAll().size();
        // set the field null
        billItem.setSellPrice(null);

        // Create the BillItem, which fails.
        BillItemDTO billItemDTO = billItemMapper.toDto(billItem);

        restBillItemMockMvc.perform(post("/api/bill-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(billItemDTO)))
            .andExpect(status().isBadRequest());

        List<BillItem> billItemList = billItemRepository.findAll();
        assertThat(billItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBillItems() throws Exception {
        // Initialize the database
        billItemRepository.saveAndFlush(billItem);

        // Get all the billItemList
        restBillItemMockMvc.perform(get("/api/bill-items?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(billItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].sellPrice").value(hasItem(DEFAULT_SELL_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].sellCost").value(hasItem(DEFAULT_SELL_COST.intValue())));
    }

    @Test
    @Transactional
    public void getBillItem() throws Exception {
        // Initialize the database
        billItemRepository.saveAndFlush(billItem);

        // Get the billItem
        restBillItemMockMvc.perform(get("/api/bill-items/{id}", billItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(billItem.getId().intValue()))
            .andExpect(jsonPath("$.sellPrice").value(DEFAULT_SELL_PRICE.intValue()))
            .andExpect(jsonPath("$.sellCost").value(DEFAULT_SELL_COST.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingBillItem() throws Exception {
        // Get the billItem
        restBillItemMockMvc.perform(get("/api/bill-items/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBillItem() throws Exception {
        // Initialize the database
        billItemRepository.saveAndFlush(billItem);
        billItemSearchRepository.save(billItem);
        int databaseSizeBeforeUpdate = billItemRepository.findAll().size();

        // Update the billItem
        BillItem updatedBillItem = billItemRepository.findOne(billItem.getId());
        updatedBillItem
            .sellPrice(UPDATED_SELL_PRICE)
            .sellCost(UPDATED_SELL_COST);
        BillItemDTO billItemDTO = billItemMapper.toDto(updatedBillItem);

        restBillItemMockMvc.perform(put("/api/bill-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(billItemDTO)))
            .andExpect(status().isOk());

        // Validate the BillItem in the database
        List<BillItem> billItemList = billItemRepository.findAll();
        assertThat(billItemList).hasSize(databaseSizeBeforeUpdate);
        BillItem testBillItem = billItemList.get(billItemList.size() - 1);
        assertThat(testBillItem.getSellPrice()).isEqualTo(UPDATED_SELL_PRICE);
        assertThat(testBillItem.getSellCost()).isEqualTo(UPDATED_SELL_COST);

        // Validate the BillItem in Elasticsearch
        BillItem billItemEs = billItemSearchRepository.findOne(testBillItem.getId());
        assertThat(billItemEs).isEqualToComparingFieldByField(testBillItem);
    }

    @Test
    @Transactional
    public void updateNonExistingBillItem() throws Exception {
        int databaseSizeBeforeUpdate = billItemRepository.findAll().size();

        // Create the BillItem
        BillItemDTO billItemDTO = billItemMapper.toDto(billItem);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restBillItemMockMvc.perform(put("/api/bill-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(billItemDTO)))
            .andExpect(status().isCreated());

        // Validate the BillItem in the database
        List<BillItem> billItemList = billItemRepository.findAll();
        assertThat(billItemList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteBillItem() throws Exception {
        // Initialize the database
        billItemRepository.saveAndFlush(billItem);
        billItemSearchRepository.save(billItem);
        int databaseSizeBeforeDelete = billItemRepository.findAll().size();

        // Get the billItem
        restBillItemMockMvc.perform(delete("/api/bill-items/{id}", billItem.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean billItemExistsInEs = billItemSearchRepository.exists(billItem.getId());
        assertThat(billItemExistsInEs).isFalse();

        // Validate the database is empty
        List<BillItem> billItemList = billItemRepository.findAll();
        assertThat(billItemList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchBillItem() throws Exception {
        // Initialize the database
        billItemRepository.saveAndFlush(billItem);
        billItemSearchRepository.save(billItem);

        // Search the billItem
        restBillItemMockMvc.perform(get("/api/_search/bill-items?query=id:" + billItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(billItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].sellPrice").value(hasItem(DEFAULT_SELL_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].sellCost").value(hasItem(DEFAULT_SELL_COST.intValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BillItem.class);
        BillItem billItem1 = new BillItem();
        billItem1.setId(1L);
        BillItem billItem2 = new BillItem();
        billItem2.setId(billItem1.getId());
        assertThat(billItem1).isEqualTo(billItem2);
        billItem2.setId(2L);
        assertThat(billItem1).isNotEqualTo(billItem2);
        billItem1.setId(null);
        assertThat(billItem1).isNotEqualTo(billItem2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BillItemDTO.class);
        BillItemDTO billItemDTO1 = new BillItemDTO();
        billItemDTO1.setId(1L);
        BillItemDTO billItemDTO2 = new BillItemDTO();
        assertThat(billItemDTO1).isNotEqualTo(billItemDTO2);
        billItemDTO2.setId(billItemDTO1.getId());
        assertThat(billItemDTO1).isEqualTo(billItemDTO2);
        billItemDTO2.setId(2L);
        assertThat(billItemDTO1).isNotEqualTo(billItemDTO2);
        billItemDTO1.setId(null);
        assertThat(billItemDTO1).isNotEqualTo(billItemDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(billItemMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(billItemMapper.fromId(null)).isNull();
    }
}
