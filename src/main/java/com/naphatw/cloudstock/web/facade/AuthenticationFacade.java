package com.naphatw.cloudstock.web.facade;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.naphatw.cloudstock.exception.AuthenticationException;
import com.naphatw.cloudstock.service.AuthenticationService;
import com.naphatw.cloudstock.web.rest.dto.LoginDataDTO;
import com.naphatw.cloudstock.web.rest.dto.TokenDTO;

/**
 * Facade of the authentication.
 *
 * @author Allianz
 */
@Service
public class AuthenticationFacade {
	/** Authentication service. */
	@Inject
	private AuthenticationService authenticationService;

	/**
	 * Logs in an user.
	 *
	 * @param loginData
	 *            Login data of the user to login.
	 * @return A token.
	 * @throws AuthenticationException
	 *             If the authentication fails.
	 */
	public TokenDTO login(final LoginDataDTO loginData) {
		TokenDTO token = new TokenDTO();

		token.setToken(this.authenticationService.login(loginData.getLogin(), loginData.getPassword()));

		return token;
	}

	/**
	 * Checks the validity of the given token.
	 *
	 * @param token
	 *            Token to check.
	 * @throws AuthenticationException
	 *             If the token is not recognized.
	 */
	public void checkAuthentication(final String token) {
		this.authenticationService.checkAuthentication(token);
	}

	/**
	 * Logs out.
	 *
	 * @param token
	 *            token of the user to logout.
	 */
	public void logout(final String token) {
		this.authenticationService.logout(token);
	}
}
