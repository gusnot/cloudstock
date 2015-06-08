package com.naphatw.cloudstock.config;

/**
 * Application constants.
 */
public final class Constants {
	private Constants() {
	}

	public static final String SPRING_PROFILE_CI = "ci";

	public static final String SPRING_PROFILE_DEVELOPMENT = "dev";

	public static final String SPRING_PROFILE_PRODUCTION = "prod";

	public static final String SPRING_PROFILE_SHOW = "show";

	public static final String SPRING_PROFILE_SIT = "test";

	public static final String SPRING_PROFILE_UAT = "uat";

	public static final String JASYPT_PASSWORD_SYSTEM_PROPERTY = "jbancecho.password";

	public static final String JASYPT_PASSWORD_ENVIRONMENT_PROPERTY = "JBANCECHO_PASSWORD";

	public static final String DEV_LIQUIBASE_MASTER_RELATIVE_PATH = "config/liquibase/master-changelog-dev.yml";

	public static final String PROD_LIQUIBASE_MASTER_RELATIVE_PATH = "config/liquibase/master-changelog-prod.yml";

	public static final String OPENSSL_COMMAND = "openssl";

	public static final String OPENSSL_COMMAND_LINE = OPENSSL_COMMAND + " pkcs8 -topk8 -nocrypt -inform PEM -outform DER";

	public static final String DEFAULT_KEYSTORE_FILENAME = "jbancecho-keystore";
}
