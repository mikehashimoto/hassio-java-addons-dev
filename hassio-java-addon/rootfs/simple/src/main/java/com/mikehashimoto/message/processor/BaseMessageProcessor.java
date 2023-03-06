package com.mikehashimoto.message.processor;

import org.json.JSONObject;
import org.springframework.web.reactive.socket.WebSocketSession;

public abstract class BaseMessageProcessor implements MessageProcessor {

	@Override
	public long getID() {
		return _id;
	}

	@Override
	public WebSocketSession getWebSocketSession() {
		return _webSocketSession;
	}

	@Override
	public JSONObject getPayloadJSONObject() {
		return _payloadJSONObject;
	}

	protected BaseMessageProcessor(
		long id, JSONObject payloadJSONObject,
		WebSocketSession webSocketSession) {

		_id = id;
		_payloadJSONObject = payloadJSONObject;
		_webSocketSession = webSocketSession;
	}

	private final long _id;
	private final JSONObject _payloadJSONObject;
	private final WebSocketSession _webSocketSession;

}