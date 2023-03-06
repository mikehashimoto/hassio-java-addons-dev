package com.mikehashimoto;

import java.net.URI;

import java.nio.charset.StandardCharsets;

import com.mikehashimoto.message.processor.MessageProcessor;
import com.mikehashimoto.message.processor.MessageProcessorFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.json.JSONException;
import org.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.context.annotation.Configuration;

import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import org.springframework.web.reactive.socket.client.WebSocketClient;

import reactor.core.publisher.Mono;

@Configuration
public class HassioWebSocketHandler implements WebSocketHandler {

	@Override
	public Mono<Void> handle(WebSocketSession webSocketSession) {
		_webSocketSession = webSocketSession;

		return _webSocketSession.receive().map(
			webSocketMessage -> _handleWebSocketMessage(
				webSocketMessage, webSocketSession)).then();
	}

	public HassioWebSocketHandler() {
		_webSocketClient = new ReactorNettyWebSocketClient();
	}

	public boolean isConnected() {
		return _connected;
	}

	public void start() {
		if (_webSocketSession != null) {
			return;
		}

		Mono<Void> mono = _webSocketClient.execute(
			URI.create(_homeAssistantWebSocketURL), this);

		mono.block();
	}

	public void stop() {
		_webSocketSession.close().subscribe();

		_connected = false;
	}

	private String _handleWebSocketMessage(
		WebSocketMessage webSocketMessage, WebSocketSession webSocketSession) {

		MessageProcessor messageProcessor =
			_messageProcessorFactory.getMessageProcessor(
				webSocketMessage, webSocketSession);

		messageProcessor.process();

		return webSocketMessage.getPayloadAsText();
	}

	@Value("${hassio.access.token}")
	private String _hassioAccessToken;

	@Value("${hassio.web.socket.url}")
	private String _homeAssistantWebSocketURL;

	@Autowired
	MessageProcessorFactory _messageProcessorFactory;

	private final WebSocketClient _webSocketClient;
	private WebSocketSession _webSocketSession;

	private static final Log _log = LogFactory.getLog(
		MessageProcessor.class);

	private boolean _connected = false;

}