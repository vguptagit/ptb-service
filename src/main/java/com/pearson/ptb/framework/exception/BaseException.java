package com.pearson.ptb.framework.exception;

/**
 * This Class is the Exception framework for the system. All other exception
 * class needs to inherit from this class. This provide the basic exception
 * handling framework.
 * 
 * @author pranav.jain
 */
@SuppressWarnings("serial")
public abstract class BaseException extends RuntimeException {

	/**
	 * This constructors logs the basic error
	 */
	public BaseException(String message) {
		
		super(message);
	}

	public BaseException(String message, Exception innerException) {
		
		super(message, innerException);
	}

}