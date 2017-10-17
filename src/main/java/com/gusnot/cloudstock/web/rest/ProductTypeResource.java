package com.gusnot.cloudstock.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.gusnot.cloudstock.service.ProductTypeService;
import com.gusnot.cloudstock.web.rest.util.HeaderUtil;
import com.gusnot.cloudstock.service.dto.ProductTypeDTO;
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
 * REST controller for managing ProductType.
 */
@RestController
@RequestMapping("/api")
public class ProductTypeResource {

    private final Logger log = LoggerFactory.getLogger(ProductTypeResource.class);

    private static final String ENTITY_NAME = "productType";

    private final ProductTypeService productTypeService;

    public ProductTypeResource(ProductTypeService productTypeService) {
        this.productTypeService = productTypeService;
    }

    /**
     * POST  /product-types : Create a new productType.
     *
     * @param productTypeDTO the productTypeDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new productTypeDTO, or with status 400 (Bad Request) if the productType has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/product-types")
    @Timed
    public ResponseEntity<ProductTypeDTO> createProductType(@Valid @RequestBody ProductTypeDTO productTypeDTO) throws URISyntaxException {
        log.debug("REST request to save ProductType : {}", productTypeDTO);
        if (productTypeDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new productType cannot already have an ID")).body(null);
        }
        ProductTypeDTO result = productTypeService.save(productTypeDTO);
        return ResponseEntity.created(new URI("/api/product-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /product-types : Updates an existing productType.
     *
     * @param productTypeDTO the productTypeDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated productTypeDTO,
     * or with status 400 (Bad Request) if the productTypeDTO is not valid,
     * or with status 500 (Internal Server Error) if the productTypeDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/product-types")
    @Timed
    public ResponseEntity<ProductTypeDTO> updateProductType(@Valid @RequestBody ProductTypeDTO productTypeDTO) throws URISyntaxException {
        log.debug("REST request to update ProductType : {}", productTypeDTO);
        if (productTypeDTO.getId() == null) {
            return createProductType(productTypeDTO);
        }
        ProductTypeDTO result = productTypeService.save(productTypeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, productTypeDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /product-types : get all the productTypes.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of productTypes in body
     */
    @GetMapping("/product-types")
    @Timed
    public List<ProductTypeDTO> getAllProductTypes() {
        log.debug("REST request to get all ProductTypes");
        return productTypeService.findAll();
        }

    /**
     * GET  /product-types/:id : get the "id" productType.
     *
     * @param id the id of the productTypeDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the productTypeDTO, or with status 404 (Not Found)
     */
    @GetMapping("/product-types/{id}")
    @Timed
    public ResponseEntity<ProductTypeDTO> getProductType(@PathVariable Long id) {
        log.debug("REST request to get ProductType : {}", id);
        ProductTypeDTO productTypeDTO = productTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(productTypeDTO));
    }

    /**
     * DELETE  /product-types/:id : delete the "id" productType.
     *
     * @param id the id of the productTypeDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/product-types/{id}")
    @Timed
    public ResponseEntity<Void> deleteProductType(@PathVariable Long id) {
        log.debug("REST request to delete ProductType : {}", id);
        productTypeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/product-types?query=:query : search for the productType corresponding
     * to the query.
     *
     * @param query the query of the productType search
     * @return the result of the search
     */
    @GetMapping("/_search/product-types")
    @Timed
    public List<ProductTypeDTO> searchProductTypes(@RequestParam String query) {
        log.debug("REST request to search ProductTypes for query {}", query);
        return productTypeService.search(query);
    }

}
