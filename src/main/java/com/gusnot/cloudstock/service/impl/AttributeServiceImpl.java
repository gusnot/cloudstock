package com.gusnot.cloudstock.service.impl;

import com.gusnot.cloudstock.service.AttributeService;
import com.gusnot.cloudstock.domain.Attribute;
import com.gusnot.cloudstock.repository.AttributeRepository;
import com.gusnot.cloudstock.repository.search.AttributeSearchRepository;
import com.gusnot.cloudstock.service.dto.AttributeDTO;
import com.gusnot.cloudstock.service.mapper.AttributeMapper;
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
 * Service Implementation for managing Attribute.
 */
@Service
@Transactional
public class AttributeServiceImpl implements AttributeService{

    private final Logger log = LoggerFactory.getLogger(AttributeServiceImpl.class);

    private final AttributeRepository attributeRepository;

    private final AttributeMapper attributeMapper;

    private final AttributeSearchRepository attributeSearchRepository;

    public AttributeServiceImpl(AttributeRepository attributeRepository, AttributeMapper attributeMapper, AttributeSearchRepository attributeSearchRepository) {
        this.attributeRepository = attributeRepository;
        this.attributeMapper = attributeMapper;
        this.attributeSearchRepository = attributeSearchRepository;
    }

    /**
     * Save a attribute.
     *
     * @param attributeDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public AttributeDTO save(AttributeDTO attributeDTO) {
        log.debug("Request to save Attribute : {}", attributeDTO);
        Attribute attribute = attributeMapper.toEntity(attributeDTO);
        attribute = attributeRepository.save(attribute);
        AttributeDTO result = attributeMapper.toDto(attribute);
        attributeSearchRepository.save(attribute);
        return result;
    }

    /**
     *  Get all the attributes.
     *
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<AttributeDTO> findAll() {
        log.debug("Request to get all Attributes");
        return attributeRepository.findAll().stream()
            .map(attributeMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get one attribute by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public AttributeDTO findOne(Long id) {
        log.debug("Request to get Attribute : {}", id);
        Attribute attribute = attributeRepository.findOne(id);
        return attributeMapper.toDto(attribute);
    }

    /**
     *  Delete the  attribute by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Attribute : {}", id);
        attributeRepository.delete(id);
        attributeSearchRepository.delete(id);
    }

    /**
     * Search for the attribute corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<AttributeDTO> search(String query) {
        log.debug("Request to search Attributes for query {}", query);
        return StreamSupport
            .stream(attributeSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(attributeMapper::toDto)
            .collect(Collectors.toList());
    }
}
