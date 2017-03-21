package com.gusnot.cloudstock.service.mapper;

import com.gusnot.cloudstock.domain.*;
import com.gusnot.cloudstock.service.dto.BranchDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Branch and its DTO BranchDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface BranchMapper {

    BranchDTO branchToBranchDTO(Branch branch);

    List<BranchDTO> branchesToBranchDTOs(List<Branch> branches);

    Branch branchDTOToBranch(BranchDTO branchDTO);

    List<Branch> branchDTOsToBranches(List<BranchDTO> branchDTOs);
}
