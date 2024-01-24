package com.pearson.ptb.util;

import java.io.IOException;

import org.apache.http.Header;
import org.apache.http.util.EntityUtils;
import org.springframework.http.HttpStatus;
/**
 * This <code>HttpResponse</code> is utility for making http response
 * 
 */
public class HttpResponse {
	/**
	 * This is the response code.
	 */
	private HttpStatus code;

	/**
	 * This is the response body.
	 */
	private String reponseMessage;

	/**
	 * This is the Apache Http Response.
	 */
	private org.apache.http.HttpResponse response = null;

	/**
	 * Empty Constructor of zero default arguments
	 */
	public HttpResponse() {
		
	}

	/**
	 * This Constructor creates an object of HttpResponse from Apache
	 * HttpResponse
	 * 
	 * @param httpResponse
	 *            The apache Http Response.
	 * @throws IOException
	 *             if an error occurs reading the input stream
	 */
	public HttpResponse(org.apache.http.HttpResponse httpResponse)
			throws IOException {
		
		this.setResponse(httpResponse);
		
		this.setReponseMessage(
				new String(EntityUtils.toByteArray(this.response.getEntity())));
		
		this.setCode(HttpStatus
				.valueOf(this.response.getStatusLine().getStatusCode()));
	
		EntityUtils.consume(httpResponse.getEntity());
	}

	/**
	 * @param code
	 *            the code to set
	 */
	public void setCode(HttpStatus code) {
		
		this.code = code;
	}

	/**
	 * @param reponseMessage
	 *            the reponseMessage to set
	 */
	public void setReponseMessage(String reponseMessage) {
		
		this.reponseMessage = reponseMessage;
	}

	/**
	 * @param httpResponse
	 *            the httpResponse to set
	 */
	public void setResponse(org.apache.http.HttpResponse httpResponse) {
		
		this.response = httpResponse;
	}

	/**
	 * This method returns the Http Status Code
	 * 
	 * @return the response code
	 */
	public HttpStatus getCode() {
		
		return this.code;
	}

	/**
	 * This method returns the response Message.
	 * 
	 * @return The reponseMessage
	 * @throws IOException
	 */
	public String getReponseMessage() {
		
		return this.reponseMessage;
	}

	/**
	 * get the response header value for the given response header key
	 * 
	 * @param key,
	 *            header response key
	 * @return
	 */
	public String getResponseHeader(String key) {
		for (Header header : this.response.getAllHeaders()) {
			if (header.getName().equals(key)) {
				return header.getValue();
			}
		}
		return "";
	}

}
