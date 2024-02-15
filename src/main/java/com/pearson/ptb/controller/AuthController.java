package com.pearson.ptb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pearson.ptb.provider.pi.service.AuthService;
import com.pearson.ptb.response.AuthResponse;

import jakarta.servlet.http.HttpServletRequest;

/***
 * 
 * @author manojkumar.ns
 *
 */

@RestController
public class AuthController {

	@Autowired
	private AuthService authService;

	private static final String ACCESS_TOKEN = "AccessToken";
	private static final String USER_ID = "UserId";



	@GetMapping("/auth")
	public ResponseEntity<String> auth(HttpServletRequest request) {
		String accessToken = request.getHeader(ACCESS_TOKEN);
		String userId = request.getHeader(USER_ID);

		// Delegate business logic to service layer
		AuthResponse authResponse = authService.authenticateUser(userId, accessToken);

		if (authResponse.isSuccess()) {
			return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(authResponse.getJsonResponse());
		} else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error accessing resource");
		}
	}
	
	

	

}
