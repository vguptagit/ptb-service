package com.pearson.ptb.framework.exception;

/**
 * This Class is the base Exception framework for the mytest exception. All
 * other exception class needs to inherit from this class. This is used to
 * create any custom exception to throw server error.
 * 
 * @author nithinjain
 */
@SuppressWarnings("serial")
public class InternalException extends BaseException {

	/**
	 * This constructors logs the basic error
	 */
	public InternalException(String message) {
		
		super(message);
	}

	public InternalException(String message, Exception innerException) {
		
		super(message, innerException);

	}
}
