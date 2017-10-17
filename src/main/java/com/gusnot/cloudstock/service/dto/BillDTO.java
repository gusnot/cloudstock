package com.gusnot.cloudstock.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import com.gusnot.cloudstock.domain.enumeration.BillStatus;

/**
 * A DTO for the Bill entity.
 */
public class BillDTO implements Serializable {

    private Long id;

    @NotNull
    private String refNo;

    @NotNull
    private BillStatus type;

    private Long branchId;

    private String branchName;

    private Long sellId;

    private String sellRefNo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRefNo() {
        return refNo;
    }

    public void setRefNo(String refNo) {
        this.refNo = refNo;
    }

    public BillStatus getType() {
        return type;
    }

    public void setType(BillStatus type) {
        this.type = type;
    }

    public Long getBranchId() {
        return branchId;
    }

    public void setBranchId(Long branchId) {
        this.branchId = branchId;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public Long getSellId() {
        return sellId;
    }

    public void setSellId(Long sellId) {
        this.sellId = sellId;
    }

    public String getSellRefNo() {
        return sellRefNo;
    }

    public void setSellRefNo(String sellRefNo) {
        this.sellRefNo = sellRefNo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BillDTO billDTO = (BillDTO) o;
        if(billDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), billDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "BillDTO{" +
            "id=" + getId() +
            ", refNo='" + getRefNo() + "'" +
            ", type='" + getType() + "'" +
            "}";
    }
}
