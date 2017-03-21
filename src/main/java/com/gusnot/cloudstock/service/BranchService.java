package com.gusnot.cloudstock.service;

import com.gusnot.cloudstock.service.dto.BranchDTO;
import java.util.List;

/**
 * Service Interface for managing Branch.
 */
public interface BranchService {

    /**
     * Save a branch.
     *
     * @param branchDTO the entity to save
     * @return the persisted entity
     */
    BranchDTO save(BranchDTO branchDTO);

    /**
     *  Get all the branches.
     *  
     *  @return the list of entities
     */
    List<BranchDTO> findAll();

    /**
     *  Get the "id" branch.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    BranchDTO findOne(Long id);

    /**
     *  Delete the "id" branch.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
