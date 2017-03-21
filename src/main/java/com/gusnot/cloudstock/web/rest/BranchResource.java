package com.gusnot.cloudstock.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.gusnot.cloudstock.service.BranchService;
import com.gusnot.cloudstock.web.rest.util.HeaderUtil;
import com.gusnot.cloudstock.service.dto.BranchDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing Branch.
 */
@RestController
@RequestMapping("/api")
public class BranchResource {

    private final Logger log = LoggerFactory.getLogger(BranchResource.class);

    private static final String ENTITY_NAME = "branch";
        
    private final BranchService branchService;

    public BranchResource(BranchService branchService) {
        this.branchService = branchService;
    }

    /**
     * POST  /branches : Create a new branch.
     *
     * @param branchDTO the branchDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new branchDTO, or with status 400 (Bad Request) if the branch has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/branches")
    @Timed
    public ResponseEntity<BranchDTO> createBranch(@Valid @RequestBody BranchDTO branchDTO) throws URISyntaxException {
        log.debug("REST request to save Branch : {}", branchDTO);
        if (branchDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new branch cannot already have an ID")).body(null);
        }
        BranchDTO result = branchService.save(branchDTO);
        return ResponseEntity.created(new URI("/api/branches/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /branches : Updates an existing branch.
     *
     * @param branchDTO the branchDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated branchDTO,
     * or with status 400 (Bad Request) if the branchDTO is not valid,
     * or with status 500 (Internal Server Error) if the branchDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/branches")
    @Timed
    public ResponseEntity<BranchDTO> updateBranch(@Valid @RequestBody BranchDTO branchDTO) throws URISyntaxException {
        log.debug("REST request to update Branch : {}", branchDTO);
        if (branchDTO.getId() == null) {
            return createBranch(branchDTO);
        }
        BranchDTO result = branchService.save(branchDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, branchDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /branches : get all the branches.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of branches in body
     */
    @GetMapping("/branches")
    @Timed
    public List<BranchDTO> getAllBranches() {
        log.debug("REST request to get all Branches");
        return branchService.findAll();
    }

    /**
     * GET  /branches/:id : get the "id" branch.
     *
     * @param id the id of the branchDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the branchDTO, or with status 404 (Not Found)
     */
    @GetMapping("/branches/{id}")
    @Timed
    public ResponseEntity<BranchDTO> getBranch(@PathVariable Long id) {
        log.debug("REST request to get Branch : {}", id);
        BranchDTO branchDTO = branchService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(branchDTO));
    }

    /**
     * DELETE  /branches/:id : delete the "id" branch.
     *
     * @param id the id of the branchDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/branches/{id}")
    @Timed
    public ResponseEntity<Void> deleteBranch(@PathVariable Long id) {
        log.debug("REST request to delete Branch : {}", id);
        branchService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
