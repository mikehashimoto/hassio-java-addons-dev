package com.mikehashimoto;

import java.net.URI;
import java.time.Duration;

import org.json.JSONObject;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import org.springframework.web.reactive.socket.client.WebSocketClient;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class MyClient implements WebSocketHandler {

	@Override
	public Mono<Void> handle(WebSocketSession webSocketSession) {
		JSONObject jsonObject = new JSONObject();

		jsonObject.put("type", "auth");
		jsonObject.put("access_token", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJlNTc2ZDMzMWNkMWQ0YWJkOGIyNDljNTdmMDlkN2U1YSIsImlhdCI6MTY3Nzc3MjY3MCwiZXhwIjoxOTkzMTMyNjcwfQ.mfEBiSW7H40bDBHn_ljbeOBLorJts3yN1hdDs7gx1_s");

		JSONObject jsonObject0 = new JSONObject();

		jsonObject0.put("id", 18);
		jsonObject0.put("type", "subscribe_events");
		jsonObject0.put("event_type", "state_changed");

		return webSocketSession.send(Mono.just(webSocketSession.textMessage(jsonObject.toString())))
			.then(
				webSocketSession.send(Mono.just(webSocketSession.textMessage(jsonObject0.toString())))
			)
			.thenMany(
				webSocketSession.receive()
				.map(webSocketMessage -> webSocketMessage.getPayloadAsText())
				.log()
			)
			.then();
	}

	public static void main(String args[]) throws Exception {
		WebSocketClient webSocketClient = new ReactorNettyWebSocketClient();

		Mono<Void> mono = webSocketClient.execute(
			URI.create("ws://homeassistant:8123/api/websocket"),
			new MyClient());

		mono.block();

		Thread.sleep(10000);
	}

}