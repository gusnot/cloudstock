package com.gusnot.cloudstock.service.mapper;

import com.gusnot.cloudstock.domain.*;
import com.gusnot.cloudstock.service.dto.TransactionDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Transaction and its DTO TransactionDTO.
 */
@Mapper(componentModel = "spring", uses = {BranchMapper.class, })
public interface TransactionMapper extends EntityMapper <TransactionDTO, Transaction> {

    @Mapping(source = "branch.id", target = "branchId")
    @Mapping(source = "branch.name", target = "branchName")
    TransactionDTO toDto(Transaction transaction); 
    @Mapping(target = "transactionItems", ignore = true)

    @Mapping(source = "branchId", target = "branch")
    Transaction toEntity(TransactionDTO transactionDTO); 
    default Transaction fromId(Long id) {
        if (id == null) {
            return null;
        }
        Transaction transaction = new Transaction();
        transaction.setId(id);
        return transaction;
    }
}
