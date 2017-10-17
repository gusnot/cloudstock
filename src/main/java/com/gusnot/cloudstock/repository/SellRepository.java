package com.gusnot.cloudstock.repository;

import com.gusnot.cloudstock.domain.Sell;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Sell entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SellRepository extends JpaRepository<Sell, Long> {

}
