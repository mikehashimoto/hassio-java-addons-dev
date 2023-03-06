package com.mikehashimoto.message.processor;

import org.json.JSONObject;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;

public class AuthRequiredMessageProcessor extends BaseMessageProcessor {

	@Override
	public void process() {
		JSONObject jsonObject = new JSONObject();

		jsonObject.put("access_token", _accessToken);
		jsonObject.put("type", "auth");

		WebSocketSession webSocketSession = getWebSocketSession();

		webSocketSession.send(
			Mono.just(
				webSocketSession.textMessage(
					jsonObject.toString()))).subscribe();
	}

	protected AuthRequiredMessageProcessor(
		long id, JSONObject payloadJSONObject, WebSocketSession webSocketSession,
		String accessToken) {

		super(id, payloadJSONObject, webSocketSession);

		_accessToken = accessToken;
	}

	private final String _accessToken;

}
