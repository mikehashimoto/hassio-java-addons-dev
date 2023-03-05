package com.mikehashimoto.http.request;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import javax.net.ssl.SSLException;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class HttpRequest {

	public String get(String path) {
		return request(path, null, HttpMethod.GET);
	}

	public String post(String path, String data) {
		return request(path, data, HttpMethod.POST);
	}

	public String request(String path, String data, HttpMethod httpMethod) {
		WebClient.Builder webClientBuilder = WebClient.builder();

		ClientHttpConnector clientHttpConnector = _getClientHttpConnector();

		if (clientHttpConnector != null) {
			webClientBuilder.clientConnector(clientHttpConnector);
		}

		Map<String, String> headers = new HashMap<>();

		headers.put("Authorization", "Bearer " + _hassioAccessToken);
		headers.put("Content-Type", "application/json");

		webClientBuilder.defaultHeaders(httpHeaders -> {
			for (Map.Entry<String, String> header : headers.entrySet()) {
				httpHeaders.add(header.getKey(), header.getValue());
			}
		});

		WebClient.RequestBodySpec requestBodySpec = webClientBuilder
			.build()
			.method(httpMethod)
			.uri(_hassioURL + path);

		if (data != null) {
			requestBodySpec.body(BodyInserters.fromValue(data));
		}

		return requestBodySpec
			.retrieve()
			.bodyToMono(String.class)
			.block();
    }

	private ClientHttpConnector _getClientHttpConnector() {
		if (!_hassioURL.startsWith("https://")) {
			return null;
		}

		try {
			SslContext sslContext = SslContextBuilder.forClient()
				.trustManager(InsecureTrustManagerFactory.INSTANCE)
				.build();

			return new ReactorClientHttpConnector(
				HttpClient.create().secure(
					sslContextSpec -> sslContextSpec.sslContext(sslContext)));
		}
		catch (SSLException sslException) {
			throw new RuntimeException(sslException);
		}
	}

	@Value("${hassio.access.token}")
	private String _hassioAccessToken;

	@Value("${hassio.url}")
	private String _hassioURL;

}