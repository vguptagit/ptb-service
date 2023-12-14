package com.pearson.ptb.framework.exception;

/**
 * This Class is the Exception framework class to throw paf oauth toke generator
 * exception.
 * 
 * @author nithinjain
 */
@SuppressWarnings("serial")
public class AuthTokenException extends InternalException {

	/**
	 * This constructor generates object on the basis of two parameters.
	 * 
	 * @param message
	 *            The custom message which needs to set while throwing the
	 *            exception
	 */
	public AuthTokenException(String message) {
		super(message);

	}

	/**
	 * This constructor generates object on the basis of two parameters.
	 * 
	 * @param message
	 *            The custom message which needs to set while throwing the
	 *            exception
	 * @param innerException
	 *            The instance of the internal exception.
	 */
	public AuthTokenException(String message, Exception innerException) {
		super(message, innerException);

	}

}
