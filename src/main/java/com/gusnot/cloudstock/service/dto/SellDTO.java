package com.gusnot.cloudstock.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import com.gusnot.cloudstock.domain.enumeration.SellStatus;

/**
 * A DTO for the Sell entity.
 */
public class SellDTO implements Serializable {

    private Long id;

    @NotNull
    private String refNo;

    @NotNull
    private SellStatus status;

    private Long branchId;

    private String branchName;

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

    public SellStatus getStatus() {
        return status;
    }

    public void setStatus(SellStatus status) {
        this.status = status;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SellDTO sellDTO = (SellDTO) o;
        if(sellDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), sellDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SellDTO{" +
            "id=" + getId() +
            ", refNo='" + getRefNo() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
