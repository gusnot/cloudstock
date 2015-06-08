package com.naphatw.cloudstock.exception;

public class DataValidationException extends RuntimeException {
	/** Serial version UID. */
	private static final long serialVersionUID = -366397435609520424L;

	private final String reasonCode;

	public DataValidationException(final String reasonCode, final String message, final Throwable cause) {
		super(message, cause);

		this.reasonCode = reasonCode;
	}

	public String getReasonCode() {
		return this.reasonCode;
	}
}