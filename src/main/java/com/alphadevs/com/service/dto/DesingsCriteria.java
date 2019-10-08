package com.alphadevs.com.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.alphadevs.com.domain.Desings} entity. This class is used
 * in {@link com.alphadevs.com.web.rest.DesingsResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /desings?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class DesingsCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter desingCode;

    private StringFilter desingName;

    private StringFilter desingPrefix;

    private DoubleFilter desingProfMargin;

    private LongFilter relatedProductId;

    private LongFilter locationId;

    public DesingsCriteria(){
    }

    public DesingsCriteria(DesingsCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.desingCode = other.desingCode == null ? null : other.desingCode.copy();
        this.desingName = other.desingName == null ? null : other.desingName.copy();
        this.desingPrefix = other.desingPrefix == null ? null : other.desingPrefix.copy();
        this.desingProfMargin = other.desingProfMargin == null ? null : other.desingProfMargin.copy();
        this.relatedProductId = other.relatedProductId == null ? null : other.relatedProductId.copy();
        this.locationId = other.locationId == null ? null : other.locationId.copy();
    }

    @Override
    public DesingsCriteria copy() {
        return new DesingsCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getDesingCode() {
        return desingCode;
    }

    public void setDesingCode(StringFilter desingCode) {
        this.desingCode = desingCode;
    }

    public StringFilter getDesingName() {
        return desingName;
    }

    public void setDesingName(StringFilter desingName) {
        this.desingName = desingName;
    }

    public StringFilter getDesingPrefix() {
        return desingPrefix;
    }

    public void setDesingPrefix(StringFilter desingPrefix) {
        this.desingPrefix = desingPrefix;
    }

    public DoubleFilter getDesingProfMargin() {
        return desingProfMargin;
    }

    public void setDesingProfMargin(DoubleFilter desingProfMargin) {
        this.desingProfMargin = desingProfMargin;
    }

    public LongFilter getRelatedProductId() {
        return relatedProductId;
    }

    public void setRelatedProductId(LongFilter relatedProductId) {
        this.relatedProductId = relatedProductId;
    }

    public LongFilter getLocationId() {
        return locationId;
    }

    public void setLocationId(LongFilter locationId) {
        this.locationId = locationId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final DesingsCriteria that = (DesingsCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(desingCode, that.desingCode) &&
            Objects.equals(desingName, that.desingName) &&
            Objects.equals(desingPrefix, that.desingPrefix) &&
            Objects.equals(desingProfMargin, that.desingProfMargin) &&
            Objects.equals(relatedProductId, that.relatedProductId) &&
            Objects.equals(locationId, that.locationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        desingCode,
        desingName,
        desingPrefix,
        desingProfMargin,
        relatedProductId,
        locationId
        );
    }

    @Override
    public String toString() {
        return "DesingsCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (desingCode != null ? "desingCode=" + desingCode + ", " : "") +
                (desingName != null ? "desingName=" + desingName + ", " : "") +
                (desingPrefix != null ? "desingPrefix=" + desingPrefix + ", " : "") +
                (desingProfMargin != null ? "desingProfMargin=" + desingProfMargin + ", " : "") +
                (relatedProductId != null ? "relatedProductId=" + relatedProductId + ", " : "") +
                (locationId != null ? "locationId=" + locationId + ", " : "") +
            "}";
    }

}
