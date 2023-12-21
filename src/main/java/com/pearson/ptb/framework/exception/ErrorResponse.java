/**
 * 
 */
package com.pearson.ptb.framework.exception;

import java.io.Serializable;

public class ErrorResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * This is the message
	 */
	private String message;

	/**
	 * Gets the message
	 * 
	 * @return the message
	 */
	public String getMessage() {
		// return message
		return this.message;
	}

	/**
	 * Sets the message
	 * 
	 * @param message
	 *            the message to set
	 */
	public void setMessage(String message) {
		// set the message
		this.message = message;
	}

}
