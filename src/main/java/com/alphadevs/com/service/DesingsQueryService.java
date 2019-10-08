package com.alphadevs.com.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.alphadevs.com.domain.Desings;
import com.alphadevs.com.domain.*; // for static metamodels
import com.alphadevs.com.repository.DesingsRepository;
import com.alphadevs.com.service.dto.DesingsCriteria;

/**
 * Service for executing complex queries for {@link Desings} entities in the database.
 * The main input is a {@link DesingsCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Desings} or a {@link Page} of {@link Desings} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DesingsQueryService extends QueryService<Desings> {

    private final Logger log = LoggerFactory.getLogger(DesingsQueryService.class);

    private final DesingsRepository desingsRepository;

    public DesingsQueryService(DesingsRepository desingsRepository) {
        this.desingsRepository = desingsRepository;
    }

    /**
     * Return a {@link List} of {@link Desings} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Desings> findByCriteria(DesingsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Desings> specification = createSpecification(criteria);
        return desingsRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Desings} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Desings> findByCriteria(DesingsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Desings> specification = createSpecification(criteria);
        return desingsRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DesingsCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Desings> specification = createSpecification(criteria);
        return desingsRepository.count(specification);
    }

    /**
     * Function to convert ConsumerCriteria to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */    
    protected Specification<Desings> createSpecification(DesingsCriteria criteria) {
        Specification<Desings> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Desings_.id));
            }
            if (criteria.getDesingCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDesingCode(), Desings_.desingCode));
            }
            if (criteria.getDesingName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDesingName(), Desings_.desingName));
            }
            if (criteria.getDesingPrefix() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDesingPrefix(), Desings_.desingPrefix));
            }
            if (criteria.getDesingProfMargin() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDesingProfMargin(), Desings_.desingProfMargin));
            }
            if (criteria.getRelatedProductId() != null) {
                specification = specification.and(buildSpecification(criteria.getRelatedProductId(),
                    root -> root.join(Desings_.relatedProduct, JoinType.LEFT).get(Products_.id)));
            }
            if (criteria.getLocationId() != null) {
                specification = specification.and(buildSpecification(criteria.getLocationId(),
                    root -> root.join(Desings_.location, JoinType.LEFT).get(Location_.id)));
            }
        }
        return specification;
    }
}
