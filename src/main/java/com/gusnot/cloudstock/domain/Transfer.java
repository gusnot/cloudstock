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

/**
 * A Transfer.
 */
@Entity
@Table(name = "transfer")
@Document(indexName = "transfer")
public class Transfer implements Serializable {

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

    @OneToMany(mappedBy = "transfer")
    @JsonIgnore
    private Set<TransferItem> transferItems = new HashSet<>();

    @ManyToOne
    private Branch srcBranch;

    @ManyToOne
    private Branch destBranch;

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

    public Transfer refNo(String refNo) {
        this.refNo = refNo;
        return this;
    }

    public void setRefNo(String refNo) {
        this.refNo = refNo;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public Transfer status(TransactionStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    public Set<TransferItem> getTransferItems() {
        return transferItems;
    }

    public Transfer transferItems(Set<TransferItem> transferItems) {
        this.transferItems = transferItems;
        return this;
    }

    public Transfer addTransferItem(TransferItem transferItem) {
        this.transferItems.add(transferItem);
        transferItem.setTransfer(this);
        return this;
    }

    public Transfer removeTransferItem(TransferItem transferItem) {
        this.transferItems.remove(transferItem);
        transferItem.setTransfer(null);
        return this;
    }

    public void setTransferItems(Set<TransferItem> transferItems) {
        this.transferItems = transferItems;
    }

    public Branch getSrcBranch() {
        return srcBranch;
    }

    public Transfer srcBranch(Branch branch) {
        this.srcBranch = branch;
        return this;
    }

    public void setSrcBranch(Branch branch) {
        this.srcBranch = branch;
    }

    public Branch getDestBranch() {
        return destBranch;
    }

    public Transfer destBranch(Branch branch) {
        this.destBranch = branch;
        return this;
    }

    public void setDestBranch(Branch branch) {
        this.destBranch = branch;
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
        Transfer transfer = (Transfer) o;
        if (transfer.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), transfer.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Transfer{" +
            "id=" + getId() +
            ", refNo='" + getRefNo() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
