package com.alphadevs.com.web.rest;

import com.alphadevs.com.domain.JobDetais;
import com.alphadevs.com.service.JobDetaisService;
import com.alphadevs.com.web.rest.errors.BadRequestAlertException;
import com.alphadevs.com.service.dto.JobDetaisCriteria;
import com.alphadevs.com.service.JobDetaisQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.alphadevs.com.domain.JobDetais}.
 */
@RestController
@RequestMapping("/api")
public class JobDetaisResource {

    private final Logger log = LoggerFactory.getLogger(JobDetaisResource.class);

    private static final String ENTITY_NAME = "jobDetais";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final JobDetaisService jobDetaisService;

    private final JobDetaisQueryService jobDetaisQueryService;

    public JobDetaisResource(JobDetaisService jobDetaisService, JobDetaisQueryService jobDetaisQueryService) {
        this.jobDetaisService = jobDetaisService;
        this.jobDetaisQueryService = jobDetaisQueryService;
    }

    /**
     * {@code POST  /job-detais} : Create a new jobDetais.
     *
     * @param jobDetais the jobDetais to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new jobDetais, or with status {@code 400 (Bad Request)} if the jobDetais has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/job-detais")
    public ResponseEntity<JobDetais> createJobDetais(@Valid @RequestBody JobDetais jobDetais) throws URISyntaxException {
        log.debug("REST request to save JobDetais : {}", jobDetais);
        if (jobDetais.getId() != null) {
            throw new BadRequestAlertException("A new jobDetais cannot already have an ID", ENTITY_NAME, "idexists");
        }
        JobDetais result = jobDetaisService.save(jobDetais);
        return ResponseEntity.created(new URI("/api/job-detais/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /job-detais} : Updates an existing jobDetais.
     *
     * @param jobDetais the jobDetais to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated jobDetais,
     * or with status {@code 400 (Bad Request)} if the jobDetais is not valid,
     * or with status {@code 500 (Internal Server Error)} if the jobDetais couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/job-detais")
    public ResponseEntity<JobDetais> updateJobDetais(@Valid @RequestBody JobDetais jobDetais) throws URISyntaxException {
        log.debug("REST request to update JobDetais : {}", jobDetais);
        if (jobDetais.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        JobDetais result = jobDetaisService.save(jobDetais);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, jobDetais.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /job-detais} : get all the jobDetais.
     *

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of jobDetais in body.
     */
    @GetMapping("/job-detais")
    public ResponseEntity<List<JobDetais>> getAllJobDetais(JobDetaisCriteria criteria) {
        log.debug("REST request to get JobDetais by criteria: {}", criteria);
        List<JobDetais> entityList = jobDetaisQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
    * {@code GET  /job-detais/count} : count all the jobDetais.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/job-detais/count")
    public ResponseEntity<Long> countJobDetais(JobDetaisCriteria criteria) {
        log.debug("REST request to count JobDetais by criteria: {}", criteria);
        return ResponseEntity.ok().body(jobDetaisQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /job-detais/:id} : get the "id" jobDetais.
     *
     * @param id the id of the jobDetais to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the jobDetais, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/job-detais/{id}")
    public ResponseEntity<JobDetais> getJobDetais(@PathVariable Long id) {
        log.debug("REST request to get JobDetais : {}", id);
        Optional<JobDetais> jobDetais = jobDetaisService.findOne(id);
        return ResponseUtil.wrapOrNotFound(jobDetais);
    }

    /**
     * {@code DELETE  /job-detais/:id} : delete the "id" jobDetais.
     *
     * @param id the id of the jobDetais to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/job-detais/{id}")
    public ResponseEntity<Void> deleteJobDetais(@PathVariable Long id) {
        log.debug("REST request to delete JobDetais : {}", id);
        jobDetaisService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
