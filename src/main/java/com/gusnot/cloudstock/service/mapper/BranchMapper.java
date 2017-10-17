package com.gusnot.cloudstock.service.mapper;

import com.gusnot.cloudstock.domain.*;
import com.gusnot.cloudstock.service.dto.BranchDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Branch and its DTO BranchDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface BranchMapper extends EntityMapper <BranchDTO, Branch> {
    
    
    default Branch fromId(Long id) {
        if (id == null) {
            return null;
        }
        Branch branch = new Branch();
        branch.setId(id);
        return branch;
    }
}
