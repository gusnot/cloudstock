package com.gusnot.cloudstock.repository;

import com.gusnot.cloudstock.domain.TransactionItem;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the TransactionItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TransactionItemRepository extends JpaRepository<TransactionItem, Long> {

}
