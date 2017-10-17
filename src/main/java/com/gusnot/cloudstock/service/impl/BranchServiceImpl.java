package com.gusnot.cloudstock.service.impl;

import com.gusnot.cloudstock.service.BranchService;
import com.gusnot.cloudstock.domain.Branch;
import com.gusnot.cloudstock.repository.BranchRepository;
import com.gusnot.cloudstock.repository.search.BranchSearchRepository;
import com.gusnot.cloudstock.service.dto.BranchDTO;
import com.gusnot.cloudstock.service.mapper.BranchMapper;
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
 * Service Implementation for managing Branch.
 */
@Service
@Transactional
public class BranchServiceImpl implements BranchService{

    private final Logger log = LoggerFactory.getLogger(BranchServiceImpl.class);

    private final BranchRepository branchRepository;

    private final BranchMapper branchMapper;

    private final BranchSearchRepository branchSearchRepository;

    public BranchServiceImpl(BranchRepository branchRepository, BranchMapper branchMapper, BranchSearchRepository branchSearchRepository) {
        this.branchRepository = branchRepository;
        this.branchMapper = branchMapper;
        this.branchSearchRepository = branchSearchRepository;
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
        Branch branch = branchMapper.toEntity(branchDTO);
        branch = branchRepository.save(branch);
        BranchDTO result = branchMapper.toDto(branch);
        branchSearchRepository.save(branch);
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
        return branchRepository.findAll().stream()
            .map(branchMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
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
        return branchMapper.toDto(branch);
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
        branchSearchRepository.delete(id);
    }

    /**
     * Search for the branch corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<BranchDTO> search(String query) {
        log.debug("Request to search Branches for query {}", query);
        return StreamSupport
            .stream(branchSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(branchMapper::toDto)
            .collect(Collectors.toList());
    }
}
