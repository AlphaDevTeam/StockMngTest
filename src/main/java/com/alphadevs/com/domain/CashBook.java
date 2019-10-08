package com.alphadevs.com.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * CashBook Entity.
 * @author Mihindu Karunarathne.
 */
@ApiModel(description = "CashBook Entity. @author Mihindu Karunarathne.")
@Entity
@Table(name = "cash_book")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class CashBook implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "cashbook_date", nullable = false)
    private LocalDate cashbookDate;

    @NotNull
    @Column(name = "cashbook_description", nullable = false)
    private String cashbookDescription;

    @NotNull
    @Column(name = "cashbook_amount_cr", nullable = false)
    private Double cashbookAmountCR;

    @NotNull
    @Column(name = "cashbook_amount_dr", nullable = false)
    private Double cashbookAmountDR;

    @NotNull
    @Column(name = "cashbook_balance", nullable = false)
    private Double cashbookBalance;

    @ManyToOne
    @JsonIgnoreProperties("cashBooks")
    private Location location;

    @ManyToOne
    @JsonIgnoreProperties("cashBooks")
    private DocumentType documentType;

    @ManyToOne
    @JsonIgnoreProperties("cashBooks")
    private Items item;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getCashbookDate() {
        return cashbookDate;
    }

    public CashBook cashbookDate(LocalDate cashbookDate) {
        this.cashbookDate = cashbookDate;
        return this;
    }

    public void setCashbookDate(LocalDate cashbookDate) {
        this.cashbookDate = cashbookDate;
    }

    public String getCashbookDescription() {
        return cashbookDescription;
    }

    public CashBook cashbookDescription(String cashbookDescription) {
        this.cashbookDescription = cashbookDescription;
        return this;
    }

    public void setCashbookDescription(String cashbookDescription) {
        this.cashbookDescription = cashbookDescription;
    }

    public Double getCashbookAmountCR() {
        return cashbookAmountCR;
    }

    public CashBook cashbookAmountCR(Double cashbookAmountCR) {
        this.cashbookAmountCR = cashbookAmountCR;
        return this;
    }

    public void setCashbookAmountCR(Double cashbookAmountCR) {
        this.cashbookAmountCR = cashbookAmountCR;
    }

    public Double getCashbookAmountDR() {
        return cashbookAmountDR;
    }

    public CashBook cashbookAmountDR(Double cashbookAmountDR) {
        this.cashbookAmountDR = cashbookAmountDR;
        return this;
    }

    public void setCashbookAmountDR(Double cashbookAmountDR) {
        this.cashbookAmountDR = cashbookAmountDR;
    }

    public Double getCashbookBalance() {
        return cashbookBalance;
    }

    public CashBook cashbookBalance(Double cashbookBalance) {
        this.cashbookBalance = cashbookBalance;
        return this;
    }

    public void setCashbookBalance(Double cashbookBalance) {
        this.cashbookBalance = cashbookBalance;
    }

    public Location getLocation() {
        return location;
    }

    public CashBook location(Location location) {
        this.location = location;
        return this;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public DocumentType getDocumentType() {
        return documentType;
    }

    public CashBook documentType(DocumentType documentType) {
        this.documentType = documentType;
        return this;
    }

    public void setDocumentType(DocumentType documentType) {
        this.documentType = documentType;
    }

    public Items getItem() {
        return item;
    }

    public CashBook item(Items items) {
        this.item = items;
        return this;
    }

    public void setItem(Items items) {
        this.item = items;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CashBook)) {
            return false;
        }
        return id != null && id.equals(((CashBook) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "CashBook{" +
            "id=" + getId() +
            ", cashbookDate='" + getCashbookDate() + "'" +
            ", cashbookDescription='" + getCashbookDescription() + "'" +
            ", cashbookAmountCR=" + getCashbookAmountCR() +
            ", cashbookAmountDR=" + getCashbookAmountDR() +
            ", cashbookBalance=" + getCashbookBalance() +
            "}";
    }
}
