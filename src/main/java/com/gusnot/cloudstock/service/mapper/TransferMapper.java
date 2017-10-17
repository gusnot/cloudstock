package com.gusnot.cloudstock.service.mapper;

import com.gusnot.cloudstock.domain.*;
import com.gusnot.cloudstock.service.dto.TransferDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Transfer and its DTO TransferDTO.
 */
@Mapper(componentModel = "spring", uses = {BranchMapper.class, })
public interface TransferMapper extends EntityMapper <TransferDTO, Transfer> {

    @Mapping(source = "srcBranch.id", target = "srcBranchId")
    @Mapping(source = "srcBranch.name", target = "srcBranchName")

    @Mapping(source = "destBranch.id", target = "destBranchId")
    @Mapping(source = "destBranch.name", target = "destBranchName")
    TransferDTO toDto(Transfer transfer); 
    @Mapping(target = "transferItems", ignore = true)

    @Mapping(source = "srcBranchId", target = "srcBranch")

    @Mapping(source = "destBranchId", target = "destBranch")
    Transfer toEntity(TransferDTO transferDTO); 
    default Transfer fromId(Long id) {
        if (id == null) {
            return null;
        }
        Transfer transfer = new Transfer();
        transfer.setId(id);
        return transfer;
    }
}
