package com.mikehashimoto.message.processor;

import org.json.JSONObject;
import org.springframework.web.reactive.socket.WebSocketSession;

public interface MessageProcessor {

	public long getID();

	public void process();

	public JSONObject getPayloadJSONObject();

	public WebSocketSession getWebSocketSession();

}