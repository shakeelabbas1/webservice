package com.service.redis.rmq;

import java.util.Set;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

/**
 * 
 * @note from Shakeel - this is copied from https://github.com/xetorthio/rmq
 *
 *       Copyright (c) 2010 Jonathan Leibiusky
 * 
 *       Permission is hereby granted, free of charge, to any person obtaining a
 *       copy of this software and associated documentation files (the
 *       "Software"), to deal in the Software without restriction, including
 *       without limitation the rights to use, copy, modify, merge, publish,
 *       distribute, sublicense, and/or sell copies of the Software, and to
 *       permit persons to whom the Software is furnished to do so, subject to
 *       the following conditions:
 * 
 *       The above copyright notice and this permission notice shall be included
 *       in all copies or substantial portions of the Software.
 * 
 *       THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
 *       OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 *       MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 *       IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 *       CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 *       TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 *       SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 * 
 */

public class Consumer {
	private Nest topic;
	private Nest subscriber;
	private String id;

	public Consumer(final Jedis jedis, final String id, final String topic) {
		this.topic = new Nest("topic:" + topic, jedis);
		this.subscriber = new Nest(this.topic.cat("subscribers").key(), jedis);
		this.id = id;
	}

	private void waitForMessages() {
		try {
			// TODO el otro metodo podria hacer q no se consuman mensajes por un
			// tiempo si no llegan, de esta manera solo se esperan 500ms y se
			// controla que haya mensajes.
			Thread.sleep(100);
		} catch (InterruptedException e) {
		}
	}

	public void consume(Callback callback) {
		while (true) {
			String message = readUntilEnd();
			if (message != null)
				callback.onMessage(message);
			else
				waitForMessages();
		}
	}

	public String consume() {
		return readUntilEnd();
	}

	private String readUntilEnd() {
		while (unreadMessages() > 0) {
			String message = read();
			goNext();
			if (message != null)
				return message;
		}

		return null;
	}

	private void goNext() {
		subscriber.zincrby(1, id);
	}

	private int getLastReadMessage() {
		Double lastMessageRead = subscriber.zscore(id);
		if (lastMessageRead == null) {
			Set<Tuple> zrangeWithScores = subscriber.zrangeWithScores(0, 1);
			if (zrangeWithScores.iterator().hasNext()) {
				Tuple next = zrangeWithScores.iterator().next();
				Integer lowest = (int) next.getScore() - 1;
				subscriber.zadd(lowest, id);
				return lowest;
			} else {
				return 0;
			}
		}
		return lastMessageRead.intValue();
	}

	private int getTopicSize() {
		String stopicSize = topic.get();
		int topicSize = 0;
		if (stopicSize != null) {
			topicSize = Integer.valueOf(stopicSize);
		}
		return topicSize;
	}

	public String read() {
		int lastReadMessage = getLastReadMessage();
		return topic.cat("message").cat(lastReadMessage + 1).get();
	}

	public int unreadMessages() {
		return getTopicSize() - getLastReadMessage();
	}
}