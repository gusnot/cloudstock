package com.gusnot.cloudstock.service.impl;

import com.gusnot.cloudstock.service.SkuService;
import com.gusnot.cloudstock.domain.Sku;
import com.gusnot.cloudstock.repository.SkuRepository;
import com.gusnot.cloudstock.repository.search.SkuSearchRepository;
import com.gusnot.cloudstock.service.dto.SkuDTO;
import com.gusnot.cloudstock.service.mapper.SkuMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Sku.
 */
@Service
@Transactional
public class SkuServiceImpl implements SkuService{

    private final Logger log = LoggerFactory.getLogger(SkuServiceImpl.class);

    private final SkuRepository skuRepository;

    private final SkuMapper skuMapper;

    private final SkuSearchRepository skuSearchRepository;

    public SkuServiceImpl(SkuRepository skuRepository, SkuMapper skuMapper, SkuSearchRepository skuSearchRepository) {
        this.skuRepository = skuRepository;
        this.skuMapper = skuMapper;
        this.skuSearchRepository = skuSearchRepository;
    }

    /**
     * Save a sku.
     *
     * @param skuDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public SkuDTO save(SkuDTO skuDTO) {
        log.debug("Request to save Sku : {}", skuDTO);
        Sku sku = skuMapper.toEntity(skuDTO);
        sku = skuRepository.save(sku);
        SkuDTO result = skuMapper.toDto(sku);
        skuSearchRepository.save(sku);
        return result;
    }

    /**
     *  Get all the skus.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<SkuDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Skus");
        return skuRepository.findAll(pageable)
            .map(skuMapper::toDto);
    }

    /**
     *  Get one sku by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public SkuDTO findOne(Long id) {
        log.debug("Request to get Sku : {}", id);
        Sku sku = skuRepository.findOne(id);
        return skuMapper.toDto(sku);
    }

    /**
     *  Delete the  sku by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Sku : {}", id);
        skuRepository.delete(id);
        skuSearchRepository.delete(id);
    }

    /**
     * Search for the sku corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<SkuDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Skus for query {}", query);
        Page<Sku> result = skuSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(skuMapper::toDto);
    }
}
