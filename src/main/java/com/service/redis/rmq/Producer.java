package com.service.redis.rmq;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;
import redis.clients.jedis.Tuple;

/**
 * 
 * @note from Shakeel - this is copied from https://github.com/xetorthio/rmq
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
@Component
public class Producer {
	private Jedis jedis;
	private Nest topic;
	private Nest subscriber;

	public Producer(final Jedis jedis, final String topic) {
		this.jedis = jedis;
		this.topic = new Nest("topic:" + topic, jedis);
		this.subscriber = new Nest(this.topic.cat("subscribers").key(), jedis);
	}

	public void publish(final String message) {
		publish(message, 0);
	}

	protected Integer getNextMessageId() {
		final String slastMessageId = topic.get();
		Integer lastMessageId = 0;
		if (slastMessageId != null) {
			lastMessageId = Integer.parseInt(slastMessageId);
		}
		lastMessageId++;
		return lastMessageId;
	}

	public void clean() {
		Set<Tuple> zrangeWithScores = subscriber.zrangeWithScores(0, 1);
		Tuple next = zrangeWithScores.iterator().next();
		Integer lowest = (int) next.getScore();
		topic.cat("message").cat(lowest).del();
	}

	/**
	 * 
	 * @param message
	 *            menssage
	 * @param seconds
	 *            expiry time
	 */
	public void publish(String message, int seconds) {
		List<Object> exec = null;
		Integer lastMessageId = null;
		do {
			topic.watch();
			lastMessageId = getNextMessageId();
			Transaction trans = jedis.multi();
			String msgKey = topic.cat("message").cat(lastMessageId).key();
			trans.set(msgKey, message);
			trans.set(topic.key(), lastMessageId.toString());
			if (seconds > 0)
				trans.expire(msgKey, seconds);
			exec = trans.exec();
		} while (exec == null);
	}

}