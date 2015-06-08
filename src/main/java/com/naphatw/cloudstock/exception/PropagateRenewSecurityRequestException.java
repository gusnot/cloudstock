package com.naphatw.cloudstock.exception;

public class PropagateRenewSecurityRequestException extends RuntimeException {
	/** Serial version UID. */
	private static final long serialVersionUID = 889957036974301210L;

	private final int lastServerIndex;

	public PropagateRenewSecurityRequestException(final String message, final int lastServerIndex) {
		super(message);

		this.lastServerIndex = lastServerIndex;
	}

	public int getLastServerIndex() {
		return this.lastServerIndex;
	}
}