package com.pearson.ptb.framework;

public final class RequestCorrelation {

	public static final String HEADER = "Correlation-Id";

	/*
	 * This method reads the Correlation Id from the request header. This
	 * Correlation Id is added in the request header while calling api service
	 * from the client side. Correlation Id has been logging on each api call
	 * and also adding to the request header while calling to the third party
	 * api's like PAF,PI & EPS.
	 */
	public static String getId() {
		
		return null;
	}

}
