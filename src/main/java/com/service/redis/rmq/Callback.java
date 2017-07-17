package com.service.redis.rmq;

/**
 * 
 * @note from Shakeel - this is copied from https://github.com/xetorthio/rmq
 *
 */
public interface Callback {
	public void onMessage(String message);
}
