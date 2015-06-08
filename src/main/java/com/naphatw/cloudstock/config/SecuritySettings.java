package com.naphatw.cloudstock.config;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.jasypt.encryption.pbe.PBEStringEncryptor;
import org.jasypt.util.password.PasswordEncryptor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "security")
public class SecuritySettings {
	@Inject
	private PBEStringEncryptor pbeStringEncryptor;

	@Inject
	private PasswordEncryptor passwordEncryptor;

	private String keystoreFolder = System.getProperty("java.io.tmpdir");

	private String keystorePath;

	private String previousKeystorePath;

	private String firstPasswordPart;

	private String jbancechoKeyAlias;

	private String thirdPartyCertificateAlias;

	private List<String> serverIps = new ArrayList<>(0);

	public PBEStringEncryptor getPbeStringEncryptor() {
		return this.pbeStringEncryptor;
	}

	public void setPbeStringEncryptor(final PBEStringEncryptor pbeStringEncryptor) {
		this.pbeStringEncryptor = pbeStringEncryptor;
	}

	public PasswordEncryptor getPasswordEncryptor() {
		return this.passwordEncryptor;
	}

	public void setPasswordEncryptor(final PasswordEncryptor passwordEncryptor) {
		this.passwordEncryptor = passwordEncryptor;
	}

	public String getKeystoreFolder() {
		return this.keystoreFolder;
	}

	public void setKeystoreFolder(final String keystoreFolder) {
		this.keystoreFolder = keystoreFolder;
	}

	public String getKeystorePath() {
		return this.keystorePath;
	}

	public void setKeystorePath(final String keystorePath) {
		this.keystorePath = keystorePath;
	}

	public String getPreviousKeystorePath() {
		return this.previousKeystorePath;
	}

	public void setPreviousKeystorePath(final String previousKeystorePath) {
		this.previousKeystorePath = previousKeystorePath;
	}

	public String getFirstPasswordPart() {
		return this.pbeStringEncryptor.decrypt(this.firstPasswordPart);
	}

	public void setFirstPasswordPart(final String firstPasswordPart) {
		this.firstPasswordPart = firstPasswordPart;
	}

	public String getJbancechoKeyAlias() {
		return this.jbancechoKeyAlias;
	}

	public void setJbancechoKeyAlias(final String jbancechoKeyAlias) {
		this.jbancechoKeyAlias = jbancechoKeyAlias;
	}

	public String getThirdPartyCertificateAlias() {
		return this.thirdPartyCertificateAlias;
	}

	public void setThirdPartyCertificateAlias(final String thirdPartyCertificateAlias) {
		this.thirdPartyCertificateAlias = thirdPartyCertificateAlias;
	}

	public List<String> getServerIps() {
		return this.serverIps;
	}

	public void setServerIps(final List<String> serverIps) {
		this.serverIps = serverIps;
	}
}
