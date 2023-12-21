package com.pearson.ptb.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.pearson.ptb.framework.exception.AccessDeniedException;
import com.pearson.ptb.provider.pi.service.AuthenticationProvider;
import com.pearson.ptb.provider.pi.service.AuthenticationService;
import com.pearson.ptb.provider.pi.service.Email;
import com.pearson.ptb.provider.pi.service.UserProfile;
//import com.pearson.mytest.proxy.mytest.repo.LoginRepo;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

/**
 * Authentication of the user
 *
 */
@RestController
@Api(value = "Authenticate", description = "Get Pearson Token from Access Token")
public class AuthenticationController extends BaseController {

	@Autowired
	@Qualifier("authenticationService")
	private AuthenticationService authenticationService;

	/**
	 * Authentication of the user
	 * 
	 * @return Pearson Token as a string
	 *
	 */
	@ApiOperation(value = "Returns Pearson Token", notes = "Returns Pearson Token as a string")
	@RequestMapping(value = "/login", method = {RequestMethod.POST,
			RequestMethod.HEAD})
	@ResponseBody
	public String authenticate(HttpServletRequest request,
			HttpServletResponse response) {

		String accessToken = request.getHeader("AccessToken");

		String pearsonToken = authenticationService
				.getPITokenFromAccessToken(accessToken);

		if (request.getMethod().equals(RequestMethod.HEAD.name())) {
			response.addHeader("x-authorization", pearsonToken);
			return null;
		}
		AuthenticationProvider pi = new AuthenticationProvider();
		String extUserId = pi.authenticate(pearsonToken);

		if (!authenticationService.isIntructor(pearsonToken, extUserId)) {
			throw new AccessDeniedException(
					"Only instructors allowed. Invalid attempt by user id : "
							+ extUserId);
		}
		// int loginCount = (new LoginRepo()).logLogin(extUserId);

		UserProfile userProfile = authenticationService
				.getUserProfileFromPIApi(accessToken, extUserId);

		return buildResponse(pearsonToken, 1, userProfile);
	}

	private String buildResponse(String pearsonToken, int loginCount,
			UserProfile userProfile) {
		StringBuilder response = new StringBuilder();
		response.append("{");
		response.append("\"token\": \"" + pearsonToken + "\", ");
		response.append("\"loginCount\": \"" + loginCount + "\", ");

		List<Email> emails = userProfile.data.emails;
		for (Email email : emails) {
			if (email.isPrimary) {
				response.append(
						"\"emailAddress\": \"" + email.emailAddress + "\", ");
			}
		}

		response.append(
				"\"familyName\": \"" + userProfile.data.familyName + "\", ");
		response.append(
				"\"givenName\": \"" + userProfile.data.givenName + "\"");
		response.append("}");
		return response.toString();
	}

}
