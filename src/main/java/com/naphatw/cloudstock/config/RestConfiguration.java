package com.naphatw.cloudstock.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.client.RestTemplate;

import com.naphatw.cloudstock.aop.rest.AuthenticationAspect;

@Configuration
@EnableAspectJAutoProxy
public class RestConfiguration {
	@Bean
	public AuthenticationAspect authenticationAspect() {
		return new AuthenticationAspect();
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}