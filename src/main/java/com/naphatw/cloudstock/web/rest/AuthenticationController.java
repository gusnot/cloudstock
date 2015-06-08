package com.naphatw.cloudstock.web.rest;

import javax.inject.Inject;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.naphatw.cloudstock.exception.AuthenticationException;
import com.naphatw.cloudstock.web.facade.AuthenticationFacade;
import com.naphatw.cloudstock.web.rest.dto.LoginDataDTO;
import com.naphatw.cloudstock.web.rest.dto.TokenDTO;

/**
 * Rest service for authenticating user,
 *
 * @author Allianz
 */
@RestController
@RequestMapping("${webservices.restPath}/authservice")
public class AuthenticationController {
	/** Logger. */
	private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationController.class);

	/** Authentication facade. */
	@Inject
	private AuthenticationFacade authenticationFacade;

	/**
	 * Logs in an user.
	 *
	 * @param loginData
	 *            Login data of the user to login.
	 * @return A token.
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<TokenDTO> login(@Valid @RequestBody final LoginDataDTO loginData) {
		LOGGER.debug("REST request to log in an user: {}", loginData);

		try {
			TokenDTO token = this.authenticationFacade.login(loginData);

			return new ResponseEntity<>(token, HttpStatus.OK);
		} catch (AuthenticationException e) {
			LOGGER.error("Authentication failed: {}", e);
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		}
	}

	/**
	 * Logs out an user.
	 */
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public void logout(@RequestHeader("authToken") final String token) {
		LOGGER.debug("REST request to logout an user");

		this.authenticationFacade.logout(token);
	}
}
