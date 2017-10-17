package com.gusnot.cloudstock.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.gusnot.cloudstock.domain.enumeration.SellStatus;

/**
 * A Sell.
 */
@Entity
@Table(name = "sell")
@Document(indexName = "sell")
public class Sell implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "ref_no", nullable = false)
    private String refNo;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private SellStatus status;

    @OneToMany(mappedBy = "sell")
    @JsonIgnore
    private Set<Bill> bills = new HashSet<>();

    @ManyToOne
    private Branch branch;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRefNo() {
        return refNo;
    }

    public Sell refNo(String refNo) {
        this.refNo = refNo;
        return this;
    }

    public void setRefNo(String refNo) {
        this.refNo = refNo;
    }

    public SellStatus getStatus() {
        return status;
    }

    public Sell status(SellStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(SellStatus status) {
        this.status = status;
    }

    public Set<Bill> getBills() {
        return bills;
    }

    public Sell bills(Set<Bill> bills) {
        this.bills = bills;
        return this;
    }

    public Sell addBill(Bill bill) {
        this.bills.add(bill);
        bill.setSell(this);
        return this;
    }

    public Sell removeBill(Bill bill) {
        this.bills.remove(bill);
        bill.setSell(null);
        return this;
    }

    public void setBills(Set<Bill> bills) {
        this.bills = bills;
    }

    public Branch getBranch() {
        return branch;
    }

    public Sell branch(Branch branch) {
        this.branch = branch;
        return this;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Sell sell = (Sell) o;
        if (sell.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), sell.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Sell{" +
            "id=" + getId() +
            ", refNo='" + getRefNo() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
