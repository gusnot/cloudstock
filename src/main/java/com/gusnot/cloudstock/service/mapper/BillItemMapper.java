package com.gusnot.cloudstock.service.mapper;

import com.gusnot.cloudstock.domain.*;
import com.gusnot.cloudstock.service.dto.BillItemDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity BillItem and its DTO BillItemDTO.
 */
@Mapper(componentModel = "spring", uses = {SkuMapper.class, BillMapper.class, })
public interface BillItemMapper extends EntityMapper <BillItemDTO, BillItem> {

    @Mapping(source = "sku.id", target = "skuId")
    @Mapping(source = "sku.code", target = "skuCode")

    @Mapping(source = "bill.id", target = "billId")
    @Mapping(source = "bill.refNo", target = "billRefNo")
    BillItemDTO toDto(BillItem billItem); 

    @Mapping(source = "skuId", target = "sku")

    @Mapping(source = "billId", target = "bill")
    BillItem toEntity(BillItemDTO billItemDTO); 
    default BillItem fromId(Long id) {
        if (id == null) {
            return null;
        }
        BillItem billItem = new BillItem();
        billItem.setId(id);
        return billItem;
    }
}
