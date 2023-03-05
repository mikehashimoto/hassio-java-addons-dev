package com.mikehashimoto;

import com.mikehashimoto.http.request.HttpRequest;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SimpleRestController {

	@GetMapping("/api")
	public String home() {
		return _httpRequest.get("/api/");
	}

	@GetMapping("/events")
	public String events() {
		return _httpRequest.get("/api/events");
	}

	@GetMapping("/config")
	public String config() {
		return _httpRequest.get("/api/config");
	}

	@GetMapping("/services")
	public String services() {
		return _httpRequest.get("/api/services");
	}

	@GetMapping("/states")
	public String states() {
		return _httpRequest.get("/api/states");
	}

	@GetMapping("/calendars")
	public String calendars() {
		return _httpRequest.get("/api/calendars");
	}

	@GetMapping("/turnOnKitchen")
	public String turnOnKitchen() {
		JSONObject requestJSONObject = new JSONObject();

		requestJSONObject.put(
			"entity_id",
			new String[] {
				"light.kitchen_main_lights"
			});
		requestJSONObject.put("brightness_pct", "100");

		JSONArray responseJSONArray = new JSONArray(
			_httpRequest.post(
				"/api/services/light/turn_on",
				requestJSONObject.toString()));

		return responseJSONArray.toString(2);
	}

	@GetMapping("/turnOnKitchenSlowly")
	public String turnOnKitchenSlowly() {
		JSONObject requestJSONObject = new JSONObject();

		for (int i = 0; i <= 100; i++) {
			try {
				Thread.sleep(500);
			}
			catch (InterruptedException interruptedException) {
				throw new RuntimeException(interruptedException);
			}

			requestJSONObject.put(
				"entity_id",
				new String[] {
					"light.kitchen_main_lights"
				});
			requestJSONObject.put("brightness_pct", i);

			_httpRequest.post(
				"/api/services/light/turn_on",
				requestJSONObject.toString());
		}

		return "{\"status\", \"done\"}";
	}

	@GetMapping("/turnOnKitchenAndLivingRoom")
	public String turnOnKitchenAndLivingRoom() {
		JSONObject requestJSONObject = new JSONObject();

		requestJSONObject.put(
			"entity_id",
			new String[] {
				"light.master_bedroom_living_room_lights",
				"light.kitchen_main_lights"
			});
		requestJSONObject.put("brightness_pct", "100");

		JSONArray responseJSONArray = new JSONArray(
			_httpRequest.post(
				"/api/services/light/turn_on",
				requestJSONObject.toString()));

		return responseJSONArray.toString(2);
	}

	@GetMapping("/turnOffKitchen")
	public String turnOffKitchen() {
		JSONObject requestJSONObject = new JSONObject();

		requestJSONObject.put(
			"entity_id",
			new String[] {
				"light.kitchen_main_lights"
			});

		JSONArray responseJSONArray = new JSONArray(
			_httpRequest.post(
				"/api/services/light/turn_off",
				requestJSONObject.toString()));

		return responseJSONArray.toString(2);
	}

	@RequestMapping(value = "/notification", method = RequestMethod.POST, consumes="application/json")
	public String notification(@RequestBody String payload) {
		System.out.println(payload);

		return "{}";
	}

	@Autowired
	private HttpRequest _httpRequest;

	@Value("${home.assistant.url}")
	private String _homeAssistantURL;

}