package com.pearson.ptb.framework.exception;

/**
 * This class will be used for throwing Service Unavailable Exception
 */
@SuppressWarnings("serial")
public class ServiceUnavailableException extends BaseException {

	/**
	 * This constructor generates object on the basis of two parameters.
	 * 
	 * @param entity
	 *            The entity which generated the exception.
	 * @param message
	 *            The custom message which needs to set while throwing the
	 *            exception
	 */
	public ServiceUnavailableException(String message) {
		
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
	public ServiceUnavailableException(String message,
			Exception innerException) {
		
		super(message, innerException);
	}
}
