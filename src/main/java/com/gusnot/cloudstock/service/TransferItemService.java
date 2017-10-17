package com.gusnot.cloudstock.service;

import com.gusnot.cloudstock.service.dto.TransferItemDTO;
import java.util.List;

/**
 * Service Interface for managing TransferItem.
 */
public interface TransferItemService {

    /**
     * Save a transferItem.
     *
     * @param transferItemDTO the entity to save
     * @return the persisted entity
     */
    TransferItemDTO save(TransferItemDTO transferItemDTO);

    /**
     *  Get all the transferItems.
     *
     *  @return the list of entities
     */
    List<TransferItemDTO> findAll();

    /**
     *  Get the "id" transferItem.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    TransferItemDTO findOne(Long id);

    /**
     *  Delete the "id" transferItem.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the transferItem corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @return the list of entities
     */
    List<TransferItemDTO> search(String query);
}
