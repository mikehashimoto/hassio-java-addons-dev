package com.mikehashimoto.message.processor;

import org.json.JSONObject;
import org.springframework.web.reactive.socket.WebSocketSession;

public class DefaultMessageProcessor extends BaseMessageProcessor {

	public void process() {
		System.out.println(getPayloadJSONObject());
	}

	protected DefaultMessageProcessor(
		long id, JSONObject payloadJSONObject,
		WebSocketSession webSocketSession) {

		super(id, payloadJSONObject, webSocketSession);
	}
}
