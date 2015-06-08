package com.naphatw.cloudstock.web.rest.dto;

import java.io.Serializable;

public class SecurityDataDTO implements Serializable {
	/** Serial version UID. */
	private static final long serialVersionUID = 1679124103965584657L;

	private boolean initialRequest;

	private String secondPasswordPart;

	private String thirdPasswordPart;

	private byte[] jbancechoPrivateKey;

	private byte[] jbancechoCertificate;

	private byte[] thirdPartyCertificate;

	public boolean isInitialRequest() {
		return this.initialRequest;
	}

	public void setInitialRequest(final boolean initialRequest) {
		this.initialRequest = initialRequest;
	}

	public String getSecondPasswordPart() {
		return this.secondPasswordPart;
	}

	public void setSecondPasswordPart(final String secondPasswordPart) {
		this.secondPasswordPart = secondPasswordPart;
	}

	public String getThirdPasswordPart() {
		return this.thirdPasswordPart;
	}

	public void setThirdPasswordPart(final String thirdPasswordPart) {
		this.thirdPasswordPart = thirdPasswordPart;
	}

	public byte[] getJbancechoPrivateKey() {
		return this.jbancechoPrivateKey;
	}

	public void setJbancechoPrivateKey(final byte[] jbancechoPrivateKey) {
		this.jbancechoPrivateKey = jbancechoPrivateKey;
	}

	public byte[] getJbancechoCertificate() {
		return this.jbancechoCertificate;
	}

	public void setJbancechoCertificate(final byte[] jbancechoCertificate) {
		this.jbancechoCertificate = jbancechoCertificate;
	}

	public byte[] getThirdPartyCertificate() {
		return this.thirdPartyCertificate;
	}

	public void setThirdPartyCertificate(final byte[] thirdPartyCertificate) {
		this.thirdPartyCertificate = thirdPartyCertificate;
	}
}
