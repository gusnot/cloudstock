package com.gusnot.cloudstock.service.mapper;

import com.gusnot.cloudstock.domain.*;
import com.gusnot.cloudstock.service.dto.BillDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Bill and its DTO BillDTO.
 */
@Mapper(componentModel = "spring", uses = {BranchMapper.class, SellMapper.class, })
public interface BillMapper extends EntityMapper <BillDTO, Bill> {

    @Mapping(source = "branch.id", target = "branchId")
    @Mapping(source = "branch.name", target = "branchName")

    @Mapping(source = "sell.id", target = "sellId")
    @Mapping(source = "sell.refNo", target = "sellRefNo")
    BillDTO toDto(Bill bill); 
    @Mapping(target = "billItems", ignore = true)

    @Mapping(source = "branchId", target = "branch")

    @Mapping(source = "sellId", target = "sell")
    Bill toEntity(BillDTO billDTO); 
    default Bill fromId(Long id) {
        if (id == null) {
            return null;
        }
        Bill bill = new Bill();
        bill.setId(id);
        return bill;
    }
}
