package com.naphatw.cloudstock.web.rest.dto;

import java.io.Serializable;

public class TokenDTO implements Serializable {
	/** Serial version UID. */
	private static final long serialVersionUID = 2840765679845550269L;

	private String token;

	public String getToken() {
		return this.token;
	}

	public void setToken(final String token) {
		this.token = token;
	}
}
