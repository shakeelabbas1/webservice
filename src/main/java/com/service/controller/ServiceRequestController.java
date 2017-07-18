package com.service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.service.pojo.DummyRequest;
import com.service.pojo.DummyResponse;
import com.service.redis.rmq.Producer;

@RestController
@RequestMapping("/myservice")

public class ServiceRequestController {
	@Autowired
	@Qualifier("producer")
	private Producer producer;

	@RequestMapping("/handleJson")
	public DummyResponse requestHandlerJson(@RequestBody DummyRequest dummyRequest) {
		return handleRequest(dummyRequest);
	}

	@RequestMapping("/handleParam")
	public @ResponseBody DummyResponse requestHandlerJson(@RequestParam("payload") String payload) {
		DummyRequest dummyRequest = new DummyRequest();
		dummyRequest.setDummyPayload(payload);
		return handleRequest(dummyRequest);
	}

	private @ResponseBody DummyResponse handleRequest(DummyRequest dummyRequest) {
		DummyResponse dResponse = new DummyResponse();
		dResponse.setDummyPayloadRecieved(dummyRequest.getDummyPayload());
		// Push it to redis queue
		producer.publish(dummyRequest.getDummyPayload());
		return dResponse;
	}

}
