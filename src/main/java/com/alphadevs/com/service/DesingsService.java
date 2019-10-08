package com.alphadevs.com.service;

import com.alphadevs.com.domain.Desings;
import com.alphadevs.com.repository.DesingsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link Desings}.
 */
@Service
@Transactional
public class DesingsService {

    private final Logger log = LoggerFactory.getLogger(DesingsService.class);

    private final DesingsRepository desingsRepository;

    public DesingsService(DesingsRepository desingsRepository) {
        this.desingsRepository = desingsRepository;
    }

    /**
     * Save a desings.
     *
     * @param desings the entity to save.
     * @return the persisted entity.
     */
    public Desings save(Desings desings) {
        log.debug("Request to save Desings : {}", desings);
        return desingsRepository.save(desings);
    }

    /**
     * Get all the desings.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Desings> findAll() {
        log.debug("Request to get all Desings");
        return desingsRepository.findAll();
    }


    /**
     * Get one desings by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Desings> findOne(Long id) {
        log.debug("Request to get Desings : {}", id);
        return desingsRepository.findById(id);
    }

    /**
     * Delete the desings by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Desings : {}", id);
        desingsRepository.deleteById(id);
    }
}
