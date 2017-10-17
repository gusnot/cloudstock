package com.gusnot.cloudstock.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.gusnot.cloudstock.service.AttributeService;
import com.gusnot.cloudstock.web.rest.util.HeaderUtil;
import com.gusnot.cloudstock.service.dto.AttributeDTO;
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
 * REST controller for managing Attribute.
 */
@RestController
@RequestMapping("/api")
public class AttributeResource {

    private final Logger log = LoggerFactory.getLogger(AttributeResource.class);

    private static final String ENTITY_NAME = "attribute";

    private final AttributeService attributeService;

    public AttributeResource(AttributeService attributeService) {
        this.attributeService = attributeService;
    }

    /**
     * POST  /attributes : Create a new attribute.
     *
     * @param attributeDTO the attributeDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new attributeDTO, or with status 400 (Bad Request) if the attribute has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/attributes")
    @Timed
    public ResponseEntity<AttributeDTO> createAttribute(@Valid @RequestBody AttributeDTO attributeDTO) throws URISyntaxException {
        log.debug("REST request to save Attribute : {}", attributeDTO);
        if (attributeDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new attribute cannot already have an ID")).body(null);
        }
        AttributeDTO result = attributeService.save(attributeDTO);
        return ResponseEntity.created(new URI("/api/attributes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /attributes : Updates an existing attribute.
     *
     * @param attributeDTO the attributeDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated attributeDTO,
     * or with status 400 (Bad Request) if the attributeDTO is not valid,
     * or with status 500 (Internal Server Error) if the attributeDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/attributes")
    @Timed
    public ResponseEntity<AttributeDTO> updateAttribute(@Valid @RequestBody AttributeDTO attributeDTO) throws URISyntaxException {
        log.debug("REST request to update Attribute : {}", attributeDTO);
        if (attributeDTO.getId() == null) {
            return createAttribute(attributeDTO);
        }
        AttributeDTO result = attributeService.save(attributeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, attributeDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /attributes : get all the attributes.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of attributes in body
     */
    @GetMapping("/attributes")
    @Timed
    public List<AttributeDTO> getAllAttributes() {
        log.debug("REST request to get all Attributes");
        return attributeService.findAll();
        }

    /**
     * GET  /attributes/:id : get the "id" attribute.
     *
     * @param id the id of the attributeDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the attributeDTO, or with status 404 (Not Found)
     */
    @GetMapping("/attributes/{id}")
    @Timed
    public ResponseEntity<AttributeDTO> getAttribute(@PathVariable Long id) {
        log.debug("REST request to get Attribute : {}", id);
        AttributeDTO attributeDTO = attributeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(attributeDTO));
    }

    /**
     * DELETE  /attributes/:id : delete the "id" attribute.
     *
     * @param id the id of the attributeDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/attributes/{id}")
    @Timed
    public ResponseEntity<Void> deleteAttribute(@PathVariable Long id) {
        log.debug("REST request to delete Attribute : {}", id);
        attributeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/attributes?query=:query : search for the attribute corresponding
     * to the query.
     *
     * @param query the query of the attribute search
     * @return the result of the search
     */
    @GetMapping("/_search/attributes")
    @Timed
    public List<AttributeDTO> searchAttributes(@RequestParam String query) {
        log.debug("REST request to search Attributes for query {}", query);
        return attributeService.search(query);
    }

}
