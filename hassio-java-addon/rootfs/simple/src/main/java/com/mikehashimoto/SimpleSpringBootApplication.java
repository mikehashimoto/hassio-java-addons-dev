package com.mikehashimoto;

import com.mikehashimoto.http.request.HttpRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class SimpleSpringBootApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(
			SimpleSpringBootApplication.class, args);

		HttpRequest httpRequest = (HttpRequest)context.getBean("httpRequest");

		JSONArray statesJSONArray = new JSONArray(
			httpRequest.get("/api/states"));

		System.out.println(statesJSONArray.toString(2));

		JSONArray servicesJSONArray = new JSONArray(
			httpRequest.get("/api/services"));

		System.out.println(servicesJSONArray.toString(2));
	}

}