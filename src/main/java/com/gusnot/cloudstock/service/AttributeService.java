package com.gusnot.cloudstock.service;

import com.gusnot.cloudstock.service.dto.AttributeDTO;
import java.util.List;

/**
 * Service Interface for managing Attribute.
 */
public interface AttributeService {

    /**
     * Save a attribute.
     *
     * @param attributeDTO the entity to save
     * @return the persisted entity
     */
    AttributeDTO save(AttributeDTO attributeDTO);

    /**
     *  Get all the attributes.
     *
     *  @return the list of entities
     */
    List<AttributeDTO> findAll();

    /**
     *  Get the "id" attribute.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    AttributeDTO findOne(Long id);

    /**
     *  Delete the "id" attribute.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the attribute corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @return the list of entities
     */
    List<AttributeDTO> search(String query);
}
