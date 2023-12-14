package com.pearson.ptb.framework.exception;

/**
 * This class will be used for throwing Access Denied Exception
 */
@SuppressWarnings("serial")
public class AccessDeniedException extends BaseException {

	/**
	 * This constructor generates object on the basis of two parameters.
	 * 
	 * @param entity
	 *            The entity which generated the exception.
	 * @param message
	 *            The custom message which needs to set while throwing the
	 *            exception
	 */
	public AccessDeniedException(String message) {
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
	public AccessDeniedException(String message, Exception innerException) {
		// Call the constructor of the BaseAMSException
		super(message, innerException);
	}
}
