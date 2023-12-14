package com.pearson.ptb.provider.pi.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.entity.ContentType;

import com.google.gson.Gson;
import com.pearson.ptb.framework.ConfigurationManager;
import com.pearson.ptb.framework.exception.ConfigException;
import com.pearson.ptb.framework.exception.InternalException;
import com.pearson.ptb.util.HttpResponse;
import com.pearson.ptb.util.HttpUtility;

import org.springframework.stereotype.Service;

/**
 * This <code>AuthenticationService</code> is responsible for authenticating access token to get the PI token
 * and validates the instructors.
 */
@Service("authenticationService")
public class AuthenticationService {

	
	private static final String CONFIG_PROPERTY_EXCEPTION = "Unable to read properties from config";
	
	/**
	 * this method will get the PI token from PI using access token.
	 * 
	 * @param  accessToken
	 * @return String.
	 */
	public String getPITokenFromAccessToken(String accessToken){

		try {

			String targetURL = ConfigurationManager.getInstance().getSysToSysPITokenFromAccessTokenUrl();
			
			Map<String, String> headers = new HashMap<String, String>();
			headers.put("Authorization", "Bearer " + accessToken);
			
			HttpResponse response = null;
			
			response = getResponseByMakeGet(targetURL, headers, response);
			
			String json = response.getReponseMessage();
			PIJson piJson = new Gson().fromJson(json, PIJson.class);

			return piJson.pi_token;
		} catch (ConfigException ex) {
			throw new InternalException(CONFIG_PROPERTY_EXCEPTION, ex);
		}
	}

	private HttpResponse getResponseByMakeGet(String targetURL,
			Map<String, String> headers, HttpResponse response) {
		try {
			response = (new HttpUtility()).makeGet(targetURL.trim(), headers, ContentType.APPLICATION_JSON, null);
		} catch (Exception ex) {
			throw new InternalException("Failed response from PIToken_From_AccessToken call", ex);
		}
		return response;
	}
	
	/**
	 * this method validates the user based on PI token.
	 * 
	 * @param  piToken
	 * @param extUserId
	 * 					,represents existing user.
	 * @return Boolean.
	 */
	public Boolean isIntructor(String piToken, String extUserId){

		Boolean isIntructor = false;
		try {

			String targetURL = String.format(ConfigurationManager.getInstance().getInstructorAuthUrl(), extUserId);
			
			Map<String, String> headers = new HashMap<String, String>();
			headers.put("x-authorization", piToken);
			
			HttpResponse response = null;
			
			response = getResponseByMakeGet(targetURL, headers, response);
			
			String json = response.getReponseMessage(); 
			Group[] groupsJson = new Gson().fromJson(json, Group[].class);

			for(Group group : groupsJson) {
				if(group.id.equals(ConfigurationManager.getInstance().getInstructorGroupId())) {
					isIntructor = true;
				}
			}
			return isIntructor;
		} catch (ConfigException ex) {
			throw new InternalException(CONFIG_PROPERTY_EXCEPTION, ex);
		}
	}
	
	/**
	 * this method will get the profile of the existing user from PI.
	 * 
	 * @param accessToken
	 * @param extUserId
	 * 					,represents existing user.
	 * @return UserProfile.
	 */
	public UserProfile getUserProfileFromPIApi(String accessToken, String extUserId){

		try {
			String targetURL = ConfigurationManager.getInstance().getSysToSysUserProfileFromAccessTokenUrl() + "/" + extUserId;
			
			Map<String, String> headers = new HashMap<String, String>();
			headers.put("Authorization", "Bearer " + accessToken);
			
			HttpResponse response = null;
			
			response = getResponseByMakeGetMethod(targetURL, headers, response);
			
			String json = response.getReponseMessage();				
			
			return new Gson().fromJson(json, UserProfile.class);
		} catch (ConfigException ex) {
			throw new InternalException(CONFIG_PROPERTY_EXCEPTION, ex);
		}
	}

	private HttpResponse getResponseByMakeGetMethod(String targetURL,
			Map<String, String> headers, HttpResponse response)
			throws InternalException {
		try {
			response = getResponseByMakeGet(targetURL, headers, response);
		} catch (Exception ex) {
			throw new InternalException("Failed response from UserProfile_From_AccessToken call", ex);
		}
		return response;
	}
	
	class PIJson {		
		String pi_token; // NOSONAR as this property name is as per the json returned by PI api
	}
	
	class Groups {
		List<Group> group;
	}
	class Group {
		String id;
		String name;
	}
		
}
