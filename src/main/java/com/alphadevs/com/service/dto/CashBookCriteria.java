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
import io.github.jhipster.service.filter.LocalDateFilter;

/**
 * Criteria class for the {@link com.alphadevs.com.domain.CashBook} entity. This class is used
 * in {@link com.alphadevs.com.web.rest.CashBookResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /cash-books?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CashBookCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter cashbookDate;

    private StringFilter cashbookDescription;

    private DoubleFilter cashbookAmountCR;

    private DoubleFilter cashbookAmountDR;

    private DoubleFilter cashbookBalance;

    private LongFilter locationId;

    private LongFilter documentTypeId;

    private LongFilter itemId;

    public CashBookCriteria(){
    }

    public CashBookCriteria(CashBookCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.cashbookDate = other.cashbookDate == null ? null : other.cashbookDate.copy();
        this.cashbookDescription = other.cashbookDescription == null ? null : other.cashbookDescription.copy();
        this.cashbookAmountCR = other.cashbookAmountCR == null ? null : other.cashbookAmountCR.copy();
        this.cashbookAmountDR = other.cashbookAmountDR == null ? null : other.cashbookAmountDR.copy();
        this.cashbookBalance = other.cashbookBalance == null ? null : other.cashbookBalance.copy();
        this.locationId = other.locationId == null ? null : other.locationId.copy();
        this.documentTypeId = other.documentTypeId == null ? null : other.documentTypeId.copy();
        this.itemId = other.itemId == null ? null : other.itemId.copy();
    }

    @Override
    public CashBookCriteria copy() {
        return new CashBookCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LocalDateFilter getCashbookDate() {
        return cashbookDate;
    }

    public void setCashbookDate(LocalDateFilter cashbookDate) {
        this.cashbookDate = cashbookDate;
    }

    public StringFilter getCashbookDescription() {
        return cashbookDescription;
    }

    public void setCashbookDescription(StringFilter cashbookDescription) {
        this.cashbookDescription = cashbookDescription;
    }

    public DoubleFilter getCashbookAmountCR() {
        return cashbookAmountCR;
    }

    public void setCashbookAmountCR(DoubleFilter cashbookAmountCR) {
        this.cashbookAmountCR = cashbookAmountCR;
    }

    public DoubleFilter getCashbookAmountDR() {
        return cashbookAmountDR;
    }

    public void setCashbookAmountDR(DoubleFilter cashbookAmountDR) {
        this.cashbookAmountDR = cashbookAmountDR;
    }

    public DoubleFilter getCashbookBalance() {
        return cashbookBalance;
    }

    public void setCashbookBalance(DoubleFilter cashbookBalance) {
        this.cashbookBalance = cashbookBalance;
    }

    public LongFilter getLocationId() {
        return locationId;
    }

    public void setLocationId(LongFilter locationId) {
        this.locationId = locationId;
    }

    public LongFilter getDocumentTypeId() {
        return documentTypeId;
    }

    public void setDocumentTypeId(LongFilter documentTypeId) {
        this.documentTypeId = documentTypeId;
    }

    public LongFilter getItemId() {
        return itemId;
    }

    public void setItemId(LongFilter itemId) {
        this.itemId = itemId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CashBookCriteria that = (CashBookCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(cashbookDate, that.cashbookDate) &&
            Objects.equals(cashbookDescription, that.cashbookDescription) &&
            Objects.equals(cashbookAmountCR, that.cashbookAmountCR) &&
            Objects.equals(cashbookAmountDR, that.cashbookAmountDR) &&
            Objects.equals(cashbookBalance, that.cashbookBalance) &&
            Objects.equals(locationId, that.locationId) &&
            Objects.equals(documentTypeId, that.documentTypeId) &&
            Objects.equals(itemId, that.itemId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        cashbookDate,
        cashbookDescription,
        cashbookAmountCR,
        cashbookAmountDR,
        cashbookBalance,
        locationId,
        documentTypeId,
        itemId
        );
    }

    @Override
    public String toString() {
        return "CashBookCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (cashbookDate != null ? "cashbookDate=" + cashbookDate + ", " : "") +
                (cashbookDescription != null ? "cashbookDescription=" + cashbookDescription + ", " : "") +
                (cashbookAmountCR != null ? "cashbookAmountCR=" + cashbookAmountCR + ", " : "") +
                (cashbookAmountDR != null ? "cashbookAmountDR=" + cashbookAmountDR + ", " : "") +
                (cashbookBalance != null ? "cashbookBalance=" + cashbookBalance + ", " : "") +
                (locationId != null ? "locationId=" + locationId + ", " : "") +
                (documentTypeId != null ? "documentTypeId=" + documentTypeId + ", " : "") +
                (itemId != null ? "itemId=" + itemId + ", " : "") +
            "}";
    }

}
