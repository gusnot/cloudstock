package com.gusnot.cloudstock.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.gusnot.cloudstock.service.TransferService;
import com.gusnot.cloudstock.web.rest.util.HeaderUtil;
import com.gusnot.cloudstock.web.rest.util.PaginationUtil;
import com.gusnot.cloudstock.service.dto.TransferDTO;
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
 * REST controller for managing Transfer.
 */
@RestController
@RequestMapping("/api")
public class TransferResource {

    private final Logger log = LoggerFactory.getLogger(TransferResource.class);

    private static final String ENTITY_NAME = "transfer";

    private final TransferService transferService;

    public TransferResource(TransferService transferService) {
        this.transferService = transferService;
    }

    /**
     * POST  /transfers : Create a new transfer.
     *
     * @param transferDTO the transferDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new transferDTO, or with status 400 (Bad Request) if the transfer has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/transfers")
    @Timed
    public ResponseEntity<TransferDTO> createTransfer(@Valid @RequestBody TransferDTO transferDTO) throws URISyntaxException {
        log.debug("REST request to save Transfer : {}", transferDTO);
        if (transferDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new transfer cannot already have an ID")).body(null);
        }
        TransferDTO result = transferService.save(transferDTO);
        return ResponseEntity.created(new URI("/api/transfers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /transfers : Updates an existing transfer.
     *
     * @param transferDTO the transferDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated transferDTO,
     * or with status 400 (Bad Request) if the transferDTO is not valid,
     * or with status 500 (Internal Server Error) if the transferDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/transfers")
    @Timed
    public ResponseEntity<TransferDTO> updateTransfer(@Valid @RequestBody TransferDTO transferDTO) throws URISyntaxException {
        log.debug("REST request to update Transfer : {}", transferDTO);
        if (transferDTO.getId() == null) {
            return createTransfer(transferDTO);
        }
        TransferDTO result = transferService.save(transferDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, transferDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /transfers : get all the transfers.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of transfers in body
     */
    @GetMapping("/transfers")
    @Timed
    public ResponseEntity<List<TransferDTO>> getAllTransfers(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Transfers");
        Page<TransferDTO> page = transferService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/transfers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /transfers/:id : get the "id" transfer.
     *
     * @param id the id of the transferDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the transferDTO, or with status 404 (Not Found)
     */
    @GetMapping("/transfers/{id}")
    @Timed
    public ResponseEntity<TransferDTO> getTransfer(@PathVariable Long id) {
        log.debug("REST request to get Transfer : {}", id);
        TransferDTO transferDTO = transferService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(transferDTO));
    }

    /**
     * DELETE  /transfers/:id : delete the "id" transfer.
     *
     * @param id the id of the transferDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/transfers/{id}")
    @Timed
    public ResponseEntity<Void> deleteTransfer(@PathVariable Long id) {
        log.debug("REST request to delete Transfer : {}", id);
        transferService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/transfers?query=:query : search for the transfer corresponding
     * to the query.
     *
     * @param query the query of the transfer search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/transfers")
    @Timed
    public ResponseEntity<List<TransferDTO>> searchTransfers(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Transfers for query {}", query);
        Page<TransferDTO> page = transferService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/transfers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
