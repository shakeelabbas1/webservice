package com.service.pojo;

/**
 * To let know the object poster what we received just in case
 * 
 * @author shakeel
 *
 */
public class DummyResponse {
	private String dummyPayloadRecieved;

	public DummyResponse() {
	}

	public String getDummyPayloadRecieved() {
		return dummyPayloadRecieved;
	}

	public void setDummyPayloadRecieved(String dummyPayloadRecieved) {
		this.dummyPayloadRecieved = dummyPayloadRecieved;
	}
}
