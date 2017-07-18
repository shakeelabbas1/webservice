package com.service.threads;

import java.util.Date;

import com.service.model.Payload;
import com.service.redis.rmq.Callback;
import com.service.redis.rmq.Consumer;
import com.service.repo.PayloadRepo;

public class ConsumerThread extends Thread {
	private Consumer consumer;
	private PayloadRepo repo;

	public ConsumerThread(Consumer consumer, PayloadRepo repo) {
		this.consumer = consumer;
		this.repo = repo;
	}

	@Override
	public void run() {
		consumer.consume(new Callback() {
			@Override
			public void onMessage(String message) {
				System.out.println(message);
				Payload payload = new Payload(new Date().getTime(), message);
				repo.save(payload);
			}
		});
	}
}
