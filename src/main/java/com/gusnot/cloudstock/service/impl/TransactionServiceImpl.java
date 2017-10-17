package com.gusnot.cloudstock.service.impl;

import com.gusnot.cloudstock.service.TransactionService;
import com.gusnot.cloudstock.domain.Transaction;
import com.gusnot.cloudstock.repository.TransactionRepository;
import com.gusnot.cloudstock.repository.search.TransactionSearchRepository;
import com.gusnot.cloudstock.service.dto.TransactionDTO;
import com.gusnot.cloudstock.service.mapper.TransactionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Transaction.
 */
@Service
@Transactional
public class TransactionServiceImpl implements TransactionService{

    private final Logger log = LoggerFactory.getLogger(TransactionServiceImpl.class);

    private final TransactionRepository transactionRepository;

    private final TransactionMapper transactionMapper;

    private final TransactionSearchRepository transactionSearchRepository;

    public TransactionServiceImpl(TransactionRepository transactionRepository, TransactionMapper transactionMapper, TransactionSearchRepository transactionSearchRepository) {
        this.transactionRepository = transactionRepository;
        this.transactionMapper = transactionMapper;
        this.transactionSearchRepository = transactionSearchRepository;
    }

    /**
     * Save a transaction.
     *
     * @param transactionDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public TransactionDTO save(TransactionDTO transactionDTO) {
        log.debug("Request to save Transaction : {}", transactionDTO);
        Transaction transaction = transactionMapper.toEntity(transactionDTO);
        transaction = transactionRepository.save(transaction);
        TransactionDTO result = transactionMapper.toDto(transaction);
        transactionSearchRepository.save(transaction);
        return result;
    }

    /**
     *  Get all the transactions.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TransactionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Transactions");
        return transactionRepository.findAll(pageable)
            .map(transactionMapper::toDto);
    }

    /**
     *  Get one transaction by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public TransactionDTO findOne(Long id) {
        log.debug("Request to get Transaction : {}", id);
        Transaction transaction = transactionRepository.findOne(id);
        return transactionMapper.toDto(transaction);
    }

    /**
     *  Delete the  transaction by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Transaction : {}", id);
        transactionRepository.delete(id);
        transactionSearchRepository.delete(id);
    }

    /**
     * Search for the transaction corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TransactionDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Transactions for query {}", query);
        Page<Transaction> result = transactionSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(transactionMapper::toDto);
    }
}
