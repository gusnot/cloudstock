package com.naphatw.cloudstock.exception;

public class PasswordComparisonException extends RuntimeException {
	/** Serial version UID. */
	private static final long serialVersionUID = -453508292031409759L;

	public PasswordComparisonException(final String message) {
		super(message);
	}
}