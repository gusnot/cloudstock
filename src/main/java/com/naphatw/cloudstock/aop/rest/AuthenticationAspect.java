package com.naphatw.cloudstock.aop.rest;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.naphatw.cloudstock.exception.AuthenticationException;
import com.naphatw.cloudstock.web.facade.AuthenticationFacade;

/**
 * Aspect for request execution of GoalSolutionMgmtService
 */
@Aspect
public class AuthenticationAspect {
	/** Logger. */
	private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationAspect.class);

	@Inject
	private AuthenticationFacade authenticationFacade;

	@Autowired(required = true)
	private HttpServletRequest request;

	@Autowired(required = true)
	private HttpServletResponse response;

	@Pointcut("execution(* com.naphatw.cloudstock.web.rest.AdminController.*(..))")
	public void doPointcut() {
	}

	@Around("doPointcut()")
	public void doArround(final ProceedingJoinPoint joinPoint) throws Throwable {
		String token = this.request.getHeader("authToken");

		if ((token != null) && !token.isEmpty()) {
			try {
				this.authenticationFacade.checkAuthentication(token);
				joinPoint.proceed();
			} catch (AuthenticationException e) {
				LOGGER.error("Authentication failed: {}", e.getMessage());

				this.response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
			}

		} else {
			LOGGER.error("Authentication failed: no token provided");

			this.response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "No token provided");
		}
	}
}
