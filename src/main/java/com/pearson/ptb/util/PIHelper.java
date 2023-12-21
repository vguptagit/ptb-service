package com.pearson.ptb.util;

import org.apache.http.entity.ContentType;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.pearson.ptb.framework.ConfigurationManager;
/**
 * This <code>PIHelper</code> is utility for PI to get the token
 * 
 */
public class PIHelper {

	/**
	 * This method will get the token from PI
	 * 
	 * @param username
	 *            ,name of the user
	 * @param password
	 *            ,password of the user
	 */
	public String getPIToken(String username, String password) {

		String payload = "{\"userName\":\"%s\",\"password\":\"%s\"}";
		String url = ConfigurationManager.getInstance().getSysToSysPITokenUrl();
		HttpUtility util = new HttpUtility();
		HttpResponse response = util.makePost(url, null,
				ContentType.APPLICATION_JSON, null,
				String.format(payload, username, password));

		String message = response.getReponseMessage();
		JsonParser parser = new JsonParser();
		JsonElement jsonElement = parser.parse(message);

		return jsonElement.getAsJsonObject().get("data").getAsString();

	}
}
