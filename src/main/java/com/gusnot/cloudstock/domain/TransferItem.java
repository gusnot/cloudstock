package com.gusnot.cloudstock.domain;

import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A TransferItem.
 */
@Entity
@Table(name = "transfer_item")
@Document(indexName = "transferitem")
public class TransferItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "amount", precision=10, scale=2, nullable = false)
    private BigDecimal amount;

    @ManyToOne
    private Sku sku;

    @ManyToOne
    private Transfer transfer;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public TransferItem amount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Sku getSku() {
        return sku;
    }

    public TransferItem sku(Sku sku) {
        this.sku = sku;
        return this;
    }

    public void setSku(Sku sku) {
        this.sku = sku;
    }

    public Transfer getTransfer() {
        return transfer;
    }

    public TransferItem transfer(Transfer transfer) {
        this.transfer = transfer;
        return this;
    }

    public void setTransfer(Transfer transfer) {
        this.transfer = transfer;
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
        TransferItem transferItem = (TransferItem) o;
        if (transferItem.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), transferItem.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TransferItem{" +
            "id=" + getId() +
            ", amount='" + getAmount() + "'" +
            "}";
    }
}
