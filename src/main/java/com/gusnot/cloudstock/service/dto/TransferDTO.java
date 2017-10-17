package com.gusnot.cloudstock.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import com.gusnot.cloudstock.domain.enumeration.TransactionStatus;

/**
 * A DTO for the Transfer entity.
 */
public class TransferDTO implements Serializable {

    private Long id;

    @NotNull
    private String refNo;

    @NotNull
    private TransactionStatus status;

    private Long srcBranchId;

    private String srcBranchName;

    private Long destBranchId;

    private String destBranchName;

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

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    public Long getSrcBranchId() {
        return srcBranchId;
    }

    public void setSrcBranchId(Long branchId) {
        this.srcBranchId = branchId;
    }

    public String getSrcBranchName() {
        return srcBranchName;
    }

    public void setSrcBranchName(String branchName) {
        this.srcBranchName = branchName;
    }

    public Long getDestBranchId() {
        return destBranchId;
    }

    public void setDestBranchId(Long branchId) {
        this.destBranchId = branchId;
    }

    public String getDestBranchName() {
        return destBranchName;
    }

    public void setDestBranchName(String branchName) {
        this.destBranchName = branchName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TransferDTO transferDTO = (TransferDTO) o;
        if(transferDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), transferDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TransferDTO{" +
            "id=" + getId() +
            ", refNo='" + getRefNo() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
