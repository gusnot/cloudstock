package com.naphatw.cloudstock.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.naphatw.cloudstock.domain.JBEConf;

public interface JBEConfRepository extends JpaRepository<JBEConf, Long> {
	JBEConf findByPasswordPartNumber(int passwordPartNumber);

	@Query("select value from JBEConf where passwordPartNumber = :passwordPartNumber")
	String findPasswordPartByPasswordPartNumber(@Param("passwordPartNumber") int passwordPartNumber);

	@Modifying
	@Query("delete from JBEConf where passwordPartNumber = :passwordPartNumber")
	void deletePasswordPartNumber(@Param("passwordPartNumber") int passwordPartNumber);

	@Modifying
	@Query("delete from JBEConf where passwordPartNumber = 4 or passwordPartNumber = 5")
	void deletePreviousPasswordParts();
}
