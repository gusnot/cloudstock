package com.gusnot.cloudstock.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.gusnot.cloudstock.service.AttributeItemService;
import com.gusnot.cloudstock.web.rest.util.HeaderUtil;
import com.gusnot.cloudstock.service.dto.AttributeItemDTO;
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
 * REST controller for managing AttributeItem.
 */
@RestController
@RequestMapping("/api")
public class AttributeItemResource {

    private final Logger log = LoggerFactory.getLogger(AttributeItemResource.class);

    private static final String ENTITY_NAME = "attributeItem";

    private final AttributeItemService attributeItemService;

    public AttributeItemResource(AttributeItemService attributeItemService) {
        this.attributeItemService = attributeItemService;
    }

    /**
     * POST  /attribute-items : Create a new attributeItem.
     *
     * @param attributeItemDTO the attributeItemDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new attributeItemDTO, or with status 400 (Bad Request) if the attributeItem has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/attribute-items")
    @Timed
    public ResponseEntity<AttributeItemDTO> createAttributeItem(@Valid @RequestBody AttributeItemDTO attributeItemDTO) throws URISyntaxException {
        log.debug("REST request to save AttributeItem : {}", attributeItemDTO);
        if (attributeItemDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new attributeItem cannot already have an ID")).body(null);
        }
        AttributeItemDTO result = attributeItemService.save(attributeItemDTO);
        return ResponseEntity.created(new URI("/api/attribute-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /attribute-items : Updates an existing attributeItem.
     *
     * @param attributeItemDTO the attributeItemDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated attributeItemDTO,
     * or with status 400 (Bad Request) if the attributeItemDTO is not valid,
     * or with status 500 (Internal Server Error) if the attributeItemDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/attribute-items")
    @Timed
    public ResponseEntity<AttributeItemDTO> updateAttributeItem(@Valid @RequestBody AttributeItemDTO attributeItemDTO) throws URISyntaxException {
        log.debug("REST request to update AttributeItem : {}", attributeItemDTO);
        if (attributeItemDTO.getId() == null) {
            return createAttributeItem(attributeItemDTO);
        }
        AttributeItemDTO result = attributeItemService.save(attributeItemDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, attributeItemDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /attribute-items : get all the attributeItems.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of attributeItems in body
     */
    @GetMapping("/attribute-items")
    @Timed
    public List<AttributeItemDTO> getAllAttributeItems() {
        log.debug("REST request to get all AttributeItems");
        return attributeItemService.findAll();
        }

    /**
     * GET  /attribute-items/:id : get the "id" attributeItem.
     *
     * @param id the id of the attributeItemDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the attributeItemDTO, or with status 404 (Not Found)
     */
    @GetMapping("/attribute-items/{id}")
    @Timed
    public ResponseEntity<AttributeItemDTO> getAttributeItem(@PathVariable Long id) {
        log.debug("REST request to get AttributeItem : {}", id);
        AttributeItemDTO attributeItemDTO = attributeItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(attributeItemDTO));
    }

    /**
     * DELETE  /attribute-items/:id : delete the "id" attributeItem.
     *
     * @param id the id of the attributeItemDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/attribute-items/{id}")
    @Timed
    public ResponseEntity<Void> deleteAttributeItem(@PathVariable Long id) {
        log.debug("REST request to delete AttributeItem : {}", id);
        attributeItemService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/attribute-items?query=:query : search for the attributeItem corresponding
     * to the query.
     *
     * @param query the query of the attributeItem search
     * @return the result of the search
     */
    @GetMapping("/_search/attribute-items")
    @Timed
    public List<AttributeItemDTO> searchAttributeItems(@RequestParam String query) {
        log.debug("REST request to search AttributeItems for query {}", query);
        return attributeItemService.search(query);
    }

}
