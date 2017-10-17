package com.gusnot.cloudstock.service;

import com.gusnot.cloudstock.service.dto.TransactionItemDTO;
import java.util.List;

/**
 * Service Interface for managing TransactionItem.
 */
public interface TransactionItemService {

    /**
     * Save a transactionItem.
     *
     * @param transactionItemDTO the entity to save
     * @return the persisted entity
     */
    TransactionItemDTO save(TransactionItemDTO transactionItemDTO);

    /**
     *  Get all the transactionItems.
     *
     *  @return the list of entities
     */
    List<TransactionItemDTO> findAll();

    /**
     *  Get the "id" transactionItem.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    TransactionItemDTO findOne(Long id);

    /**
     *  Delete the "id" transactionItem.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the transactionItem corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @return the list of entities
     */
    List<TransactionItemDTO> search(String query);
}
