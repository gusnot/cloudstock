package com.naphatw.cloudstock.exception;

public class AuthenticationException extends RuntimeException {
	/** Serial version UID. */
	private static final long serialVersionUID = -6224384528971760138L;

	public AuthenticationException(final String message) {
		super(message);
	}
}