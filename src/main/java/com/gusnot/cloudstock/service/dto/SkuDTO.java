package com.gusnot.cloudstock.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the Sku entity.
 */
public class SkuDTO implements Serializable {

    private Long id;

    @NotNull
    private BigDecimal price;

    private BigDecimal cost;

    @NotNull
    private String code;

    private String barcode;

    private Long productId;

    private String productName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SkuDTO skuDTO = (SkuDTO) o;
        if(skuDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), skuDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SkuDTO{" +
            "id=" + getId() +
            ", price='" + getPrice() + "'" +
            ", cost='" + getCost() + "'" +
            ", code='" + getCode() + "'" +
            ", barcode='" + getBarcode() + "'" +
            "}";
    }
}
