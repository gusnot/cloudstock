package com.gusnot.cloudstock.service.impl;

import com.gusnot.cloudstock.service.ProductTypeService;
import com.gusnot.cloudstock.domain.ProductType;
import com.gusnot.cloudstock.repository.ProductTypeRepository;
import com.gusnot.cloudstock.repository.search.ProductTypeSearchRepository;
import com.gusnot.cloudstock.service.dto.ProductTypeDTO;
import com.gusnot.cloudstock.service.mapper.ProductTypeMapper;
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
 * Service Implementation for managing ProductType.
 */
@Service
@Transactional
public class ProductTypeServiceImpl implements ProductTypeService{

    private final Logger log = LoggerFactory.getLogger(ProductTypeServiceImpl.class);

    private final ProductTypeRepository productTypeRepository;

    private final ProductTypeMapper productTypeMapper;

    private final ProductTypeSearchRepository productTypeSearchRepository;

    public ProductTypeServiceImpl(ProductTypeRepository productTypeRepository, ProductTypeMapper productTypeMapper, ProductTypeSearchRepository productTypeSearchRepository) {
        this.productTypeRepository = productTypeRepository;
        this.productTypeMapper = productTypeMapper;
        this.productTypeSearchRepository = productTypeSearchRepository;
    }

    /**
     * Save a productType.
     *
     * @param productTypeDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public ProductTypeDTO save(ProductTypeDTO productTypeDTO) {
        log.debug("Request to save ProductType : {}", productTypeDTO);
        ProductType productType = productTypeMapper.toEntity(productTypeDTO);
        productType = productTypeRepository.save(productType);
        ProductTypeDTO result = productTypeMapper.toDto(productType);
        productTypeSearchRepository.save(productType);
        return result;
    }

    /**
     *  Get all the productTypes.
     *
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<ProductTypeDTO> findAll() {
        log.debug("Request to get all ProductTypes");
        return productTypeRepository.findAll().stream()
            .map(productTypeMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get one productType by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public ProductTypeDTO findOne(Long id) {
        log.debug("Request to get ProductType : {}", id);
        ProductType productType = productTypeRepository.findOne(id);
        return productTypeMapper.toDto(productType);
    }

    /**
     *  Delete the  productType by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ProductType : {}", id);
        productTypeRepository.delete(id);
        productTypeSearchRepository.delete(id);
    }

    /**
     * Search for the productType corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<ProductTypeDTO> search(String query) {
        log.debug("Request to search ProductTypes for query {}", query);
        return StreamSupport
            .stream(productTypeSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(productTypeMapper::toDto)
            .collect(Collectors.toList());
    }
}
