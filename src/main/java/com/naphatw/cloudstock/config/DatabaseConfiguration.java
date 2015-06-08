package com.naphatw.cloudstock.config;

import java.util.Arrays;

import javax.sql.DataSource;

import liquibase.integration.spring.SpringLiquibase;

import org.jasypt.encryption.pbe.PBEStringEncryptor;
import org.jasypt.hibernate4.encryptor.HibernatePBEStringEncryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.ApplicationContextException;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@EnableJpaRepositories("com.naphatw.cloudstock.repository")
@EnableTransactionManagement
public class DatabaseConfiguration implements EnvironmentAware {
	private final static Logger LOGGER = LoggerFactory.getLogger(DatabaseConfiguration.class);

	private Environment environment;

	private RelaxedPropertyResolver propertyResolver;

	@Override
	public void setEnvironment(final Environment environment) {
		this.environment = environment;
		this.propertyResolver = new RelaxedPropertyResolver(environment, "spring.datasource.");
	}

	@Bean(name = "CloudstockHirakiDatasource", destroyMethod = "shutdown")
	public DataSource dataSource(final PBEStringEncryptor pbeStringEncryptor) {
		LOGGER.debug("Configuring datasource");

		if ((this.propertyResolver.getProperty("url") == null) && (this.propertyResolver.getProperty("databaseName") == null)) {
			LOGGER.error("Database connection pool configuration is incorrect! The application"
					+ "cannot start. Please check your Spring profile, current profiles are: {}",
					Arrays.toString(this.environment.getActiveProfiles()));

			throw new ApplicationContextException("Database connection pool is not configured correctly");
		}

		HikariConfig config = new HikariConfig();

		config.setPoolName("CloudstockHirakiPool");
		config.setDataSourceClassName(this.propertyResolver.getProperty("dataSourceClassName"));
		if ((this.propertyResolver.getProperty("url") == null) || "".equals(this.propertyResolver.getProperty("url"))) {
			config.addDataSourceProperty("databaseName", this.propertyResolver.getProperty("databaseName"));
			config.addDataSourceProperty("serverName", this.propertyResolver.getProperty("serverName"));
		} else {
			config.addDataSourceProperty("url", this.propertyResolver.getProperty("url"));
		}
		config.addDataSourceProperty("user", this.propertyResolver.getProperty("username"));
		config.addDataSourceProperty("password", pbeStringEncryptor.decrypt(this.propertyResolver.getProperty("password")));

		return new HikariDataSource(config);
	}

	@Bean
	public SpringLiquibase liquibase(final DataSource dataSource) {
		LOGGER.debug("Configuring Liquibase");

		SpringLiquibase liquibase = new SpringLiquibase();

		liquibase.setDataSource(dataSource);
		if (Constants.SPRING_PROFILE_PRODUCTION.equals(this.environment.getProperty("spring.profiles.active"))) {
			liquibase.setChangeLog("classpath:" + Constants.PROD_LIQUIBASE_MASTER_RELATIVE_PATH);
		} else {
			liquibase.setChangeLog("classpath:" + Constants.DEV_LIQUIBASE_MASTER_RELATIVE_PATH);
		}
		liquibase.setContexts("development, production");

		return liquibase;
	}

	@Bean
	public HibernatePBEStringEncryptor hibernatePBEStringEncryptor(final PBEStringEncryptor pbeStringEncryptor) {
		LOGGER.debug("Configuring Jasypt encryptor for Hibernate");

		HibernatePBEStringEncryptor hibernatePBEStringEncryptor = new HibernatePBEStringEncryptor();

		hibernatePBEStringEncryptor.setRegisteredName("databasePasswordEncryptor");
		hibernatePBEStringEncryptor.setEncryptor(pbeStringEncryptor);

		return hibernatePBEStringEncryptor;
	}
}
