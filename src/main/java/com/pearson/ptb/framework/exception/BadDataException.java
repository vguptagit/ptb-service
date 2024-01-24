package com.pearson.ptb.framework.exception;

/**
 * This class will be used for throwing Bad Data Exception
 * 
 * @author pranav.jain
 */
@SuppressWarnings("serial")
public class BadDataException extends BaseException {

	/**
	 * This constructor generates object on the basis of two parameters.
	 * 
	 * @param entity
	 *            The entity which generated the exception.
	 * @param message
	 *            The custom message which needs to set while throwing the
	 *            exception
	 * @author pranav.jain
	 */
	public BadDataException(String message) {
		
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
	 * @author pranav.jain
	 */
	public BadDataException(String message, Exception innerException) {
		
		super(message, innerException);
	}

}
