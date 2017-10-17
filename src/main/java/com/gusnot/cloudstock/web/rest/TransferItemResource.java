package com.gusnot.cloudstock.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.gusnot.cloudstock.service.TransferItemService;
import com.gusnot.cloudstock.web.rest.util.HeaderUtil;
import com.gusnot.cloudstock.service.dto.TransferItemDTO;
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
 * REST controller for managing TransferItem.
 */
@RestController
@RequestMapping("/api")
public class TransferItemResource {

    private final Logger log = LoggerFactory.getLogger(TransferItemResource.class);

    private static final String ENTITY_NAME = "transferItem";

    private final TransferItemService transferItemService;

    public TransferItemResource(TransferItemService transferItemService) {
        this.transferItemService = transferItemService;
    }

    /**
     * POST  /transfer-items : Create a new transferItem.
     *
     * @param transferItemDTO the transferItemDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new transferItemDTO, or with status 400 (Bad Request) if the transferItem has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/transfer-items")
    @Timed
    public ResponseEntity<TransferItemDTO> createTransferItem(@Valid @RequestBody TransferItemDTO transferItemDTO) throws URISyntaxException {
        log.debug("REST request to save TransferItem : {}", transferItemDTO);
        if (transferItemDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new transferItem cannot already have an ID")).body(null);
        }
        TransferItemDTO result = transferItemService.save(transferItemDTO);
        return ResponseEntity.created(new URI("/api/transfer-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /transfer-items : Updates an existing transferItem.
     *
     * @param transferItemDTO the transferItemDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated transferItemDTO,
     * or with status 400 (Bad Request) if the transferItemDTO is not valid,
     * or with status 500 (Internal Server Error) if the transferItemDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/transfer-items")
    @Timed
    public ResponseEntity<TransferItemDTO> updateTransferItem(@Valid @RequestBody TransferItemDTO transferItemDTO) throws URISyntaxException {
        log.debug("REST request to update TransferItem : {}", transferItemDTO);
        if (transferItemDTO.getId() == null) {
            return createTransferItem(transferItemDTO);
        }
        TransferItemDTO result = transferItemService.save(transferItemDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, transferItemDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /transfer-items : get all the transferItems.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of transferItems in body
     */
    @GetMapping("/transfer-items")
    @Timed
    public List<TransferItemDTO> getAllTransferItems() {
        log.debug("REST request to get all TransferItems");
        return transferItemService.findAll();
        }

    /**
     * GET  /transfer-items/:id : get the "id" transferItem.
     *
     * @param id the id of the transferItemDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the transferItemDTO, or with status 404 (Not Found)
     */
    @GetMapping("/transfer-items/{id}")
    @Timed
    public ResponseEntity<TransferItemDTO> getTransferItem(@PathVariable Long id) {
        log.debug("REST request to get TransferItem : {}", id);
        TransferItemDTO transferItemDTO = transferItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(transferItemDTO));
    }

    /**
     * DELETE  /transfer-items/:id : delete the "id" transferItem.
     *
     * @param id the id of the transferItemDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/transfer-items/{id}")
    @Timed
    public ResponseEntity<Void> deleteTransferItem(@PathVariable Long id) {
        log.debug("REST request to delete TransferItem : {}", id);
        transferItemService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/transfer-items?query=:query : search for the transferItem corresponding
     * to the query.
     *
     * @param query the query of the transferItem search
     * @return the result of the search
     */
    @GetMapping("/_search/transfer-items")
    @Timed
    public List<TransferItemDTO> searchTransferItems(@RequestParam String query) {
        log.debug("REST request to search TransferItems for query {}", query);
        return transferItemService.search(query);
    }

}
