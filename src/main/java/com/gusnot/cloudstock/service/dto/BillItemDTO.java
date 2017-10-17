package com.gusnot.cloudstock.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the BillItem entity.
 */
public class BillItemDTO implements Serializable {

    private Long id;

    @NotNull
    private BigDecimal sellPrice;

    private BigDecimal sellCost;

    private Long skuId;

    private String skuCode;

    private Long billId;

    private String billRefNo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(BigDecimal sellPrice) {
        this.sellPrice = sellPrice;
    }

    public BigDecimal getSellCost() {
        return sellCost;
    }

    public void setSellCost(BigDecimal sellCost) {
        this.sellCost = sellCost;
    }

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    public Long getBillId() {
        return billId;
    }

    public void setBillId(Long billId) {
        this.billId = billId;
    }

    public String getBillRefNo() {
        return billRefNo;
    }

    public void setBillRefNo(String billRefNo) {
        this.billRefNo = billRefNo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BillItemDTO billItemDTO = (BillItemDTO) o;
        if(billItemDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), billItemDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "BillItemDTO{" +
            "id=" + getId() +
            ", sellPrice='" + getSellPrice() + "'" +
            ", sellCost='" + getSellCost() + "'" +
            "}";
    }
}
