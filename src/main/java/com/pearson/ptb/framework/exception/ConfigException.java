package com.pearson.ptb.framework.exception;

import org.springframework.http.HttpStatus;
/**
 * This <code>ConfigException</code> is used for throwing configuration related
 * Exception
 * 
 */
@SuppressWarnings("serial")
public class ConfigException extends BaseException {

	/**
	 * This constructor generates object on the basis of two parameters.
	 * 
	 * @param entity
	 *            The entity which generated the exception.
	 */
	public ConfigException() {
		// Call the constructor of the BaseException
		this(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
	}

	/**
	 * This constructor generates object on the basis of two parameters.
	 * 
	 * @param entity
	 *            The entity which generated the exception.
	 * @param message
	 *            The custom message which needs to set while throwing the
	 *            exception
	 */
	public ConfigException(String message) {
		// Call the constructor of the BaseException
		super(message);
	}

	/**
	 * This constructor generates object on the basis of three parameters.
	 * 
	 * @param entity
	 *            The entity which generated the exception.
	 * @param message
	 *            The custom message which needs to set while throwing the
	 *            exception
	 * @param innerException
	 *            The instance of the internal exception.
	 */
	public ConfigException(String message, Exception innerException) {
		// Call the constructor of the BaseException
		super(message, innerException);
	}

}
