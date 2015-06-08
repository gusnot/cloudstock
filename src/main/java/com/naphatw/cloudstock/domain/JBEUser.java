package com.naphatw.cloudstock.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "T_JBEUSER")
public class JBEUser {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_JBEUSER")
	@SequenceGenerator(name = "SEQ_JBEUSER", sequenceName = "SEQ_JBEUSER", allocationSize = 1)
	private Long id;

	@Column(name = "LOGIN")
	private String login;

	@Column(name = "PASSWORD")
	private String password;

	@Column(name = "TOKEN")
	private String token;

	@Column(name = "TOKEN_DATE")
	private Date tokenDate;

	public Long getId() {
		return this.id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public String getLogin() {
		return this.login;
	}

	public void setLogin(final String login) {
		this.login = login;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(final String password) {
		this.password = password;
	}

	public String getToken() {
		return this.token;
	}

	public void setToken(final String token) {
		this.token = token;
	}

	public Date getTokenDate() {
		return this.tokenDate;
	}

	public void setTokenDate(final Date tokenDate) {
		this.tokenDate = tokenDate;
	}

	@Override
	public int hashCode() {
		return (int) (this.id ^ (this.id >>> 32));
	}
}
