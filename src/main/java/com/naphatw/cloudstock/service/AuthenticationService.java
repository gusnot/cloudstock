package com.naphatw.cloudstock.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.naphatw.cloudstock.config.SecuritySettings;
import com.naphatw.cloudstock.domain.JBEUser;
import com.naphatw.cloudstock.exception.AuthenticationException;
import com.naphatw.cloudstock.repository.JBEUserRepository;

/**
 * Service for authenticating users.
 *
 * @author Allianz
 */
@Service
@Transactional
public class AuthenticationService {
	/** Logger. */
	private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationService.class);

	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyy-mm-dd-hh:mm:ss");

	@Inject
	private JBEUserRepository jbeUserRepository;

	@Inject
	private SecuritySettings securitySettings;

	@Value("${authentication.tokenExpirationTime}")
	private long tokenExpirationTime;

	@Value("${authentication.permanentServerToken}")
	private String permanentServerToken;

	/**
	 * Logs in an user.
	 *
	 * @param login
	 *            Login of the user.
	 * @param password
	 *            Password of the user.
	 * @return A token.
	 * @throws AuthenticationException
	 *             If the authentication fails.
	 */
	public String login(final String login, final String password) {
		LOGGER.debug("Logging in an user: {}", login);

		JBEUser jbeUser = this.jbeUserRepository.findByLogin(login);

		if (this.securitySettings.getPasswordEncryptor().checkPassword(password, jbeUser.getPassword())) {
			Date tokenDate = new Date();
			String token = "JBE" + UUID.randomUUID().toString().toUpperCase() + "|" + jbeUser.getLogin() + "|"
					+ DATE_FORMAT.format(tokenDate);

			jbeUser.setToken(this.securitySettings.getPasswordEncryptor().encryptPassword(token));
			jbeUser.setTokenDate(tokenDate);
			this.jbeUserRepository.save(jbeUser);

			return token;
		} else {
			throw new AuthenticationException("Invalid password");
		}
	}

	/**
	 * Checks the validity of the given token.
	 *
	 * @param token
	 *            Token to check.
	 * @throws AuthenticationException
	 *             If the token is not recognized.
	 */
	@Transactional(noRollbackFor = AuthenticationException.class)
	public void checkAuthentication(final String token) {
		LOGGER.debug("Verifying a token");

		// Check if it is the permanent server token which is always valid and accepted
		if (!this.securitySettings.getPbeStringEncryptor().decrypt(this.permanentServerToken).equals(token)) {
			String[] splittedToken = token.split("\\|");

			if (splittedToken.length == 3) {
				JBEUser jbeUser = this.jbeUserRepository.findByLogin(splittedToken[1]);

				if (jbeUser != null) {
					if (this.securitySettings.getPasswordEncryptor().checkPassword(token, jbeUser.getToken())) {
						Date expirationDate = this.getExpirationDate();

						if (jbeUser.getTokenDate().before(expirationDate)) {
							jbeUser.setToken(null);
							jbeUser.setTokenDate(null);
							this.jbeUserRepository.save(jbeUser);
							this.jbeUserRepository.flush();
							throw new AuthenticationException("Expired token");
						} else {
							jbeUser.setTokenDate(new Date());
							this.jbeUserRepository.save(jbeUser);
						}
					} else {
						throw new AuthenticationException("Invalid token");
					}
				} else {
					throw new AuthenticationException("Unrecognized token");
				}
			} else {
				throw new AuthenticationException("Wrong token format");
			}
		}
	}

	@Async
	@Scheduled(fixedDelayString = "${authentication.scheduleTime}")
	public void checkTokenExpiration() {
		LOGGER.debug("Deleting all expired tokens");

		Date expirationDate = this.getExpirationDate();

		List<JBEUser> jbeUsers = this.jbeUserRepository.findByTokenDateBefore(expirationDate);

		for (JBEUser jbeUser : jbeUsers) {
			jbeUser.setToken(null);
			jbeUser.setTokenDate(null);
		}

		this.jbeUserRepository.save(jbeUsers);
	}

	/**
	 * Logs out an user.
	 *
	 * @param token
	 *            token of the user to logout.
	 */
	public void logout(final String token) {
		LOGGER.debug("Logging out an user");

		String[] splittedToken = token.split("\\|");

		if (splittedToken.length == 3) {
			JBEUser jbeUser = this.jbeUserRepository.findByLogin(splittedToken[1]);

			if (jbeUser != null) {
				if (this.securitySettings.getPasswordEncryptor().checkPassword(token, jbeUser.getToken())) {
					jbeUser.setToken(null);
					jbeUser.setTokenDate(null);
					this.jbeUserRepository.save(jbeUser);
				} else {
					throw new AuthenticationException("Invalid token");
				}
			} else {
				throw new AuthenticationException("Unrecognized token");
			}
		} else {
			throw new AuthenticationException("Wrong token format");
		}
	}

	private Date getExpirationDate() {
		Date d = new Date();
		long time = d.getTime() - this.tokenExpirationTime;
		d.setTime(time);
		return d;
	}
}
