package com.pearson.ptb.provider.pi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.pearson.ptb.framework.ConfigurationManager;
import com.pearson.ptb.proxy.repo.LoginRepo;
import com.pearson.ptb.response.AuthResponse;

@Service
public class AuthService {

    @Autowired
    private LoginRepo loginRepo;

	private static final String AUTHORIZATION = "x-Authorization";

    @Autowired
    private RestTemplate restTemplate;

    public AuthResponse authenticateUser(String userId, String accessToken) {
        int loginCount = loginRepo.logLogin(userId);

        String targetURL = ConfigurationManager.getInstance()
				.getSysToSysPITokenFromAccessTokenUrl();
        String url = targetURL + userId;

        HttpHeaders headers = new HttpHeaders();
        headers.set(AUTHORIZATION, accessToken);

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    url, HttpMethod.GET, requestEntity, String.class);

            String body = responseEntity.getBody();
            String jsonResponse = buildResponse(accessToken, loginCount, body);

            return new AuthResponse(true, jsonResponse);
        } catch (Exception e) {
            e.printStackTrace();
            return new AuthResponse(false, null);
        }
    }

    private String buildResponse(String accessToken, int loginCount, String responseBody) {
		JsonElement jsonElement = JsonParser.parseString(responseBody);
		JsonObject jsonObject = jsonElement.getAsJsonObject().getAsJsonObject("data");

		String givenName = jsonObject.get("givenName").getAsString();
		String familyName = jsonObject.get("familyName").getAsString();
		String emailAddress = jsonObject.getAsJsonArray("emails").get(0).getAsJsonObject().get("emailAddress")
				.getAsString();

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
