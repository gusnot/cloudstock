package com.gusnot.cloudstock.web.rest;

import com.gusnot.cloudstock.CloudstockApp;

import com.gusnot.cloudstock.domain.Sku;
import com.gusnot.cloudstock.repository.SkuRepository;
import com.gusnot.cloudstock.service.SkuService;
import com.gusnot.cloudstock.repository.search.SkuSearchRepository;
import com.gusnot.cloudstock.service.dto.SkuDTO;
import com.gusnot.cloudstock.service.mapper.SkuMapper;
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
 * Test class for the SkuResource REST controller.
 *
 * @see SkuResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CloudstockApp.class)
public class SkuResourceIntTest {

    private static final BigDecimal DEFAULT_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRICE = new BigDecimal(2);

    private static final BigDecimal DEFAULT_COST = new BigDecimal(1);
    private static final BigDecimal UPDATED_COST = new BigDecimal(2);

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_BARCODE = "AAAAAAAAAA";
    private static final String UPDATED_BARCODE = "BBBBBBBBBB";

    @Autowired
    private SkuRepository skuRepository;

    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private SkuService skuService;

    @Autowired
    private SkuSearchRepository skuSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSkuMockMvc;

    private Sku sku;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SkuResource skuResource = new SkuResource(skuService);
        this.restSkuMockMvc = MockMvcBuilders.standaloneSetup(skuResource)
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
    public static Sku createEntity(EntityManager em) {
        Sku sku = new Sku()
            .price(DEFAULT_PRICE)
            .cost(DEFAULT_COST)
            .code(DEFAULT_CODE)
            .barcode(DEFAULT_BARCODE);
        return sku;
    }

    @Before
    public void initTest() {
        skuSearchRepository.deleteAll();
        sku = createEntity(em);
    }

    @Test
    @Transactional
    public void createSku() throws Exception {
        int databaseSizeBeforeCreate = skuRepository.findAll().size();

        // Create the Sku
        SkuDTO skuDTO = skuMapper.toDto(sku);
        restSkuMockMvc.perform(post("/api/skus")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(skuDTO)))
            .andExpect(status().isCreated());

        // Validate the Sku in the database
        List<Sku> skuList = skuRepository.findAll();
        assertThat(skuList).hasSize(databaseSizeBeforeCreate + 1);
        Sku testSku = skuList.get(skuList.size() - 1);
        assertThat(testSku.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testSku.getCost()).isEqualTo(DEFAULT_COST);
        assertThat(testSku.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testSku.getBarcode()).isEqualTo(DEFAULT_BARCODE);

        // Validate the Sku in Elasticsearch
        Sku skuEs = skuSearchRepository.findOne(testSku.getId());
        assertThat(skuEs).isEqualToComparingFieldByField(testSku);
    }

    @Test
    @Transactional
    public void createSkuWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = skuRepository.findAll().size();

        // Create the Sku with an existing ID
        sku.setId(1L);
        SkuDTO skuDTO = skuMapper.toDto(sku);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSkuMockMvc.perform(post("/api/skus")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(skuDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Sku in the database
        List<Sku> skuList = skuRepository.findAll();
        assertThat(skuList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = skuRepository.findAll().size();
        // set the field null
        sku.setPrice(null);

        // Create the Sku, which fails.
        SkuDTO skuDTO = skuMapper.toDto(sku);

        restSkuMockMvc.perform(post("/api/skus")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(skuDTO)))
            .andExpect(status().isBadRequest());

        List<Sku> skuList = skuRepository.findAll();
        assertThat(skuList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = skuRepository.findAll().size();
        // set the field null
        sku.setCode(null);

        // Create the Sku, which fails.
        SkuDTO skuDTO = skuMapper.toDto(sku);

        restSkuMockMvc.perform(post("/api/skus")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(skuDTO)))
            .andExpect(status().isBadRequest());

        List<Sku> skuList = skuRepository.findAll();
        assertThat(skuList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSkus() throws Exception {
        // Initialize the database
        skuRepository.saveAndFlush(sku);

        // Get all the skuList
        restSkuMockMvc.perform(get("/api/skus?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sku.getId().intValue())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].cost").value(hasItem(DEFAULT_COST.intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].barcode").value(hasItem(DEFAULT_BARCODE.toString())));
    }

    @Test
    @Transactional
    public void getSku() throws Exception {
        // Initialize the database
        skuRepository.saveAndFlush(sku);

        // Get the sku
        restSkuMockMvc.perform(get("/api/skus/{id}", sku.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(sku.getId().intValue()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.intValue()))
            .andExpect(jsonPath("$.cost").value(DEFAULT_COST.intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.barcode").value(DEFAULT_BARCODE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSku() throws Exception {
        // Get the sku
        restSkuMockMvc.perform(get("/api/skus/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSku() throws Exception {
        // Initialize the database
        skuRepository.saveAndFlush(sku);
        skuSearchRepository.save(sku);
        int databaseSizeBeforeUpdate = skuRepository.findAll().size();

        // Update the sku
        Sku updatedSku = skuRepository.findOne(sku.getId());
        updatedSku
            .price(UPDATED_PRICE)
            .cost(UPDATED_COST)
            .code(UPDATED_CODE)
            .barcode(UPDATED_BARCODE);
        SkuDTO skuDTO = skuMapper.toDto(updatedSku);

        restSkuMockMvc.perform(put("/api/skus")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(skuDTO)))
            .andExpect(status().isOk());

        // Validate the Sku in the database
        List<Sku> skuList = skuRepository.findAll();
        assertThat(skuList).hasSize(databaseSizeBeforeUpdate);
        Sku testSku = skuList.get(skuList.size() - 1);
        assertThat(testSku.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testSku.getCost()).isEqualTo(UPDATED_COST);
        assertThat(testSku.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testSku.getBarcode()).isEqualTo(UPDATED_BARCODE);

        // Validate the Sku in Elasticsearch
        Sku skuEs = skuSearchRepository.findOne(testSku.getId());
        assertThat(skuEs).isEqualToComparingFieldByField(testSku);
    }

    @Test
    @Transactional
    public void updateNonExistingSku() throws Exception {
        int databaseSizeBeforeUpdate = skuRepository.findAll().size();

        // Create the Sku
        SkuDTO skuDTO = skuMapper.toDto(sku);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSkuMockMvc.perform(put("/api/skus")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(skuDTO)))
            .andExpect(status().isCreated());

        // Validate the Sku in the database
        List<Sku> skuList = skuRepository.findAll();
        assertThat(skuList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteSku() throws Exception {
        // Initialize the database
        skuRepository.saveAndFlush(sku);
        skuSearchRepository.save(sku);
        int databaseSizeBeforeDelete = skuRepository.findAll().size();

        // Get the sku
        restSkuMockMvc.perform(delete("/api/skus/{id}", sku.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean skuExistsInEs = skuSearchRepository.exists(sku.getId());
        assertThat(skuExistsInEs).isFalse();

        // Validate the database is empty
        List<Sku> skuList = skuRepository.findAll();
        assertThat(skuList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchSku() throws Exception {
        // Initialize the database
        skuRepository.saveAndFlush(sku);
        skuSearchRepository.save(sku);

        // Search the sku
        restSkuMockMvc.perform(get("/api/_search/skus?query=id:" + sku.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sku.getId().intValue())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].cost").value(hasItem(DEFAULT_COST.intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].barcode").value(hasItem(DEFAULT_BARCODE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Sku.class);
        Sku sku1 = new Sku();
        sku1.setId(1L);
        Sku sku2 = new Sku();
        sku2.setId(sku1.getId());
        assertThat(sku1).isEqualTo(sku2);
        sku2.setId(2L);
        assertThat(sku1).isNotEqualTo(sku2);
        sku1.setId(null);
        assertThat(sku1).isNotEqualTo(sku2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SkuDTO.class);
        SkuDTO skuDTO1 = new SkuDTO();
        skuDTO1.setId(1L);
        SkuDTO skuDTO2 = new SkuDTO();
        assertThat(skuDTO1).isNotEqualTo(skuDTO2);
        skuDTO2.setId(skuDTO1.getId());
        assertThat(skuDTO1).isEqualTo(skuDTO2);
        skuDTO2.setId(2L);
        assertThat(skuDTO1).isNotEqualTo(skuDTO2);
        skuDTO1.setId(null);
        assertThat(skuDTO1).isNotEqualTo(skuDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(skuMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(skuMapper.fromId(null)).isNull();
    }
}
