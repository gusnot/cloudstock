package com.gusnot.cloudstock.service.impl;

import com.gusnot.cloudstock.service.BillItemService;
import com.gusnot.cloudstock.domain.BillItem;
import com.gusnot.cloudstock.repository.BillItemRepository;
import com.gusnot.cloudstock.repository.search.BillItemSearchRepository;
import com.gusnot.cloudstock.service.dto.BillItemDTO;
import com.gusnot.cloudstock.service.mapper.BillItemMapper;
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
 * Service Implementation for managing BillItem.
 */
@Service
@Transactional
public class BillItemServiceImpl implements BillItemService{

    private final Logger log = LoggerFactory.getLogger(BillItemServiceImpl.class);

    private final BillItemRepository billItemRepository;

    private final BillItemMapper billItemMapper;

    private final BillItemSearchRepository billItemSearchRepository;

    public BillItemServiceImpl(BillItemRepository billItemRepository, BillItemMapper billItemMapper, BillItemSearchRepository billItemSearchRepository) {
        this.billItemRepository = billItemRepository;
        this.billItemMapper = billItemMapper;
        this.billItemSearchRepository = billItemSearchRepository;
    }

    /**
     * Save a billItem.
     *
     * @param billItemDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public BillItemDTO save(BillItemDTO billItemDTO) {
        log.debug("Request to save BillItem : {}", billItemDTO);
        BillItem billItem = billItemMapper.toEntity(billItemDTO);
        billItem = billItemRepository.save(billItem);
        BillItemDTO result = billItemMapper.toDto(billItem);
        billItemSearchRepository.save(billItem);
        return result;
    }

    /**
     *  Get all the billItems.
     *
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<BillItemDTO> findAll() {
        log.debug("Request to get all BillItems");
        return billItemRepository.findAll().stream()
            .map(billItemMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get one billItem by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public BillItemDTO findOne(Long id) {
        log.debug("Request to get BillItem : {}", id);
        BillItem billItem = billItemRepository.findOne(id);
        return billItemMapper.toDto(billItem);
    }

    /**
     *  Delete the  billItem by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete BillItem : {}", id);
        billItemRepository.delete(id);
        billItemSearchRepository.delete(id);
    }

    /**
     * Search for the billItem corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<BillItemDTO> search(String query) {
        log.debug("Request to search BillItems for query {}", query);
        return StreamSupport
            .stream(billItemSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(billItemMapper::toDto)
            .collect(Collectors.toList());
    }
}
