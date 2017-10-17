package com.gusnot.cloudstock.service.impl;

import com.gusnot.cloudstock.service.StockService;
import com.gusnot.cloudstock.domain.Stock;
import com.gusnot.cloudstock.repository.StockRepository;
import com.gusnot.cloudstock.repository.search.StockSearchRepository;
import com.gusnot.cloudstock.service.dto.StockDTO;
import com.gusnot.cloudstock.service.mapper.StockMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Stock.
 */
@Service
@Transactional
public class StockServiceImpl implements StockService{

    private final Logger log = LoggerFactory.getLogger(StockServiceImpl.class);

    private final StockRepository stockRepository;

    private final StockMapper stockMapper;

    private final StockSearchRepository stockSearchRepository;

    public StockServiceImpl(StockRepository stockRepository, StockMapper stockMapper, StockSearchRepository stockSearchRepository) {
        this.stockRepository = stockRepository;
        this.stockMapper = stockMapper;
        this.stockSearchRepository = stockSearchRepository;
    }

    /**
     * Save a stock.
     *
     * @param stockDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public StockDTO save(StockDTO stockDTO) {
        log.debug("Request to save Stock : {}", stockDTO);
        Stock stock = stockMapper.toEntity(stockDTO);
        stock = stockRepository.save(stock);
        StockDTO result = stockMapper.toDto(stock);
        stockSearchRepository.save(stock);
        return result;
    }

    /**
     *  Get all the stocks.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<StockDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Stocks");
        return stockRepository.findAll(pageable)
            .map(stockMapper::toDto);
    }

    /**
     *  Get one stock by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public StockDTO findOne(Long id) {
        log.debug("Request to get Stock : {}", id);
        Stock stock = stockRepository.findOne(id);
        return stockMapper.toDto(stock);
    }

    /**
     *  Delete the  stock by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Stock : {}", id);
        stockRepository.delete(id);
        stockSearchRepository.delete(id);
    }

    /**
     * Search for the stock corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<StockDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Stocks for query {}", query);
        Page<Stock> result = stockSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(stockMapper::toDto);
    }
}
