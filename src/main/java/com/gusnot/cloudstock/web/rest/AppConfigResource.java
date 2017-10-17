package com.gusnot.cloudstock.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.gusnot.cloudstock.service.AppConfigService;
import com.gusnot.cloudstock.web.rest.util.HeaderUtil;
import com.gusnot.cloudstock.service.dto.AppConfigDTO;
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
 * REST controller for managing AppConfig.
 */
@RestController
@RequestMapping("/api")
public class AppConfigResource {

    private final Logger log = LoggerFactory.getLogger(AppConfigResource.class);

    private static final String ENTITY_NAME = "appConfig";

    private final AppConfigService appConfigService;

    public AppConfigResource(AppConfigService appConfigService) {
        this.appConfigService = appConfigService;
    }

    /**
     * POST  /app-configs : Create a new appConfig.
     *
     * @param appConfigDTO the appConfigDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new appConfigDTO, or with status 400 (Bad Request) if the appConfig has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/app-configs")
    @Timed
    public ResponseEntity<AppConfigDTO> createAppConfig(@Valid @RequestBody AppConfigDTO appConfigDTO) throws URISyntaxException {
        log.debug("REST request to save AppConfig : {}", appConfigDTO);
        if (appConfigDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new appConfig cannot already have an ID")).body(null);
        }
        AppConfigDTO result = appConfigService.save(appConfigDTO);
        return ResponseEntity.created(new URI("/api/app-configs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /app-configs : Updates an existing appConfig.
     *
     * @param appConfigDTO the appConfigDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated appConfigDTO,
     * or with status 400 (Bad Request) if the appConfigDTO is not valid,
     * or with status 500 (Internal Server Error) if the appConfigDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/app-configs")
    @Timed
    public ResponseEntity<AppConfigDTO> updateAppConfig(@Valid @RequestBody AppConfigDTO appConfigDTO) throws URISyntaxException {
        log.debug("REST request to update AppConfig : {}", appConfigDTO);
        if (appConfigDTO.getId() == null) {
            return createAppConfig(appConfigDTO);
        }
        AppConfigDTO result = appConfigService.save(appConfigDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, appConfigDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /app-configs : get all the appConfigs.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of appConfigs in body
     */
    @GetMapping("/app-configs")
    @Timed
    public List<AppConfigDTO> getAllAppConfigs() {
        log.debug("REST request to get all AppConfigs");
        return appConfigService.findAll();
        }

    /**
     * GET  /app-configs/:id : get the "id" appConfig.
     *
     * @param id the id of the appConfigDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the appConfigDTO, or with status 404 (Not Found)
     */
    @GetMapping("/app-configs/{id}")
    @Timed
    public ResponseEntity<AppConfigDTO> getAppConfig(@PathVariable Long id) {
        log.debug("REST request to get AppConfig : {}", id);
        AppConfigDTO appConfigDTO = appConfigService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(appConfigDTO));
    }

    /**
     * DELETE  /app-configs/:id : delete the "id" appConfig.
     *
     * @param id the id of the appConfigDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/app-configs/{id}")
    @Timed
    public ResponseEntity<Void> deleteAppConfig(@PathVariable Long id) {
        log.debug("REST request to delete AppConfig : {}", id);
        appConfigService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/app-configs?query=:query : search for the appConfig corresponding
     * to the query.
     *
     * @param query the query of the appConfig search
     * @return the result of the search
     */
    @GetMapping("/_search/app-configs")
    @Timed
    public List<AppConfigDTO> searchAppConfigs(@RequestParam String query) {
        log.debug("REST request to search AppConfigs for query {}", query);
        return appConfigService.search(query);
    }

}
