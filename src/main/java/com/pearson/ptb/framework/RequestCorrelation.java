package com.pearson.ptb.framework;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

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
		// HttpServletRequest request = ((ServletRequestAttributes)
		// RequestContextHolder.currentRequestAttributes()).getRequest();
		// return request.getHeader(RequestCorrelation.HEADER);
		return null;
	}

}
