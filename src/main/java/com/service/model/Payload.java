package com.service.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Corresponding class for payload table
 * 
 * @author shakeel
 *
 */
@Entity
@Table(name = "payload")
public class Payload implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private long id;

	@Column(name = "data", columnDefinition = "text")
	private String payloadText;

	/**
	 * Protected to be used by JPA
	 */
	protected Payload() {
	};

	public Payload(long id, String payloadText) {
		this.id = id;
		this.payloadText = payloadText;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getPayloadText() {
		return payloadText;
	}

	public void setPayloadText(String payloadText) {
		this.payloadText = payloadText;
	}
}
