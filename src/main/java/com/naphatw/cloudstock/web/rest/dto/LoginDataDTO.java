package com.naphatw.cloudstock.web.rest.dto;

import java.io.Serializable;

public class LoginDataDTO implements Serializable {
	/** Serial version UID. */
	private static final long serialVersionUID = 2840765679845550269L;

	private String login;

	private String password;

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

	@Override
	public String toString() {
		return LoginDataDTO.class.getSimpleName() + "{'login'='" + this.login + "'}";
	}
}
