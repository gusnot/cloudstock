package com.gusnot.cloudstock.service;

import com.gusnot.cloudstock.service.dto.SellDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Sell.
 */
public interface SellService {

    /**
     * Save a sell.
     *
     * @param sellDTO the entity to save
     * @return the persisted entity
     */
    SellDTO save(SellDTO sellDTO);

    /**
     *  Get all the sells.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<SellDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" sell.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    SellDTO findOne(Long id);

    /**
     *  Delete the "id" sell.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the sell corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<SellDTO> search(String query, Pageable pageable);
}
