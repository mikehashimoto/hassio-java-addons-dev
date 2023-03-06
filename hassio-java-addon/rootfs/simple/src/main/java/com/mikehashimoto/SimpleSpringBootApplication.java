package com.mikehashimoto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class SimpleSpringBootApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(
			SimpleSpringBootApplication.class, args);

		HassioWebSocketHandler hassioWebSocketHandler =
			(HassioWebSocketHandler)context.getBean(
				"hassioWebSocketHandler");

		hassioWebSocketHandler.start();
	}

}