package com.gusnot.cloudstock.repository;

import com.gusnot.cloudstock.domain.TransferItem;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the TransferItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TransferItemRepository extends JpaRepository<TransferItem, Long> {

}
