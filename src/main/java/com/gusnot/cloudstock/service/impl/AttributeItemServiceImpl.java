package com.gusnot.cloudstock.service.impl;

import com.gusnot.cloudstock.service.AttributeItemService;
import com.gusnot.cloudstock.domain.AttributeItem;
import com.gusnot.cloudstock.repository.AttributeItemRepository;
import com.gusnot.cloudstock.repository.search.AttributeItemSearchRepository;
import com.gusnot.cloudstock.service.dto.AttributeItemDTO;
import com.gusnot.cloudstock.service.mapper.AttributeItemMapper;
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
 * Service Implementation for managing AttributeItem.
 */
@Service
@Transactional
public class AttributeItemServiceImpl implements AttributeItemService{

    private final Logger log = LoggerFactory.getLogger(AttributeItemServiceImpl.class);

    private final AttributeItemRepository attributeItemRepository;

    private final AttributeItemMapper attributeItemMapper;

    private final AttributeItemSearchRepository attributeItemSearchRepository;

    public AttributeItemServiceImpl(AttributeItemRepository attributeItemRepository, AttributeItemMapper attributeItemMapper, AttributeItemSearchRepository attributeItemSearchRepository) {
        this.attributeItemRepository = attributeItemRepository;
        this.attributeItemMapper = attributeItemMapper;
        this.attributeItemSearchRepository = attributeItemSearchRepository;
    }

    /**
     * Save a attributeItem.
     *
     * @param attributeItemDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public AttributeItemDTO save(AttributeItemDTO attributeItemDTO) {
        log.debug("Request to save AttributeItem : {}", attributeItemDTO);
        AttributeItem attributeItem = attributeItemMapper.toEntity(attributeItemDTO);
        attributeItem = attributeItemRepository.save(attributeItem);
        AttributeItemDTO result = attributeItemMapper.toDto(attributeItem);
        attributeItemSearchRepository.save(attributeItem);
        return result;
    }

    /**
     *  Get all the attributeItems.
     *
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<AttributeItemDTO> findAll() {
        log.debug("Request to get all AttributeItems");
        return attributeItemRepository.findAll().stream()
            .map(attributeItemMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get one attributeItem by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public AttributeItemDTO findOne(Long id) {
        log.debug("Request to get AttributeItem : {}", id);
        AttributeItem attributeItem = attributeItemRepository.findOne(id);
        return attributeItemMapper.toDto(attributeItem);
    }

    /**
     *  Delete the  attributeItem by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete AttributeItem : {}", id);
        attributeItemRepository.delete(id);
        attributeItemSearchRepository.delete(id);
    }

    /**
     * Search for the attributeItem corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<AttributeItemDTO> search(String query) {
        log.debug("Request to search AttributeItems for query {}", query);
        return StreamSupport
            .stream(attributeItemSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(attributeItemMapper::toDto)
            .collect(Collectors.toList());
    }
}
