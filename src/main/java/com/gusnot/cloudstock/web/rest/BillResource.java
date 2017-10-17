package com.gusnot.cloudstock.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.gusnot.cloudstock.service.BillService;
import com.gusnot.cloudstock.web.rest.util.HeaderUtil;
import com.gusnot.cloudstock.web.rest.util.PaginationUtil;
import com.gusnot.cloudstock.service.dto.BillDTO;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Bill.
 */
@RestController
@RequestMapping("/api")
public class BillResource {

    private final Logger log = LoggerFactory.getLogger(BillResource.class);

    private static final String ENTITY_NAME = "bill";

    private final BillService billService;

    public BillResource(BillService billService) {
        this.billService = billService;
    }

    /**
     * POST  /bills : Create a new bill.
     *
     * @param billDTO the billDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new billDTO, or with status 400 (Bad Request) if the bill has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/bills")
    @Timed
    public ResponseEntity<BillDTO> createBill(@Valid @RequestBody BillDTO billDTO) throws URISyntaxException {
        log.debug("REST request to save Bill : {}", billDTO);
        if (billDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new bill cannot already have an ID")).body(null);
        }
        BillDTO result = billService.save(billDTO);
        return ResponseEntity.created(new URI("/api/bills/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /bills : Updates an existing bill.
     *
     * @param billDTO the billDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated billDTO,
     * or with status 400 (Bad Request) if the billDTO is not valid,
     * or with status 500 (Internal Server Error) if the billDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/bills")
    @Timed
    public ResponseEntity<BillDTO> updateBill(@Valid @RequestBody BillDTO billDTO) throws URISyntaxException {
        log.debug("REST request to update Bill : {}", billDTO);
        if (billDTO.getId() == null) {
            return createBill(billDTO);
        }
        BillDTO result = billService.save(billDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, billDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /bills : get all the bills.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of bills in body
     */
    @GetMapping("/bills")
    @Timed
    public ResponseEntity<List<BillDTO>> getAllBills(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Bills");
        Page<BillDTO> page = billService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/bills");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /bills/:id : get the "id" bill.
     *
     * @param id the id of the billDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the billDTO, or with status 404 (Not Found)
     */
    @GetMapping("/bills/{id}")
    @Timed
    public ResponseEntity<BillDTO> getBill(@PathVariable Long id) {
        log.debug("REST request to get Bill : {}", id);
        BillDTO billDTO = billService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(billDTO));
    }

    /**
     * DELETE  /bills/:id : delete the "id" bill.
     *
     * @param id the id of the billDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/bills/{id}")
    @Timed
    public ResponseEntity<Void> deleteBill(@PathVariable Long id) {
        log.debug("REST request to delete Bill : {}", id);
        billService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/bills?query=:query : search for the bill corresponding
     * to the query.
     *
     * @param query the query of the bill search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/bills")
    @Timed
    public ResponseEntity<List<BillDTO>> searchBills(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Bills for query {}", query);
        Page<BillDTO> page = billService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/bills");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
