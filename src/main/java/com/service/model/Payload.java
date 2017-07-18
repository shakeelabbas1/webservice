package com.service.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "payload")
public class Payload {
	@Id
	private long id;

	@Column(name = "data", columnDefinition = "text")
	private String payloadText;

	protected Payload() {
	};

	public Payload(long id, String payloadText) {
		this.id = id;
		this.payloadText = payloadText;
	}
}
