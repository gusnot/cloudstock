package com.gusnot.cloudstock.web.rest;

import com.gusnot.cloudstock.CloudstockApp;

import com.gusnot.cloudstock.domain.AttributeItem;
import com.gusnot.cloudstock.repository.AttributeItemRepository;
import com.gusnot.cloudstock.service.AttributeItemService;
import com.gusnot.cloudstock.repository.search.AttributeItemSearchRepository;
import com.gusnot.cloudstock.service.dto.AttributeItemDTO;
import com.gusnot.cloudstock.service.mapper.AttributeItemMapper;
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

/**
 * Test class for the AttributeItemResource REST controller.
 *
 * @see AttributeItemResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CloudstockApp.class)
public class AttributeItemResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PREFIX = "AAA";
    private static final String UPDATED_PREFIX = "BBB";

    @Autowired
    private AttributeItemRepository attributeItemRepository;

    @Autowired
    private AttributeItemMapper attributeItemMapper;

    @Autowired
    private AttributeItemService attributeItemService;

    @Autowired
    private AttributeItemSearchRepository attributeItemSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restAttributeItemMockMvc;

    private AttributeItem attributeItem;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AttributeItemResource attributeItemResource = new AttributeItemResource(attributeItemService);
        this.restAttributeItemMockMvc = MockMvcBuilders.standaloneSetup(attributeItemResource)
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
    public static AttributeItem createEntity(EntityManager em) {
        AttributeItem attributeItem = new AttributeItem()
            .name(DEFAULT_NAME)
            .prefix(DEFAULT_PREFIX);
        return attributeItem;
    }

    @Before
    public void initTest() {
        attributeItemSearchRepository.deleteAll();
        attributeItem = createEntity(em);
    }

    @Test
    @Transactional
    public void createAttributeItem() throws Exception {
        int databaseSizeBeforeCreate = attributeItemRepository.findAll().size();

        // Create the AttributeItem
        AttributeItemDTO attributeItemDTO = attributeItemMapper.toDto(attributeItem);
        restAttributeItemMockMvc.perform(post("/api/attribute-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(attributeItemDTO)))
            .andExpect(status().isCreated());

        // Validate the AttributeItem in the database
        List<AttributeItem> attributeItemList = attributeItemRepository.findAll();
        assertThat(attributeItemList).hasSize(databaseSizeBeforeCreate + 1);
        AttributeItem testAttributeItem = attributeItemList.get(attributeItemList.size() - 1);
        assertThat(testAttributeItem.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testAttributeItem.getPrefix()).isEqualTo(DEFAULT_PREFIX);

        // Validate the AttributeItem in Elasticsearch
        AttributeItem attributeItemEs = attributeItemSearchRepository.findOne(testAttributeItem.getId());
        assertThat(attributeItemEs).isEqualToComparingFieldByField(testAttributeItem);
    }

    @Test
    @Transactional
    public void createAttributeItemWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = attributeItemRepository.findAll().size();

        // Create the AttributeItem with an existing ID
        attributeItem.setId(1L);
        AttributeItemDTO attributeItemDTO = attributeItemMapper.toDto(attributeItem);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAttributeItemMockMvc.perform(post("/api/attribute-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(attributeItemDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AttributeItem in the database
        List<AttributeItem> attributeItemList = attributeItemRepository.findAll();
        assertThat(attributeItemList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = attributeItemRepository.findAll().size();
        // set the field null
        attributeItem.setName(null);

        // Create the AttributeItem, which fails.
        AttributeItemDTO attributeItemDTO = attributeItemMapper.toDto(attributeItem);

        restAttributeItemMockMvc.perform(post("/api/attribute-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(attributeItemDTO)))
            .andExpect(status().isBadRequest());

        List<AttributeItem> attributeItemList = attributeItemRepository.findAll();
        assertThat(attributeItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPrefixIsRequired() throws Exception {
        int databaseSizeBeforeTest = attributeItemRepository.findAll().size();
        // set the field null
        attributeItem.setPrefix(null);

        // Create the AttributeItem, which fails.
        AttributeItemDTO attributeItemDTO = attributeItemMapper.toDto(attributeItem);

        restAttributeItemMockMvc.perform(post("/api/attribute-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(attributeItemDTO)))
            .andExpect(status().isBadRequest());

        List<AttributeItem> attributeItemList = attributeItemRepository.findAll();
        assertThat(attributeItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAttributeItems() throws Exception {
        // Initialize the database
        attributeItemRepository.saveAndFlush(attributeItem);

        // Get all the attributeItemList
        restAttributeItemMockMvc.perform(get("/api/attribute-items?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(attributeItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].prefix").value(hasItem(DEFAULT_PREFIX.toString())));
    }

    @Test
    @Transactional
    public void getAttributeItem() throws Exception {
        // Initialize the database
        attributeItemRepository.saveAndFlush(attributeItem);

        // Get the attributeItem
        restAttributeItemMockMvc.perform(get("/api/attribute-items/{id}", attributeItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(attributeItem.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.prefix").value(DEFAULT_PREFIX.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAttributeItem() throws Exception {
        // Get the attributeItem
        restAttributeItemMockMvc.perform(get("/api/attribute-items/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAttributeItem() throws Exception {
        // Initialize the database
        attributeItemRepository.saveAndFlush(attributeItem);
        attributeItemSearchRepository.save(attributeItem);
        int databaseSizeBeforeUpdate = attributeItemRepository.findAll().size();

        // Update the attributeItem
        AttributeItem updatedAttributeItem = attributeItemRepository.findOne(attributeItem.getId());
        updatedAttributeItem
            .name(UPDATED_NAME)
            .prefix(UPDATED_PREFIX);
        AttributeItemDTO attributeItemDTO = attributeItemMapper.toDto(updatedAttributeItem);

        restAttributeItemMockMvc.perform(put("/api/attribute-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(attributeItemDTO)))
            .andExpect(status().isOk());

        // Validate the AttributeItem in the database
        List<AttributeItem> attributeItemList = attributeItemRepository.findAll();
        assertThat(attributeItemList).hasSize(databaseSizeBeforeUpdate);
        AttributeItem testAttributeItem = attributeItemList.get(attributeItemList.size() - 1);
        assertThat(testAttributeItem.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testAttributeItem.getPrefix()).isEqualTo(UPDATED_PREFIX);

        // Validate the AttributeItem in Elasticsearch
        AttributeItem attributeItemEs = attributeItemSearchRepository.findOne(testAttributeItem.getId());
        assertThat(attributeItemEs).isEqualToComparingFieldByField(testAttributeItem);
    }

    @Test
    @Transactional
    public void updateNonExistingAttributeItem() throws Exception {
        int databaseSizeBeforeUpdate = attributeItemRepository.findAll().size();

        // Create the AttributeItem
        AttributeItemDTO attributeItemDTO = attributeItemMapper.toDto(attributeItem);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restAttributeItemMockMvc.perform(put("/api/attribute-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(attributeItemDTO)))
            .andExpect(status().isCreated());

        // Validate the AttributeItem in the database
        List<AttributeItem> attributeItemList = attributeItemRepository.findAll();
        assertThat(attributeItemList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteAttributeItem() throws Exception {
        // Initialize the database
        attributeItemRepository.saveAndFlush(attributeItem);
        attributeItemSearchRepository.save(attributeItem);
        int databaseSizeBeforeDelete = attributeItemRepository.findAll().size();

        // Get the attributeItem
        restAttributeItemMockMvc.perform(delete("/api/attribute-items/{id}", attributeItem.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean attributeItemExistsInEs = attributeItemSearchRepository.exists(attributeItem.getId());
        assertThat(attributeItemExistsInEs).isFalse();

        // Validate the database is empty
        List<AttributeItem> attributeItemList = attributeItemRepository.findAll();
        assertThat(attributeItemList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchAttributeItem() throws Exception {
        // Initialize the database
        attributeItemRepository.saveAndFlush(attributeItem);
        attributeItemSearchRepository.save(attributeItem);

        // Search the attributeItem
        restAttributeItemMockMvc.perform(get("/api/_search/attribute-items?query=id:" + attributeItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(attributeItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].prefix").value(hasItem(DEFAULT_PREFIX.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AttributeItem.class);
        AttributeItem attributeItem1 = new AttributeItem();
        attributeItem1.setId(1L);
        AttributeItem attributeItem2 = new AttributeItem();
        attributeItem2.setId(attributeItem1.getId());
        assertThat(attributeItem1).isEqualTo(attributeItem2);
        attributeItem2.setId(2L);
        assertThat(attributeItem1).isNotEqualTo(attributeItem2);
        attributeItem1.setId(null);
        assertThat(attributeItem1).isNotEqualTo(attributeItem2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AttributeItemDTO.class);
        AttributeItemDTO attributeItemDTO1 = new AttributeItemDTO();
        attributeItemDTO1.setId(1L);
        AttributeItemDTO attributeItemDTO2 = new AttributeItemDTO();
        assertThat(attributeItemDTO1).isNotEqualTo(attributeItemDTO2);
        attributeItemDTO2.setId(attributeItemDTO1.getId());
        assertThat(attributeItemDTO1).isEqualTo(attributeItemDTO2);
        attributeItemDTO2.setId(2L);
        assertThat(attributeItemDTO1).isNotEqualTo(attributeItemDTO2);
        attributeItemDTO1.setId(null);
        assertThat(attributeItemDTO1).isNotEqualTo(attributeItemDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(attributeItemMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(attributeItemMapper.fromId(null)).isNull();
    }
}
