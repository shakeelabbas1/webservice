package com.service.model;

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
public class Payload {
	@Id
	private long id;

	@Column(name = "data", columnDefinition = "text")
	private String payloadText;

	/**
	 * Protected to be use by JPA
	 */
	protected Payload() {
	};

	public Payload(long id, String payloadText) {
		this.id = id;
		this.payloadText = payloadText;
	}
}
