package com.service;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import com.service.redis.rmq.Consumer;
import com.service.repo.PayloadRepo;
import com.service.threads.ConsumerThread;

@SpringBootApplication
@ImportResource("classpath:beans.xml")
public class ServiceApplication {
	@Autowired
	private PayloadRepo repo;
	@Autowired
	private Consumer consumer;
	@Autowired
	private SimpMessagingTemplate template;

	public static void main(String[] args) {
		SpringApplication.run(ServiceApplication.class, args);

	}

	@PostConstruct
	public void postProcessing() {
		ConsumerThread ct = new ConsumerThread(consumer, repo, template);
		ct.start();
	}

}
