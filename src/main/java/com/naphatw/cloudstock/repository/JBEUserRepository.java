package com.naphatw.cloudstock.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.naphatw.cloudstock.domain.JBEUser;

public interface JBEUserRepository extends JpaRepository<JBEUser, Long> {
	JBEUser findByLogin(String login);

	List<JBEUser> findByTokenDateBefore(Date tokenDate);
}
