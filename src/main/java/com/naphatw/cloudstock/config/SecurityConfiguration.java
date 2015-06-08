package com.naphatw.cloudstock.config;

import javax.inject.Inject;

import org.jasypt.encryption.pbe.PBEStringEncryptor;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.EnvironmentPBEConfig;
import org.jasypt.util.password.BasicPasswordEncryptor;
import org.jasypt.util.password.PasswordEncryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class SecurityConfiguration {
	private final static Logger LOGGER = LoggerFactory.getLogger(SecurityConfiguration.class);

	@Inject
	private Environment environment;

	@Bean
	public EnvironmentPBEConfig environmentPBEConfig() {
		LOGGER.debug("Configuring Jasypt configuration");

		EnvironmentPBEConfig environmentPBEConfig = new EnvironmentPBEConfig();

		environmentPBEConfig.setAlgorithm("PBEWithSHA1AndDESede");
		if (this.environment.getProperty("jasypt.password") != null) {
			environmentPBEConfig.setPassword(this.environment.getProperty("jasypt.password"));
		} else if (System.getProperty(Constants.JASYPT_PASSWORD_SYSTEM_PROPERTY) != null) {
			environmentPBEConfig.setPasswordSysPropertyName(Constants.JASYPT_PASSWORD_SYSTEM_PROPERTY);
		} else {
			environmentPBEConfig.setPasswordEnvName(Constants.JASYPT_PASSWORD_ENVIRONMENT_PROPERTY);
		}

		return environmentPBEConfig;
	}

	@Bean
	public PBEStringEncryptor pbeStringEncryptor(final EnvironmentPBEConfig environmentPBEConfig) {
		LOGGER.debug("Configuring Jasypt encryptor");

		StandardPBEStringEncryptor standardPBEStringEncryptor = new StandardPBEStringEncryptor();

		standardPBEStringEncryptor.setConfig(environmentPBEConfig);

		return standardPBEStringEncryptor;
	}

	@Bean
	public PasswordEncryptor passwordEncryptor() {
		return new BasicPasswordEncryptor();
	}
}
