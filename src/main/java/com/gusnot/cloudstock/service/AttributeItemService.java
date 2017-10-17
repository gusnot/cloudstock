package com.gusnot.cloudstock.service;

import com.gusnot.cloudstock.service.dto.AttributeItemDTO;
import java.util.List;

/**
 * Service Interface for managing AttributeItem.
 */
public interface AttributeItemService {

    /**
     * Save a attributeItem.
     *
     * @param attributeItemDTO the entity to save
     * @return the persisted entity
     */
    AttributeItemDTO save(AttributeItemDTO attributeItemDTO);

    /**
     *  Get all the attributeItems.
     *
     *  @return the list of entities
     */
    List<AttributeItemDTO> findAll();

    /**
     *  Get the "id" attributeItem.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    AttributeItemDTO findOne(Long id);

    /**
     *  Delete the "id" attributeItem.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the attributeItem corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @return the list of entities
     */
    List<AttributeItemDTO> search(String query);
}
