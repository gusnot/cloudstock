package com.gusnot.cloudstock.service;

import com.gusnot.cloudstock.service.dto.TransferDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Transfer.
 */
public interface TransferService {

    /**
     * Save a transfer.
     *
     * @param transferDTO the entity to save
     * @return the persisted entity
     */
    TransferDTO save(TransferDTO transferDTO);

    /**
     *  Get all the transfers.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<TransferDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" transfer.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    TransferDTO findOne(Long id);

    /**
     *  Delete the "id" transfer.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the transfer corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<TransferDTO> search(String query, Pageable pageable);
}
