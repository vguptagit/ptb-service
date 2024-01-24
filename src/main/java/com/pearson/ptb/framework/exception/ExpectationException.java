package com.pearson.ptb.framework.exception;

/**
 * This is used to create any custom exception to throw when expectation is not
 * met to perform the job.
 * 
 * @author ganapati.bhagwat
 *
 */
@SuppressWarnings("serial")
public class ExpectationException extends BaseException {
	/**
	 * This constructors logs the basic error
	 */
	public ExpectationException(String message) {
		
		super(message);
	}

	public ExpectationException(String message, Exception innerException) {
		
		super(message, innerException);

	}

}
