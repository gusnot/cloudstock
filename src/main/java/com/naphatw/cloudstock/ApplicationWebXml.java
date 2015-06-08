package com.naphatw.cloudstock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.naphatw.cloudstock.config.Constants;

@ComponentScan
@Configuration
@EnableAutoConfiguration
public class ApplicationWebXml extends SpringBootServletInitializer {
	private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationWebXml.class);

	@Override
	protected SpringApplicationBuilder configure(final SpringApplicationBuilder application) {
		return application.profiles(this.addDefaultProfile()).showBanner(false).sources(Application.class);
	}

	/**
	 * Set a default profile if it has not been set.
	 * <p/>
	 * <p>
	 * Please use -Dspring.profiles.active=dev
	 * </p>
	 */
	private String addDefaultProfile() {
		String profile = System.getProperty("spring.profiles.active");

		if (profile != null) {
			ApplicationWebXml.LOGGER.info("Running with Spring profile(s) : {}", profile);

			return profile;
		} else {
			ApplicationWebXml.LOGGER.warn("No Spring profile configured, running with default configuration");

			return Constants.SPRING_PROFILE_DEVELOPMENT;
		}
	}
}
