package com.gusnot.cloudstock.service.impl;

import com.gusnot.cloudstock.service.TransferItemService;
import com.gusnot.cloudstock.domain.TransferItem;
import com.gusnot.cloudstock.repository.TransferItemRepository;
import com.gusnot.cloudstock.repository.search.TransferItemSearchRepository;
import com.gusnot.cloudstock.service.dto.TransferItemDTO;
import com.gusnot.cloudstock.service.mapper.TransferItemMapper;
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
 * Service Implementation for managing TransferItem.
 */
@Service
@Transactional
public class TransferItemServiceImpl implements TransferItemService{

    private final Logger log = LoggerFactory.getLogger(TransferItemServiceImpl.class);

    private final TransferItemRepository transferItemRepository;

    private final TransferItemMapper transferItemMapper;

    private final TransferItemSearchRepository transferItemSearchRepository;

    public TransferItemServiceImpl(TransferItemRepository transferItemRepository, TransferItemMapper transferItemMapper, TransferItemSearchRepository transferItemSearchRepository) {
        this.transferItemRepository = transferItemRepository;
        this.transferItemMapper = transferItemMapper;
        this.transferItemSearchRepository = transferItemSearchRepository;
    }

    /**
     * Save a transferItem.
     *
     * @param transferItemDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public TransferItemDTO save(TransferItemDTO transferItemDTO) {
        log.debug("Request to save TransferItem : {}", transferItemDTO);
        TransferItem transferItem = transferItemMapper.toEntity(transferItemDTO);
        transferItem = transferItemRepository.save(transferItem);
        TransferItemDTO result = transferItemMapper.toDto(transferItem);
        transferItemSearchRepository.save(transferItem);
        return result;
    }

    /**
     *  Get all the transferItems.
     *
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<TransferItemDTO> findAll() {
        log.debug("Request to get all TransferItems");
        return transferItemRepository.findAll().stream()
            .map(transferItemMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get one transferItem by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public TransferItemDTO findOne(Long id) {
        log.debug("Request to get TransferItem : {}", id);
        TransferItem transferItem = transferItemRepository.findOne(id);
        return transferItemMapper.toDto(transferItem);
    }

    /**
     *  Delete the  transferItem by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete TransferItem : {}", id);
        transferItemRepository.delete(id);
        transferItemSearchRepository.delete(id);
    }

    /**
     * Search for the transferItem corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<TransferItemDTO> search(String query) {
        log.debug("Request to search TransferItems for query {}", query);
        return StreamSupport
            .stream(transferItemSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(transferItemMapper::toDto)
            .collect(Collectors.toList());
    }
}
