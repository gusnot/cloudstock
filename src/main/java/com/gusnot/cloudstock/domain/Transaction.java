package com.gusnot.cloudstock.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.gusnot.cloudstock.domain.enumeration.TransactionStatus;

import com.gusnot.cloudstock.domain.enumeration.TransactionType;

/**
 * A Transaction.
 */
@Entity
@Table(name = "transaction")
@Document(indexName = "transaction")
public class Transaction implements Serializable {

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
    private TransactionStatus status;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "jhi_type", nullable = false)
    private TransactionType type;

    @Size(max = 500)
    @Column(name = "reason", length = 500)
    private String reason;

    @OneToMany(mappedBy = "transaction")
    @JsonIgnore
    private Set<TransactionItem> transactionItems = new HashSet<>();

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

    public Transaction refNo(String refNo) {
        this.refNo = refNo;
        return this;
    }

    public void setRefNo(String refNo) {
        this.refNo = refNo;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public Transaction status(TransactionStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    public TransactionType getType() {
        return type;
    }

    public Transaction type(TransactionType type) {
        this.type = type;
        return this;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public String getReason() {
        return reason;
    }

    public Transaction reason(String reason) {
        this.reason = reason;
        return this;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Set<TransactionItem> getTransactionItems() {
        return transactionItems;
    }

    public Transaction transactionItems(Set<TransactionItem> transactionItems) {
        this.transactionItems = transactionItems;
        return this;
    }

    public Transaction addTransactionItem(TransactionItem transactionItem) {
        this.transactionItems.add(transactionItem);
        transactionItem.setTransaction(this);
        return this;
    }

    public Transaction removeTransactionItem(TransactionItem transactionItem) {
        this.transactionItems.remove(transactionItem);
        transactionItem.setTransaction(null);
        return this;
    }

    public void setTransactionItems(Set<TransactionItem> transactionItems) {
        this.transactionItems = transactionItems;
    }

    public Branch getBranch() {
        return branch;
    }

    public Transaction branch(Branch branch) {
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
        Transaction transaction = (Transaction) o;
        if (transaction.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), transaction.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Transaction{" +
            "id=" + getId() +
            ", refNo='" + getRefNo() + "'" +
            ", status='" + getStatus() + "'" +
            ", type='" + getType() + "'" +
            ", reason='" + getReason() + "'" +
            "}";
    }
}
