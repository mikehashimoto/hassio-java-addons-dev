package com.mikehashimoto.message.processor;

import org.json.JSONObject;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;

public class AuthOkMessageProcessor extends BaseMessageProcessor {

	public void process() {
		JSONObject jsonObject = new JSONObject();

		jsonObject.put("id", getID());
		jsonObject.put("type", "subscribe_events");
		jsonObject.put("event_type", "state_changed");

		WebSocketSession webSocketSession = getWebSocketSession();

		webSocketSession.send(
			Mono.just(
				webSocketSession.textMessage(
					jsonObject.toString()))).subscribe();
	}

	protected AuthOkMessageProcessor(
		long id, JSONObject payloadJSONObject,
		WebSocketSession webSocketSession) {

		super(id, payloadJSONObject, webSocketSession);
	}

}
