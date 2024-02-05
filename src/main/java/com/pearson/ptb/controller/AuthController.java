package com.pearson.ptb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.pearson.ptb.provider.pi.service.AuthenticationService;
import com.pearson.ptb.proxy.repo.LoginRepo;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class AuthController {

	
	/*
	 * @GetMapping("/auth") public String auth(HttpServletRequest request) { String
	 * header = request.getHeader("AccessToken"); String header2 =
	 * request.getHeader("UserId"); String h3= header + " " +header2;
	 * 
	 * return h3; }
	 */
	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	@Qualifier("authenticationService")
	private AuthenticationService authenticationService;

		@Autowired
		private LoginRepo loginRepo;
	
	@GetMapping("/auth")
	public ResponseEntity<String> auth(HttpServletRequest request) {
	    String accessToken = request.getHeader("AccessToken");
	    String userId = request.getHeader("UserId");
	    System.out.println(accessToken +" " +userId);

	    // Get the login count for the user
	    int loginCount =loginRepo.logLogin(userId);

	    String url = "https://int-piapi.stg-openclass.com/identityprofiles/" + userId;


	    HttpHeaders headers = new HttpHeaders();
	    headers.set("x-Authorization", accessToken);
	    HttpEntity<String> requestEntity = new HttpEntity<>(headers);

	    try {
	        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
	                String.class);
	        String body = responseEntity.getBody();
	        String jsonResponse = buildResponse(accessToken, loginCount, body);

	        // Set the content type to application/json
	        HttpHeaders responseHeaders = new HttpHeaders();
	        responseHeaders.set("Content-Type", "application/json");

	        return ResponseEntity.ok()
	                             .headers(responseHeaders)
	                             .body(jsonResponse);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error accessing resource");
	    }

	}

	private String buildResponse(String accessToken, int loginCount, String responseBody) {
	    JsonElement jsonElement = JsonParser.parseString(responseBody);
	    JsonObject jsonObject = jsonElement.getAsJsonObject().getAsJsonObject("data");

	    String givenName = jsonObject.get("givenName").getAsString();
	    String familyName = jsonObject.get("familyName").getAsString();
	    String emailAddress = jsonObject.getAsJsonArray("emails").get(0).getAsJsonObject().get("emailAddress").getAsString();

	    StringBuilder responseBuilder = new StringBuilder();
	    responseBuilder.append("{");
	    responseBuilder.append("\"token\": \"" + accessToken + "\", ");
	    responseBuilder.append("\"loginCount\": \"" + loginCount + "\", ");
	    responseBuilder.append("\"emailAddress\": \"" + emailAddress + "\", ");
	    responseBuilder.append("\"familyName\": \"" + familyName + "\", ");
	    responseBuilder.append("\"givenName\": \"" + givenName + "\"");
	    responseBuilder.append("}");

	    return responseBuilder.toString();
	}

}
