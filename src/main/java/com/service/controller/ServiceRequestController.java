package com.service.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.service.model.Payload;
import com.service.pojo.DummyRequest;
import com.service.pojo.DummyResponse;
import com.service.redis.rmq.Producer;
import com.service.repo.PayloadRepo;

@Controller
@ResponseBody

public class ServiceRequestController {
	@Autowired
	@Qualifier("producer")
	private Producer producer;
	@Autowired
	private PayloadRepo repo;

	@Autowired
	private SimpMessagingTemplate template;

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

	@RequestMapping(value = "/getAll", method = RequestMethod.GET)
	public @ResponseBody List<Payload> getAllSaved() {
		List<Payload> payloadList = new ArrayList<Payload>();
		Iterable<Payload> itr = repo.findAll();
		for (Payload payload : itr) {
			payloadList.add(payload);
		}
		return payloadList;
	}

	@MessageMapping("/posthere")
	@SendTo("/topic/listener")
	public DummyResponse handleRequest(@RequestBody(required = false) DummyRequest dummyRequest) {
		if (dummyRequest == null)
			return null;
		DummyResponse dResponse = new DummyResponse();
		dResponse.setDummyPayloadRecieved(dummyRequest.getDummyPayload());
		// Push it to redis queue
		producer.publish(dummyRequest.getDummyPayload());
		template.convertAndSend("/topic/listener", dResponse);
		return dResponse;
	}
}
