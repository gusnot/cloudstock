package com.gusnot.cloudstock.service.mapper;

import com.gusnot.cloudstock.domain.*;
import com.gusnot.cloudstock.service.dto.TransactionItemDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity TransactionItem and its DTO TransactionItemDTO.
 */
@Mapper(componentModel = "spring", uses = {SkuMapper.class, TransactionMapper.class, })
public interface TransactionItemMapper extends EntityMapper <TransactionItemDTO, TransactionItem> {

    @Mapping(source = "sku.id", target = "skuId")
    @Mapping(source = "sku.code", target = "skuCode")

    @Mapping(source = "transaction.id", target = "transactionId")
    @Mapping(source = "transaction.refNo", target = "transactionRefNo")
    TransactionItemDTO toDto(TransactionItem transactionItem); 

    @Mapping(source = "skuId", target = "sku")

    @Mapping(source = "transactionId", target = "transaction")
    TransactionItem toEntity(TransactionItemDTO transactionItemDTO); 
    default TransactionItem fromId(Long id) {
        if (id == null) {
            return null;
        }
        TransactionItem transactionItem = new TransactionItem();
        transactionItem.setId(id);
        return transactionItem;
    }
}
