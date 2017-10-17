package com.gusnot.cloudstock.repository;

import com.gusnot.cloudstock.domain.AttributeItem;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the AttributeItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AttributeItemRepository extends JpaRepository<AttributeItem, Long> {

}
