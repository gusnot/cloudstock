package com.gusnot.cloudstock.service.mapper;

import com.gusnot.cloudstock.domain.*;
import com.gusnot.cloudstock.service.dto.StockDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Stock and its DTO StockDTO.
 */
@Mapper(componentModel = "spring", uses = {BranchMapper.class, SkuMapper.class, })
public interface StockMapper extends EntityMapper <StockDTO, Stock> {

    @Mapping(source = "branch.id", target = "branchId")
    @Mapping(source = "branch.name", target = "branchName")

    @Mapping(source = "sku.id", target = "skuId")
    @Mapping(source = "sku.code", target = "skuCode")
    StockDTO toDto(Stock stock); 

    @Mapping(source = "branchId", target = "branch")

    @Mapping(source = "skuId", target = "sku")
    Stock toEntity(StockDTO stockDTO); 
    default Stock fromId(Long id) {
        if (id == null) {
            return null;
        }
        Stock stock = new Stock();
        stock.setId(id);
        return stock;
    }
}
