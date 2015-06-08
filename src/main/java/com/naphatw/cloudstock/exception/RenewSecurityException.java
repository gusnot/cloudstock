package com.naphatw.cloudstock.exception;

public class RenewSecurityException extends RuntimeException {
	/** Serial version UID. */
	private static final long serialVersionUID = -8239142604099448655L;

	public RenewSecurityException(final String message) {
		super(message);
	}

	public RenewSecurityException(final Throwable cause) {
		super(cause);
	}

	public RenewSecurityException(final String message, final Throwable cause) {
		super(message, cause);
	}
}