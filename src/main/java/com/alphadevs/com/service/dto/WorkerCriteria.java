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
 * Criteria class for the {@link com.alphadevs.com.domain.Worker} entity. This class is used
 * in {@link com.alphadevs.com.web.rest.WorkerResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /workers?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class WorkerCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter workerCode;

    private StringFilter workerName;

    private DoubleFilter workerLimit;

    private BooleanFilter isActive;

    private LongFilter jobId;

    private LongFilter locationId;

    public WorkerCriteria(){
    }

    public WorkerCriteria(WorkerCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.workerCode = other.workerCode == null ? null : other.workerCode.copy();
        this.workerName = other.workerName == null ? null : other.workerName.copy();
        this.workerLimit = other.workerLimit == null ? null : other.workerLimit.copy();
        this.isActive = other.isActive == null ? null : other.isActive.copy();
        this.jobId = other.jobId == null ? null : other.jobId.copy();
        this.locationId = other.locationId == null ? null : other.locationId.copy();
    }

    @Override
    public WorkerCriteria copy() {
        return new WorkerCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getWorkerCode() {
        return workerCode;
    }

    public void setWorkerCode(StringFilter workerCode) {
        this.workerCode = workerCode;
    }

    public StringFilter getWorkerName() {
        return workerName;
    }

    public void setWorkerName(StringFilter workerName) {
        this.workerName = workerName;
    }

    public DoubleFilter getWorkerLimit() {
        return workerLimit;
    }

    public void setWorkerLimit(DoubleFilter workerLimit) {
        this.workerLimit = workerLimit;
    }

    public BooleanFilter getIsActive() {
        return isActive;
    }

    public void setIsActive(BooleanFilter isActive) {
        this.isActive = isActive;
    }

    public LongFilter getJobId() {
        return jobId;
    }

    public void setJobId(LongFilter jobId) {
        this.jobId = jobId;
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
        final WorkerCriteria that = (WorkerCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(workerCode, that.workerCode) &&
            Objects.equals(workerName, that.workerName) &&
            Objects.equals(workerLimit, that.workerLimit) &&
            Objects.equals(isActive, that.isActive) &&
            Objects.equals(jobId, that.jobId) &&
            Objects.equals(locationId, that.locationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        workerCode,
        workerName,
        workerLimit,
        isActive,
        jobId,
        locationId
        );
    }

    @Override
    public String toString() {
        return "WorkerCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (workerCode != null ? "workerCode=" + workerCode + ", " : "") +
                (workerName != null ? "workerName=" + workerName + ", " : "") +
                (workerLimit != null ? "workerLimit=" + workerLimit + ", " : "") +
                (isActive != null ? "isActive=" + isActive + ", " : "") +
                (jobId != null ? "jobId=" + jobId + ", " : "") +
                (locationId != null ? "locationId=" + locationId + ", " : "") +
            "}";
    }

}
