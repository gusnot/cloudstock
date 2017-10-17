package com.gusnot.cloudstock.web.rest;

import com.gusnot.cloudstock.CloudstockApp;

import com.gusnot.cloudstock.domain.AppConfig;
import com.gusnot.cloudstock.repository.AppConfigRepository;
import com.gusnot.cloudstock.service.AppConfigService;
import com.gusnot.cloudstock.repository.search.AppConfigSearchRepository;
import com.gusnot.cloudstock.service.dto.AppConfigDTO;
import com.gusnot.cloudstock.service.mapper.AppConfigMapper;
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
 * Test class for the AppConfigResource REST controller.
 *
 * @see AppConfigResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CloudstockApp.class)
public class AppConfigResourceIntTest {

    private static final String DEFAULT_KEY = "AAAAAAAAAA";
    private static final String UPDATED_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    @Autowired
    private AppConfigRepository appConfigRepository;

    @Autowired
    private AppConfigMapper appConfigMapper;

    @Autowired
    private AppConfigService appConfigService;

    @Autowired
    private AppConfigSearchRepository appConfigSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restAppConfigMockMvc;

    private AppConfig appConfig;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AppConfigResource appConfigResource = new AppConfigResource(appConfigService);
        this.restAppConfigMockMvc = MockMvcBuilders.standaloneSetup(appConfigResource)
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
    public static AppConfig createEntity(EntityManager em) {
        AppConfig appConfig = new AppConfig()
            .key(DEFAULT_KEY)
            .value(DEFAULT_VALUE);
        return appConfig;
    }

    @Before
    public void initTest() {
        appConfigSearchRepository.deleteAll();
        appConfig = createEntity(em);
    }

    @Test
    @Transactional
    public void createAppConfig() throws Exception {
        int databaseSizeBeforeCreate = appConfigRepository.findAll().size();

        // Create the AppConfig
        AppConfigDTO appConfigDTO = appConfigMapper.toDto(appConfig);
        restAppConfigMockMvc.perform(post("/api/app-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(appConfigDTO)))
            .andExpect(status().isCreated());

        // Validate the AppConfig in the database
        List<AppConfig> appConfigList = appConfigRepository.findAll();
        assertThat(appConfigList).hasSize(databaseSizeBeforeCreate + 1);
        AppConfig testAppConfig = appConfigList.get(appConfigList.size() - 1);
        assertThat(testAppConfig.getKey()).isEqualTo(DEFAULT_KEY);
        assertThat(testAppConfig.getValue()).isEqualTo(DEFAULT_VALUE);

        // Validate the AppConfig in Elasticsearch
        AppConfig appConfigEs = appConfigSearchRepository.findOne(testAppConfig.getId());
        assertThat(appConfigEs).isEqualToComparingFieldByField(testAppConfig);
    }

    @Test
    @Transactional
    public void createAppConfigWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = appConfigRepository.findAll().size();

        // Create the AppConfig with an existing ID
        appConfig.setId(1L);
        AppConfigDTO appConfigDTO = appConfigMapper.toDto(appConfig);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAppConfigMockMvc.perform(post("/api/app-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(appConfigDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AppConfig in the database
        List<AppConfig> appConfigList = appConfigRepository.findAll();
        assertThat(appConfigList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkKeyIsRequired() throws Exception {
        int databaseSizeBeforeTest = appConfigRepository.findAll().size();
        // set the field null
        appConfig.setKey(null);

        // Create the AppConfig, which fails.
        AppConfigDTO appConfigDTO = appConfigMapper.toDto(appConfig);

        restAppConfigMockMvc.perform(post("/api/app-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(appConfigDTO)))
            .andExpect(status().isBadRequest());

        List<AppConfig> appConfigList = appConfigRepository.findAll();
        assertThat(appConfigList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAppConfigs() throws Exception {
        // Initialize the database
        appConfigRepository.saveAndFlush(appConfig);

        // Get all the appConfigList
        restAppConfigMockMvc.perform(get("/api/app-configs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(appConfig.getId().intValue())))
            .andExpect(jsonPath("$.[*].key").value(hasItem(DEFAULT_KEY.toString())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.toString())));
    }

    @Test
    @Transactional
    public void getAppConfig() throws Exception {
        // Initialize the database
        appConfigRepository.saveAndFlush(appConfig);

        // Get the appConfig
        restAppConfigMockMvc.perform(get("/api/app-configs/{id}", appConfig.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(appConfig.getId().intValue()))
            .andExpect(jsonPath("$.key").value(DEFAULT_KEY.toString()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAppConfig() throws Exception {
        // Get the appConfig
        restAppConfigMockMvc.perform(get("/api/app-configs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAppConfig() throws Exception {
        // Initialize the database
        appConfigRepository.saveAndFlush(appConfig);
        appConfigSearchRepository.save(appConfig);
        int databaseSizeBeforeUpdate = appConfigRepository.findAll().size();

        // Update the appConfig
        AppConfig updatedAppConfig = appConfigRepository.findOne(appConfig.getId());
        updatedAppConfig
            .key(UPDATED_KEY)
            .value(UPDATED_VALUE);
        AppConfigDTO appConfigDTO = appConfigMapper.toDto(updatedAppConfig);

        restAppConfigMockMvc.perform(put("/api/app-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(appConfigDTO)))
            .andExpect(status().isOk());

        // Validate the AppConfig in the database
        List<AppConfig> appConfigList = appConfigRepository.findAll();
        assertThat(appConfigList).hasSize(databaseSizeBeforeUpdate);
        AppConfig testAppConfig = appConfigList.get(appConfigList.size() - 1);
        assertThat(testAppConfig.getKey()).isEqualTo(UPDATED_KEY);
        assertThat(testAppConfig.getValue()).isEqualTo(UPDATED_VALUE);

        // Validate the AppConfig in Elasticsearch
        AppConfig appConfigEs = appConfigSearchRepository.findOne(testAppConfig.getId());
        assertThat(appConfigEs).isEqualToComparingFieldByField(testAppConfig);
    }

    @Test
    @Transactional
    public void updateNonExistingAppConfig() throws Exception {
        int databaseSizeBeforeUpdate = appConfigRepository.findAll().size();

        // Create the AppConfig
        AppConfigDTO appConfigDTO = appConfigMapper.toDto(appConfig);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restAppConfigMockMvc.perform(put("/api/app-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(appConfigDTO)))
            .andExpect(status().isCreated());

        // Validate the AppConfig in the database
        List<AppConfig> appConfigList = appConfigRepository.findAll();
        assertThat(appConfigList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteAppConfig() throws Exception {
        // Initialize the database
        appConfigRepository.saveAndFlush(appConfig);
        appConfigSearchRepository.save(appConfig);
        int databaseSizeBeforeDelete = appConfigRepository.findAll().size();

        // Get the appConfig
        restAppConfigMockMvc.perform(delete("/api/app-configs/{id}", appConfig.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean appConfigExistsInEs = appConfigSearchRepository.exists(appConfig.getId());
        assertThat(appConfigExistsInEs).isFalse();

        // Validate the database is empty
        List<AppConfig> appConfigList = appConfigRepository.findAll();
        assertThat(appConfigList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchAppConfig() throws Exception {
        // Initialize the database
        appConfigRepository.saveAndFlush(appConfig);
        appConfigSearchRepository.save(appConfig);

        // Search the appConfig
        restAppConfigMockMvc.perform(get("/api/_search/app-configs?query=id:" + appConfig.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(appConfig.getId().intValue())))
            .andExpect(jsonPath("$.[*].key").value(hasItem(DEFAULT_KEY.toString())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AppConfig.class);
        AppConfig appConfig1 = new AppConfig();
        appConfig1.setId(1L);
        AppConfig appConfig2 = new AppConfig();
        appConfig2.setId(appConfig1.getId());
        assertThat(appConfig1).isEqualTo(appConfig2);
        appConfig2.setId(2L);
        assertThat(appConfig1).isNotEqualTo(appConfig2);
        appConfig1.setId(null);
        assertThat(appConfig1).isNotEqualTo(appConfig2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AppConfigDTO.class);
        AppConfigDTO appConfigDTO1 = new AppConfigDTO();
        appConfigDTO1.setId(1L);
        AppConfigDTO appConfigDTO2 = new AppConfigDTO();
        assertThat(appConfigDTO1).isNotEqualTo(appConfigDTO2);
        appConfigDTO2.setId(appConfigDTO1.getId());
        assertThat(appConfigDTO1).isEqualTo(appConfigDTO2);
        appConfigDTO2.setId(2L);
        assertThat(appConfigDTO1).isNotEqualTo(appConfigDTO2);
        appConfigDTO1.setId(null);
        assertThat(appConfigDTO1).isNotEqualTo(appConfigDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(appConfigMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(appConfigMapper.fromId(null)).isNull();
    }
}
