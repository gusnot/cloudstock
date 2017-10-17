package com.gusnot.cloudstock.service.impl;

import com.gusnot.cloudstock.service.AppConfigService;
import com.gusnot.cloudstock.domain.AppConfig;
import com.gusnot.cloudstock.repository.AppConfigRepository;
import com.gusnot.cloudstock.repository.search.AppConfigSearchRepository;
import com.gusnot.cloudstock.service.dto.AppConfigDTO;
import com.gusnot.cloudstock.service.mapper.AppConfigMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing AppConfig.
 */
@Service
@Transactional
public class AppConfigServiceImpl implements AppConfigService{

    private final Logger log = LoggerFactory.getLogger(AppConfigServiceImpl.class);

    private final AppConfigRepository appConfigRepository;

    private final AppConfigMapper appConfigMapper;

    private final AppConfigSearchRepository appConfigSearchRepository;

    public AppConfigServiceImpl(AppConfigRepository appConfigRepository, AppConfigMapper appConfigMapper, AppConfigSearchRepository appConfigSearchRepository) {
        this.appConfigRepository = appConfigRepository;
        this.appConfigMapper = appConfigMapper;
        this.appConfigSearchRepository = appConfigSearchRepository;
    }

    /**
     * Save a appConfig.
     *
     * @param appConfigDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public AppConfigDTO save(AppConfigDTO appConfigDTO) {
        log.debug("Request to save AppConfig : {}", appConfigDTO);
        AppConfig appConfig = appConfigMapper.toEntity(appConfigDTO);
        appConfig = appConfigRepository.save(appConfig);
        AppConfigDTO result = appConfigMapper.toDto(appConfig);
        appConfigSearchRepository.save(appConfig);
        return result;
    }

    /**
     *  Get all the appConfigs.
     *
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<AppConfigDTO> findAll() {
        log.debug("Request to get all AppConfigs");
        return appConfigRepository.findAll().stream()
            .map(appConfigMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get one appConfig by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public AppConfigDTO findOne(Long id) {
        log.debug("Request to get AppConfig : {}", id);
        AppConfig appConfig = appConfigRepository.findOne(id);
        return appConfigMapper.toDto(appConfig);
    }

    /**
     *  Delete the  appConfig by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete AppConfig : {}", id);
        appConfigRepository.delete(id);
        appConfigSearchRepository.delete(id);
    }

    /**
     * Search for the appConfig corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<AppConfigDTO> search(String query) {
        log.debug("Request to search AppConfigs for query {}", query);
        return StreamSupport
            .stream(appConfigSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(appConfigMapper::toDto)
            .collect(Collectors.toList());
    }
}
