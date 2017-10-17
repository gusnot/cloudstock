package com.gusnot.cloudstock.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the TransferItem entity.
 */
public class TransferItemDTO implements Serializable {

    private Long id;

    @NotNull
    private BigDecimal amount;

    private Long skuId;

    private String skuCode;

    private Long transferId;

    private String transferRefNo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
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

    public Long getTransferId() {
        return transferId;
    }

    public void setTransferId(Long transferId) {
        this.transferId = transferId;
    }

    public String getTransferRefNo() {
        return transferRefNo;
    }

    public void setTransferRefNo(String transferRefNo) {
        this.transferRefNo = transferRefNo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TransferItemDTO transferItemDTO = (TransferItemDTO) o;
        if(transferItemDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), transferItemDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TransferItemDTO{" +
            "id=" + getId() +
            ", amount='" + getAmount() + "'" +
            "}";
    }
}
