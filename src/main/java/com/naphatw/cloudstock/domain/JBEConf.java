package com.naphatw.cloudstock.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.jasypt.hibernate4.type.EncryptedStringType;

@Entity
@Table(name = "T_JBECONF")
@TypeDefs({ @TypeDef(name = "encryptedPassword", typeClass = EncryptedStringType.class, parameters = { @Parameter(name = "encryptorRegisteredName", value = "databasePasswordEncryptor") }) })
public class JBEConf {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_JBECONF")
	@SequenceGenerator(name = "SEQ_JBECONF", sequenceName = "SEQ_JBECONF", allocationSize = 1)
	private Long id;

	@Type(type = "encryptedPassword")
	@Column(name = "VALUE")
	private String value;

	@Column(name = "PART_NUMBER")
	private int passwordPartNumber;

	public Long getId() {
		return this.id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(final String value) {
		this.value = value;
	}

	public int getPasswordPartNumber() {
		return this.passwordPartNumber;
	}

	public void setPasswordPartNumber(final int passwordPartNumber) {
		this.passwordPartNumber = passwordPartNumber;
	}

	@Override
	public int hashCode() {
		return (int) (this.id ^ (this.id >>> 32));
	}
}
