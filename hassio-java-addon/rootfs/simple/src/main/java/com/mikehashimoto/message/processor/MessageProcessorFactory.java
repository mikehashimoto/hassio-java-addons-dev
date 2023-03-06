package com.mikehashimoto.message.processor;

import com.mikehashimoto.HassioWebSocketHandler;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class MessageProcessorFactory {

	public MessageProcessor getMessageProcessor(
		WebSocketMessage webSocketMessage, WebSocketSession webSocketSession) {

		synchronized (_messageProcessors) {
			_id++;

			JSONObject payloadJSONObject = new JSONObject(
				webSocketMessage.getPayloadAsText(StandardCharsets.UTF_8));

			String type = payloadJSONObject.getString("type");

			if (type.equals("auth_required")) {
				return new AuthRequiredMessageProcessor(
					_id, payloadJSONObject, webSocketSession,
					_hassioAccessToken);
			}
			else if (type.equals("auth_ok")) {
				return new AuthOkMessageProcessor(
					_id, payloadJSONObject, webSocketSession);
			}

			return new DefaultMessageProcessor(
				_id, payloadJSONObject, webSocketSession);
		}
	}

	private Long _id = 0L;

	private final Map<Long, MessageProcessor> _messageProcessors =
		new HashMap<>();

	@Value("${hassio.access.token}")
	private String _hassioAccessToken;

}