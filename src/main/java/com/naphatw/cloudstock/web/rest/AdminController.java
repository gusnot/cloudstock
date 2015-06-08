package com.naphatw.cloudstock.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.naphatw.cloudstock.exception.PasswordComparisonException;
import com.naphatw.cloudstock.exception.RenewSecurityException;

/**
 * Rest service for synchronizing data between the administrator page
 * and the back-end.
 *
 * @author Allianz
 */
@RestController
@RequestMapping("${webservices.restPath}/adminservice")
public class AdminController {
	/** Logger. */
	private static final Logger LOGGER = LoggerFactory.getLogger(AdminController.class);

	/**
	 * Handles the given {@link PasswordComparisonException exception} and sets
	 * the HTTP status code to "Bad request" (400).
	 *
	 * @param e
	 *            {@link PasswordComparisonException Exception} to handle.
	 */
	@ExceptionHandler(PasswordComparisonException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Password comparison failed")
	protected void handlePasswordComparisonException(final PasswordComparisonException e) {
		LOGGER.error("Password comparison failed", e);
	}

	/**
	 * Handles the given {@link RenewSecurityException exception} and sets
	 * the HTTP status code to "Bad request" (400).
	 *
	 * @param e
	 *            {@link RenewSecurityException Exception} to handle.
	 */
	@ExceptionHandler(RenewSecurityException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Failed to renew security")
	protected void handleRenewSecurityException(final RenewSecurityException e) {
		LOGGER.error("Failed to renew security", e);
	}

	/**
	 * Handles the given {@link Exception exception} and sets
	 * the HTTP status code to "Internal server error" (500).
	 *
	 * @param e
	 *            {@link Exception Exception} to handle.
	 */
	@ExceptionHandler(Exception.class)
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Internal server error")
	protected void handleException(final Exception e) {
		LOGGER.error("Internal server error", e);
	}
}
