package com.gusnot.cloudstock.service.impl;

import com.gusnot.cloudstock.service.TransactionItemService;
import com.gusnot.cloudstock.domain.TransactionItem;
import com.gusnot.cloudstock.repository.TransactionItemRepository;
import com.gusnot.cloudstock.repository.search.TransactionItemSearchRepository;
import com.gusnot.cloudstock.service.dto.TransactionItemDTO;
import com.gusnot.cloudstock.service.mapper.TransactionItemMapper;
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
 * Service Implementation for managing TransactionItem.
 */
@Service
@Transactional
public class TransactionItemServiceImpl implements TransactionItemService{

    private final Logger log = LoggerFactory.getLogger(TransactionItemServiceImpl.class);

    private final TransactionItemRepository transactionItemRepository;

    private final TransactionItemMapper transactionItemMapper;

    private final TransactionItemSearchRepository transactionItemSearchRepository;

    public TransactionItemServiceImpl(TransactionItemRepository transactionItemRepository, TransactionItemMapper transactionItemMapper, TransactionItemSearchRepository transactionItemSearchRepository) {
        this.transactionItemRepository = transactionItemRepository;
        this.transactionItemMapper = transactionItemMapper;
        this.transactionItemSearchRepository = transactionItemSearchRepository;
    }

    /**
     * Save a transactionItem.
     *
     * @param transactionItemDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public TransactionItemDTO save(TransactionItemDTO transactionItemDTO) {
        log.debug("Request to save TransactionItem : {}", transactionItemDTO);
        TransactionItem transactionItem = transactionItemMapper.toEntity(transactionItemDTO);
        transactionItem = transactionItemRepository.save(transactionItem);
        TransactionItemDTO result = transactionItemMapper.toDto(transactionItem);
        transactionItemSearchRepository.save(transactionItem);
        return result;
    }

    /**
     *  Get all the transactionItems.
     *
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<TransactionItemDTO> findAll() {
        log.debug("Request to get all TransactionItems");
        return transactionItemRepository.findAll().stream()
            .map(transactionItemMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get one transactionItem by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public TransactionItemDTO findOne(Long id) {
        log.debug("Request to get TransactionItem : {}", id);
        TransactionItem transactionItem = transactionItemRepository.findOne(id);
        return transactionItemMapper.toDto(transactionItem);
    }

    /**
     *  Delete the  transactionItem by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete TransactionItem : {}", id);
        transactionItemRepository.delete(id);
        transactionItemSearchRepository.delete(id);
    }

    /**
     * Search for the transactionItem corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<TransactionItemDTO> search(String query) {
        log.debug("Request to search TransactionItems for query {}", query);
        return StreamSupport
            .stream(transactionItemSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(transactionItemMapper::toDto)
            .collect(Collectors.toList());
    }
}
