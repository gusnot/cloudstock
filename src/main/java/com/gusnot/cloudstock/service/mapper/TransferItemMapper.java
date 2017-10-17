package com.gusnot.cloudstock.service.mapper;

import com.gusnot.cloudstock.domain.*;
import com.gusnot.cloudstock.service.dto.TransferItemDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity TransferItem and its DTO TransferItemDTO.
 */
@Mapper(componentModel = "spring", uses = {SkuMapper.class, TransferMapper.class, })
public interface TransferItemMapper extends EntityMapper <TransferItemDTO, TransferItem> {

    @Mapping(source = "sku.id", target = "skuId")
    @Mapping(source = "sku.code", target = "skuCode")

    @Mapping(source = "transfer.id", target = "transferId")
    @Mapping(source = "transfer.refNo", target = "transferRefNo")
    TransferItemDTO toDto(TransferItem transferItem); 

    @Mapping(source = "skuId", target = "sku")

    @Mapping(source = "transferId", target = "transfer")
    TransferItem toEntity(TransferItemDTO transferItemDTO); 
    default TransferItem fromId(Long id) {
        if (id == null) {
            return null;
        }
        TransferItem transferItem = new TransferItem();
        transferItem.setId(id);
        return transferItem;
    }
}
