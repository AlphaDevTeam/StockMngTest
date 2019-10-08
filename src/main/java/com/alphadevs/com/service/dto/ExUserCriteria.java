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
 * Criteria class for the {@link com.alphadevs.com.domain.ExUser} entity. This class is used
 * in {@link com.alphadevs.com.web.rest.ExUserResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /ex-users?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ExUserCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter userKey;

    private LongFilter relatedUserId;

    private LongFilter companyId;

    public ExUserCriteria(){
    }

    public ExUserCriteria(ExUserCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.userKey = other.userKey == null ? null : other.userKey.copy();
        this.relatedUserId = other.relatedUserId == null ? null : other.relatedUserId.copy();
        this.companyId = other.companyId == null ? null : other.companyId.copy();
    }

    @Override
    public ExUserCriteria copy() {
        return new ExUserCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getUserKey() {
        return userKey;
    }

    public void setUserKey(StringFilter userKey) {
        this.userKey = userKey;
    }

    public LongFilter getRelatedUserId() {
        return relatedUserId;
    }

    public void setRelatedUserId(LongFilter relatedUserId) {
        this.relatedUserId = relatedUserId;
    }

    public LongFilter getCompanyId() {
        return companyId;
    }

    public void setCompanyId(LongFilter companyId) {
        this.companyId = companyId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ExUserCriteria that = (ExUserCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(userKey, that.userKey) &&
            Objects.equals(relatedUserId, that.relatedUserId) &&
            Objects.equals(companyId, that.companyId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        userKey,
        relatedUserId,
        companyId
        );
    }

    @Override
    public String toString() {
        return "ExUserCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (userKey != null ? "userKey=" + userKey + ", " : "") +
                (relatedUserId != null ? "relatedUserId=" + relatedUserId + ", " : "") +
                (companyId != null ? "companyId=" + companyId + ", " : "") +
            "}";
    }

}
