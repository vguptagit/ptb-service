package com.pearson.ptb.framework.exception;

import org.springframework.http.HttpStatus;
/**
 * This <code>NotFoundException</code> is used to create any custom exception to
 * throw when expectation is not met to perform the job.
 * 
 */
@SuppressWarnings("serial")
public class NotFoundException extends BaseException {

	/**
	 * This constructor generates object on the basis of two parameters.
	 * 
	 * @param entity
	 *            The entity which generated the exception.
	 */
	public NotFoundException() {
		// Call the constructor of the BaseAMSException
		this(HttpStatus.NOT_FOUND.getReasonPhrase());
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
	public NotFoundException(String message) {
		// Call the constructor of the BaseAMSException
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
	public NotFoundException(String message, Exception innerException) {
		// Call the constructor of the BaseAMSException
		super(message, innerException);
	}

}
