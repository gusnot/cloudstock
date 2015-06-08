package com.naphatw.cloudstock.config;

import org.dozer.DozerBeanMapper;
import org.dozer.spring.DozerBeanMapperFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DozerConfiguration {
	private static final Logger LOGGER = LoggerFactory.getLogger(DozerConfiguration.class);

	/**
	 * Returns the {@link DozerBeanMapperFactoryBean Dozer bean mapper factory
	 * bean}.
	 *
	 * @return The {@link DozerBeanMapperFactoryBean Dozer bean mapper factory
	 *         bean}.
	 */
	@Bean
	public DozerBeanMapperFactoryBean mapperFactory() {
		LOGGER.debug("Configuring Dozer mapper factory");

		return new DozerBeanMapperFactoryBean();
	}

	/**
	 * Returns the {@link DozerBeanMapper Dozer bean mapper}.
	 *
	 * @return The {@link DozerBeanMapper Dozer bean mapper}.
	 */
	@Bean
	public DozerBeanMapper mapper() {
		LOGGER.debug("Configuring Dozer mapper");

		return new DozerBeanMapper();
	}
}
