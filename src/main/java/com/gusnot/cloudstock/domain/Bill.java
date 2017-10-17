package com.gusnot.cloudstock.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.gusnot.cloudstock.domain.enumeration.BillStatus;

/**
 * A Bill.
 */
@Entity
@Table(name = "bill")
@Document(indexName = "bill")
public class Bill implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "ref_no", nullable = false)
    private String refNo;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "jhi_type", nullable = false)
    private BillStatus type;

    @OneToMany(mappedBy = "bill")
    @JsonIgnore
    private Set<BillItem> billItems = new HashSet<>();

    @ManyToOne
    private Branch branch;

    @ManyToOne
    private Sell sell;

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

    public Bill refNo(String refNo) {
        this.refNo = refNo;
        return this;
    }

    public void setRefNo(String refNo) {
        this.refNo = refNo;
    }

    public BillStatus getType() {
        return type;
    }

    public Bill type(BillStatus type) {
        this.type = type;
        return this;
    }

    public void setType(BillStatus type) {
        this.type = type;
    }

    public Set<BillItem> getBillItems() {
        return billItems;
    }

    public Bill billItems(Set<BillItem> billItems) {
        this.billItems = billItems;
        return this;
    }

    public Bill addBillItem(BillItem billItem) {
        this.billItems.add(billItem);
        billItem.setBill(this);
        return this;
    }

    public Bill removeBillItem(BillItem billItem) {
        this.billItems.remove(billItem);
        billItem.setBill(null);
        return this;
    }

    public void setBillItems(Set<BillItem> billItems) {
        this.billItems = billItems;
    }

    public Branch getBranch() {
        return branch;
    }

    public Bill branch(Branch branch) {
        this.branch = branch;
        return this;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    public Sell getSell() {
        return sell;
    }

    public Bill sell(Sell sell) {
        this.sell = sell;
        return this;
    }

    public void setSell(Sell sell) {
        this.sell = sell;
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
        Bill bill = (Bill) o;
        if (bill.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), bill.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Bill{" +
            "id=" + getId() +
            ", refNo='" + getRefNo() + "'" +
            ", type='" + getType() + "'" +
            "}";
    }
}
