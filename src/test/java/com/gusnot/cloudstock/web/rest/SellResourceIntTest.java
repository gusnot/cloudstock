package com.gusnot.cloudstock.web.rest;

import com.gusnot.cloudstock.CloudstockApp;

import com.gusnot.cloudstock.domain.Sell;
import com.gusnot.cloudstock.repository.SellRepository;
import com.gusnot.cloudstock.service.SellService;
import com.gusnot.cloudstock.repository.search.SellSearchRepository;
import com.gusnot.cloudstock.service.dto.SellDTO;
import com.gusnot.cloudstock.service.mapper.SellMapper;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.gusnot.cloudstock.domain.enumeration.SellStatus;
/**
 * Test class for the SellResource REST controller.
 *
 * @see SellResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CloudstockApp.class)
public class SellResourceIntTest {

    private static final String DEFAULT_REF_NO = "AAAAAAAAAA";
    private static final String UPDATED_REF_NO = "BBBBBBBBBB";

    private static final SellStatus DEFAULT_STATUS = SellStatus.OPENED;
    private static final SellStatus UPDATED_STATUS = SellStatus.CLOSED;

    @Autowired
    private SellRepository sellRepository;

    @Autowired
    private SellMapper sellMapper;

    @Autowired
    private SellService sellService;

    @Autowired
    private SellSearchRepository sellSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSellMockMvc;

    private Sell sell;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SellResource sellResource = new SellResource(sellService);
        this.restSellMockMvc = MockMvcBuilders.standaloneSetup(sellResource)
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
    public static Sell createEntity(EntityManager em) {
        Sell sell = new Sell()
            .refNo(DEFAULT_REF_NO)
            .status(DEFAULT_STATUS);
        return sell;
    }

    @Before
    public void initTest() {
        sellSearchRepository.deleteAll();
        sell = createEntity(em);
    }

    @Test
    @Transactional
    public void createSell() throws Exception {
        int databaseSizeBeforeCreate = sellRepository.findAll().size();

        // Create the Sell
        SellDTO sellDTO = sellMapper.toDto(sell);
        restSellMockMvc.perform(post("/api/sells")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sellDTO)))
            .andExpect(status().isCreated());

        // Validate the Sell in the database
        List<Sell> sellList = sellRepository.findAll();
        assertThat(sellList).hasSize(databaseSizeBeforeCreate + 1);
        Sell testSell = sellList.get(sellList.size() - 1);
        assertThat(testSell.getRefNo()).isEqualTo(DEFAULT_REF_NO);
        assertThat(testSell.getStatus()).isEqualTo(DEFAULT_STATUS);

        // Validate the Sell in Elasticsearch
        Sell sellEs = sellSearchRepository.findOne(testSell.getId());
        assertThat(sellEs).isEqualToComparingFieldByField(testSell);
    }

    @Test
    @Transactional
    public void createSellWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = sellRepository.findAll().size();

        // Create the Sell with an existing ID
        sell.setId(1L);
        SellDTO sellDTO = sellMapper.toDto(sell);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSellMockMvc.perform(post("/api/sells")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sellDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Sell in the database
        List<Sell> sellList = sellRepository.findAll();
        assertThat(sellList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkRefNoIsRequired() throws Exception {
        int databaseSizeBeforeTest = sellRepository.findAll().size();
        // set the field null
        sell.setRefNo(null);

        // Create the Sell, which fails.
        SellDTO sellDTO = sellMapper.toDto(sell);

        restSellMockMvc.perform(post("/api/sells")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sellDTO)))
            .andExpect(status().isBadRequest());

        List<Sell> sellList = sellRepository.findAll();
        assertThat(sellList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = sellRepository.findAll().size();
        // set the field null
        sell.setStatus(null);

        // Create the Sell, which fails.
        SellDTO sellDTO = sellMapper.toDto(sell);

        restSellMockMvc.perform(post("/api/sells")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sellDTO)))
            .andExpect(status().isBadRequest());

        List<Sell> sellList = sellRepository.findAll();
        assertThat(sellList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSells() throws Exception {
        // Initialize the database
        sellRepository.saveAndFlush(sell);

        // Get all the sellList
        restSellMockMvc.perform(get("/api/sells?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sell.getId().intValue())))
            .andExpect(jsonPath("$.[*].refNo").value(hasItem(DEFAULT_REF_NO.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void getSell() throws Exception {
        // Initialize the database
        sellRepository.saveAndFlush(sell);

        // Get the sell
        restSellMockMvc.perform(get("/api/sells/{id}", sell.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(sell.getId().intValue()))
            .andExpect(jsonPath("$.refNo").value(DEFAULT_REF_NO.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSell() throws Exception {
        // Get the sell
        restSellMockMvc.perform(get("/api/sells/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSell() throws Exception {
        // Initialize the database
        sellRepository.saveAndFlush(sell);
        sellSearchRepository.save(sell);
        int databaseSizeBeforeUpdate = sellRepository.findAll().size();

        // Update the sell
        Sell updatedSell = sellRepository.findOne(sell.getId());
        updatedSell
            .refNo(UPDATED_REF_NO)
            .status(UPDATED_STATUS);
        SellDTO sellDTO = sellMapper.toDto(updatedSell);

        restSellMockMvc.perform(put("/api/sells")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sellDTO)))
            .andExpect(status().isOk());

        // Validate the Sell in the database
        List<Sell> sellList = sellRepository.findAll();
        assertThat(sellList).hasSize(databaseSizeBeforeUpdate);
        Sell testSell = sellList.get(sellList.size() - 1);
        assertThat(testSell.getRefNo()).isEqualTo(UPDATED_REF_NO);
        assertThat(testSell.getStatus()).isEqualTo(UPDATED_STATUS);

        // Validate the Sell in Elasticsearch
        Sell sellEs = sellSearchRepository.findOne(testSell.getId());
        assertThat(sellEs).isEqualToComparingFieldByField(testSell);
    }

    @Test
    @Transactional
    public void updateNonExistingSell() throws Exception {
        int databaseSizeBeforeUpdate = sellRepository.findAll().size();

        // Create the Sell
        SellDTO sellDTO = sellMapper.toDto(sell);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSellMockMvc.perform(put("/api/sells")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sellDTO)))
            .andExpect(status().isCreated());

        // Validate the Sell in the database
        List<Sell> sellList = sellRepository.findAll();
        assertThat(sellList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteSell() throws Exception {
        // Initialize the database
        sellRepository.saveAndFlush(sell);
        sellSearchRepository.save(sell);
        int databaseSizeBeforeDelete = sellRepository.findAll().size();

        // Get the sell
        restSellMockMvc.perform(delete("/api/sells/{id}", sell.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean sellExistsInEs = sellSearchRepository.exists(sell.getId());
        assertThat(sellExistsInEs).isFalse();

        // Validate the database is empty
        List<Sell> sellList = sellRepository.findAll();
        assertThat(sellList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchSell() throws Exception {
        // Initialize the database
        sellRepository.saveAndFlush(sell);
        sellSearchRepository.save(sell);

        // Search the sell
        restSellMockMvc.perform(get("/api/_search/sells?query=id:" + sell.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sell.getId().intValue())))
            .andExpect(jsonPath("$.[*].refNo").value(hasItem(DEFAULT_REF_NO.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Sell.class);
        Sell sell1 = new Sell();
        sell1.setId(1L);
        Sell sell2 = new Sell();
        sell2.setId(sell1.getId());
        assertThat(sell1).isEqualTo(sell2);
        sell2.setId(2L);
        assertThat(sell1).isNotEqualTo(sell2);
        sell1.setId(null);
        assertThat(sell1).isNotEqualTo(sell2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SellDTO.class);
        SellDTO sellDTO1 = new SellDTO();
        sellDTO1.setId(1L);
        SellDTO sellDTO2 = new SellDTO();
        assertThat(sellDTO1).isNotEqualTo(sellDTO2);
        sellDTO2.setId(sellDTO1.getId());
        assertThat(sellDTO1).isEqualTo(sellDTO2);
        sellDTO2.setId(2L);
        assertThat(sellDTO1).isNotEqualTo(sellDTO2);
        sellDTO1.setId(null);
        assertThat(sellDTO1).isNotEqualTo(sellDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(sellMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(sellMapper.fromId(null)).isNull();
    }
}
