package com.gusnot.cloudstock.service.impl;

import com.gusnot.cloudstock.service.BranchService;
import com.gusnot.cloudstock.domain.Branch;
import com.gusnot.cloudstock.repository.BranchRepository;
import com.gusnot.cloudstock.service.dto.BranchDTO;
import com.gusnot.cloudstock.service.mapper.BranchMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Branch.
 */
@Service
@Transactional
public class BranchServiceImpl implements BranchService{

    private final Logger log = LoggerFactory.getLogger(BranchServiceImpl.class);
    
    private final BranchRepository branchRepository;

    private final BranchMapper branchMapper;

    public BranchServiceImpl(BranchRepository branchRepository, BranchMapper branchMapper) {
        this.branchRepository = branchRepository;
        this.branchMapper = branchMapper;
    }

    /**
     * Save a branch.
     *
     * @param branchDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public BranchDTO save(BranchDTO branchDTO) {
        log.debug("Request to save Branch : {}", branchDTO);
        Branch branch = branchMapper.branchDTOToBranch(branchDTO);
        branch = branchRepository.save(branch);
        BranchDTO result = branchMapper.branchToBranchDTO(branch);
        return result;
    }

    /**
     *  Get all the branches.
     *  
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<BranchDTO> findAll() {
        log.debug("Request to get all Branches");
        List<BranchDTO> result = branchRepository.findAll().stream()
            .map(branchMapper::branchToBranchDTO)
            .collect(Collectors.toCollection(LinkedList::new));

        return result;
    }

    /**
     *  Get one branch by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public BranchDTO findOne(Long id) {
        log.debug("Request to get Branch : {}", id);
        Branch branch = branchRepository.findOne(id);
        BranchDTO branchDTO = branchMapper.branchToBranchDTO(branch);
        return branchDTO;
    }

    /**
     *  Delete the  branch by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Branch : {}", id);
        branchRepository.delete(id);
    }
}
