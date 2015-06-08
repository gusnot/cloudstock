package com.naphatw.cloudstock.web.rest.dto;

import java.io.Serializable;

public class PasswordPartDataDTO implements Serializable {
	/** Serial version UID. */
	private static final long serialVersionUID = -7765308129540164145L;

	private String value;

	private int number;

	public String getValue() {
		return this.value;
	}

	public void setValue(final String value) {
		this.value = value;
	}

	public int getNumber() {
		return this.number;
	}

	public void setNumber(final int number) {
		this.number = number;
	}
}
