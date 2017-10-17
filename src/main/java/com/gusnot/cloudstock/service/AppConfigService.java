package com.gusnot.cloudstock.service;

import com.gusnot.cloudstock.service.dto.AppConfigDTO;
import java.util.List;

/**
 * Service Interface for managing AppConfig.
 */
public interface AppConfigService {

    /**
     * Save a appConfig.
     *
     * @param appConfigDTO the entity to save
     * @return the persisted entity
     */
    AppConfigDTO save(AppConfigDTO appConfigDTO);

    /**
     *  Get all the appConfigs.
     *
     *  @return the list of entities
     */
    List<AppConfigDTO> findAll();

    /**
     *  Get the "id" appConfig.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    AppConfigDTO findOne(Long id);

    /**
     *  Delete the "id" appConfig.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the appConfig corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @return the list of entities
     */
    List<AppConfigDTO> search(String query);
}
