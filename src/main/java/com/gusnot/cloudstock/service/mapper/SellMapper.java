package com.gusnot.cloudstock.service.mapper;

import com.gusnot.cloudstock.domain.*;
import com.gusnot.cloudstock.service.dto.SellDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Sell and its DTO SellDTO.
 */
@Mapper(componentModel = "spring", uses = {BranchMapper.class, })
public interface SellMapper extends EntityMapper <SellDTO, Sell> {

    @Mapping(source = "branch.id", target = "branchId")
    @Mapping(source = "branch.name", target = "branchName")
    SellDTO toDto(Sell sell); 
    @Mapping(target = "bills", ignore = true)

    @Mapping(source = "branchId", target = "branch")
    Sell toEntity(SellDTO sellDTO); 
    default Sell fromId(Long id) {
        if (id == null) {
            return null;
        }
        Sell sell = new Sell();
        sell.setId(id);
        return sell;
    }
}
