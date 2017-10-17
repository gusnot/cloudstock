package com.gusnot.cloudstock.domain;

import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A BillItem.
 */
@Entity
@Table(name = "bill_item")
@Document(indexName = "billitem")
public class BillItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "sell_price", precision=10, scale=2, nullable = false)
    private BigDecimal sellPrice;

    @Column(name = "sell_cost", precision=10, scale=2)
    private BigDecimal sellCost;

    @ManyToOne
    private Sku sku;

    @ManyToOne
    private Bill bill;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getSellPrice() {
        return sellPrice;
    }

    public BillItem sellPrice(BigDecimal sellPrice) {
        this.sellPrice = sellPrice;
        return this;
    }

    public void setSellPrice(BigDecimal sellPrice) {
        this.sellPrice = sellPrice;
    }

    public BigDecimal getSellCost() {
        return sellCost;
    }

    public BillItem sellCost(BigDecimal sellCost) {
        this.sellCost = sellCost;
        return this;
    }

    public void setSellCost(BigDecimal sellCost) {
        this.sellCost = sellCost;
    }

    public Sku getSku() {
        return sku;
    }

    public BillItem sku(Sku sku) {
        this.sku = sku;
        return this;
    }

    public void setSku(Sku sku) {
        this.sku = sku;
    }

    public Bill getBill() {
        return bill;
    }

    public BillItem bill(Bill bill) {
        this.bill = bill;
        return this;
    }

    public void setBill(Bill bill) {
        this.bill = bill;
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
        BillItem billItem = (BillItem) o;
        if (billItem.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), billItem.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "BillItem{" +
            "id=" + getId() +
            ", sellPrice='" + getSellPrice() + "'" +
            ", sellCost='" + getSellCost() + "'" +
            "}";
    }
}
