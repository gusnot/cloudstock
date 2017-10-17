package com.gusnot.cloudstock.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.gusnot.cloudstock.service.SkuService;
import com.gusnot.cloudstock.web.rest.util.HeaderUtil;
import com.gusnot.cloudstock.web.rest.util.PaginationUtil;
import com.gusnot.cloudstock.service.dto.SkuDTO;
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
 * REST controller for managing Sku.
 */
@RestController
@RequestMapping("/api")
public class SkuResource {

    private final Logger log = LoggerFactory.getLogger(SkuResource.class);

    private static final String ENTITY_NAME = "sku";

    private final SkuService skuService;

    public SkuResource(SkuService skuService) {
        this.skuService = skuService;
    }

    /**
     * POST  /skus : Create a new sku.
     *
     * @param skuDTO the skuDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new skuDTO, or with status 400 (Bad Request) if the sku has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/skus")
    @Timed
    public ResponseEntity<SkuDTO> createSku(@Valid @RequestBody SkuDTO skuDTO) throws URISyntaxException {
        log.debug("REST request to save Sku : {}", skuDTO);
        if (skuDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new sku cannot already have an ID")).body(null);
        }
        SkuDTO result = skuService.save(skuDTO);
        return ResponseEntity.created(new URI("/api/skus/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /skus : Updates an existing sku.
     *
     * @param skuDTO the skuDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated skuDTO,
     * or with status 400 (Bad Request) if the skuDTO is not valid,
     * or with status 500 (Internal Server Error) if the skuDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/skus")
    @Timed
    public ResponseEntity<SkuDTO> updateSku(@Valid @RequestBody SkuDTO skuDTO) throws URISyntaxException {
        log.debug("REST request to update Sku : {}", skuDTO);
        if (skuDTO.getId() == null) {
            return createSku(skuDTO);
        }
        SkuDTO result = skuService.save(skuDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, skuDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /skus : get all the skus.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of skus in body
     */
    @GetMapping("/skus")
    @Timed
    public ResponseEntity<List<SkuDTO>> getAllSkus(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Skus");
        Page<SkuDTO> page = skuService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/skus");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /skus/:id : get the "id" sku.
     *
     * @param id the id of the skuDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the skuDTO, or with status 404 (Not Found)
     */
    @GetMapping("/skus/{id}")
    @Timed
    public ResponseEntity<SkuDTO> getSku(@PathVariable Long id) {
        log.debug("REST request to get Sku : {}", id);
        SkuDTO skuDTO = skuService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(skuDTO));
    }

    /**
     * DELETE  /skus/:id : delete the "id" sku.
     *
     * @param id the id of the skuDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/skus/{id}")
    @Timed
    public ResponseEntity<Void> deleteSku(@PathVariable Long id) {
        log.debug("REST request to delete Sku : {}", id);
        skuService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/skus?query=:query : search for the sku corresponding
     * to the query.
     *
     * @param query the query of the sku search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/skus")
    @Timed
    public ResponseEntity<List<SkuDTO>> searchSkus(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Skus for query {}", query);
        Page<SkuDTO> page = skuService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/skus");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
