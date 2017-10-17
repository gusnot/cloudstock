package com.gusnot.cloudstock.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.gusnot.cloudstock.service.TransactionItemService;
import com.gusnot.cloudstock.web.rest.util.HeaderUtil;
import com.gusnot.cloudstock.service.dto.TransactionItemDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * REST controller for managing TransactionItem.
 */
@RestController
@RequestMapping("/api")
public class TransactionItemResource {

    private final Logger log = LoggerFactory.getLogger(TransactionItemResource.class);

    private static final String ENTITY_NAME = "transactionItem";

    private final TransactionItemService transactionItemService;

    public TransactionItemResource(TransactionItemService transactionItemService) {
        this.transactionItemService = transactionItemService;
    }

    /**
     * POST  /transaction-items : Create a new transactionItem.
     *
     * @param transactionItemDTO the transactionItemDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new transactionItemDTO, or with status 400 (Bad Request) if the transactionItem has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/transaction-items")
    @Timed
    public ResponseEntity<TransactionItemDTO> createTransactionItem(@Valid @RequestBody TransactionItemDTO transactionItemDTO) throws URISyntaxException {
        log.debug("REST request to save TransactionItem : {}", transactionItemDTO);
        if (transactionItemDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new transactionItem cannot already have an ID")).body(null);
        }
        TransactionItemDTO result = transactionItemService.save(transactionItemDTO);
        return ResponseEntity.created(new URI("/api/transaction-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /transaction-items : Updates an existing transactionItem.
     *
     * @param transactionItemDTO the transactionItemDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated transactionItemDTO,
     * or with status 400 (Bad Request) if the transactionItemDTO is not valid,
     * or with status 500 (Internal Server Error) if the transactionItemDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/transaction-items")
    @Timed
    public ResponseEntity<TransactionItemDTO> updateTransactionItem(@Valid @RequestBody TransactionItemDTO transactionItemDTO) throws URISyntaxException {
        log.debug("REST request to update TransactionItem : {}", transactionItemDTO);
        if (transactionItemDTO.getId() == null) {
            return createTransactionItem(transactionItemDTO);
        }
        TransactionItemDTO result = transactionItemService.save(transactionItemDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, transactionItemDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /transaction-items : get all the transactionItems.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of transactionItems in body
     */
    @GetMapping("/transaction-items")
    @Timed
    public List<TransactionItemDTO> getAllTransactionItems() {
        log.debug("REST request to get all TransactionItems");
        return transactionItemService.findAll();
        }

    /**
     * GET  /transaction-items/:id : get the "id" transactionItem.
     *
     * @param id the id of the transactionItemDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the transactionItemDTO, or with status 404 (Not Found)
     */
    @GetMapping("/transaction-items/{id}")
    @Timed
    public ResponseEntity<TransactionItemDTO> getTransactionItem(@PathVariable Long id) {
        log.debug("REST request to get TransactionItem : {}", id);
        TransactionItemDTO transactionItemDTO = transactionItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(transactionItemDTO));
    }

    /**
     * DELETE  /transaction-items/:id : delete the "id" transactionItem.
     *
     * @param id the id of the transactionItemDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/transaction-items/{id}")
    @Timed
    public ResponseEntity<Void> deleteTransactionItem(@PathVariable Long id) {
        log.debug("REST request to delete TransactionItem : {}", id);
        transactionItemService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/transaction-items?query=:query : search for the transactionItem corresponding
     * to the query.
     *
     * @param query the query of the transactionItem search
     * @return the result of the search
     */
    @GetMapping("/_search/transaction-items")
    @Timed
    public List<TransactionItemDTO> searchTransactionItems(@RequestParam String query) {
        log.debug("REST request to search TransactionItems for query {}", query);
        return transactionItemService.search(query);
    }

}
