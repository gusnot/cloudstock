package com.gusnot.cloudstock.service.impl;

import com.gusnot.cloudstock.service.BillService;
import com.gusnot.cloudstock.domain.Bill;
import com.gusnot.cloudstock.repository.BillRepository;
import com.gusnot.cloudstock.repository.search.BillSearchRepository;
import com.gusnot.cloudstock.service.dto.BillDTO;
import com.gusnot.cloudstock.service.mapper.BillMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Bill.
 */
@Service
@Transactional
public class BillServiceImpl implements BillService{

    private final Logger log = LoggerFactory.getLogger(BillServiceImpl.class);

    private final BillRepository billRepository;

    private final BillMapper billMapper;

    private final BillSearchRepository billSearchRepository;

    public BillServiceImpl(BillRepository billRepository, BillMapper billMapper, BillSearchRepository billSearchRepository) {
        this.billRepository = billRepository;
        this.billMapper = billMapper;
        this.billSearchRepository = billSearchRepository;
    }

    /**
     * Save a bill.
     *
     * @param billDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public BillDTO save(BillDTO billDTO) {
        log.debug("Request to save Bill : {}", billDTO);
        Bill bill = billMapper.toEntity(billDTO);
        bill = billRepository.save(bill);
        BillDTO result = billMapper.toDto(bill);
        billSearchRepository.save(bill);
        return result;
    }

    /**
     *  Get all the bills.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<BillDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Bills");
        return billRepository.findAll(pageable)
            .map(billMapper::toDto);
    }

    /**
     *  Get one bill by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public BillDTO findOne(Long id) {
        log.debug("Request to get Bill : {}", id);
        Bill bill = billRepository.findOne(id);
        return billMapper.toDto(bill);
    }

    /**
     *  Delete the  bill by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Bill : {}", id);
        billRepository.delete(id);
        billSearchRepository.delete(id);
    }

    /**
     * Search for the bill corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<BillDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Bills for query {}", query);
        Page<Bill> result = billSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(billMapper::toDto);
    }
}
