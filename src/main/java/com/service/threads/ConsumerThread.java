package com.service.threads;

import java.util.Date;

import org.springframework.messaging.simp.SimpMessagingTemplate;

import com.service.model.Payload;
import com.service.redis.rmq.Callback;
import com.service.redis.rmq.Consumer;
import com.service.repo.PayloadRepo;

/**
 * Thread to consume messages saved in Redis and eventually add to Database
 * 
 * @author shakeel
 *
 */
public class ConsumerThread extends Thread {
	private Consumer consumer;
	private PayloadRepo repo;
	private SimpMessagingTemplate template;

	public ConsumerThread(Consumer consumer, PayloadRepo repo, SimpMessagingTemplate template) {
		this.consumer = consumer;
		this.repo = repo;
		this.template = template;
	}

	@Override
	public void run() {
		consumer.consume(new Callback() {
			@Override
			public void onMessage(String message) {
				System.out.println(message);
				Payload payload = new Payload(new Date().getTime(), message);
				repo.save(payload);
				template.convertAndSend("/topic/realtimedelivery", payload);
			}
		});
	}
}
