package com.pearson.ptb.provider.pi.service;

import java.io.IOException;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.pearson.ed.ltg.oauth.OAuthHeaderConstants;
import com.pearson.ed.ltg.oauth.util.OAuthMessageSigner;
import com.pearson.ed.ltg.oauth.util.OAuthUtil;
import com.pearson.ed.pi.encryption.exception.RemoteCallException;
import com.pearson.ed.pi.token.exception.InvalidTokenException;
import com.pearson.ed.pi.token.exception.TokenParseException;
import com.pearson.ptb.framework.ConfigurationManager;
import com.pearson.ptb.framework.LogWrapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


public class AuthFilter implements HandlerInterceptor {

	private static final Logger LOG = LogWrapper.getInstance(AuthFilter.class);

	
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		
		boolean requestStatus = false;
		try {
			
			String keyword = "/auth";
			String requestURI = request.getRequestURI();
			if (requestURI.contains(keyword)) {
				requestStatus = true;

			} else if (requestURI.contains("/books/import")) {

				SortedMap<String, String> alphaSortedMap = null;
				alphaSortedMap = new TreeMap<String, String>();

				OAuthMessageSigner signer = new OAuthMessageSigner();

				String authorizationHeader = request.getHeader("Authorization");
				if (authorizationHeader != null && authorizationHeader
						.contains(OAuthUtil.AUTH_SCHEME)) {
					Map<String, String> oauthParameters = OAuthUtil
							.decodeAuthorization(authorizationHeader);

					
					oauthParameters.remove("realm");

					alphaSortedMap.putAll(oauthParameters);
				}

				
				String secret = ConfigurationManager.getInstance()
						.getQUADDataSecretKey();
				String url = request.getRequestURL().toString();
				String signature = alphaSortedMap
						.remove(OAuthHeaderConstants.SIGNATURE_PARAM);
				String calculatedSignature = null;
				String method = request.getMethod();

				calculatedSignature = signer.sign(secret,
						OAuthUtil.mapToJava(alphaSortedMap.get(
								OAuthHeaderConstants.SIGNATURE_METHOD_PARAM)),
						method, url, alphaSortedMap);

				if (signature.equals(calculatedSignature)) {
					requestStatus = true;
				}

			} else {

				String piAuthtoken = request.getHeader("x-authorization");
				
				if (piAuthtoken == null || piAuthtoken.isEmpty()) {
					response.setStatus(HttpStatus.BAD_REQUEST.value());
					response.getWriter()
							.write("x-authorization header not found");

					
					requestStatus = false;
				} else {
					AuthenticationProvider pi = new AuthenticationProvider();
					request.setAttribute("extUserId",
							pi.authenticate(piAuthtoken));

					
					requestStatus = true;
				}

			}
		} catch (TokenParseException | InvalidTokenException e) {

			requestStatus = logError(e, response, HttpStatus.FORBIDDEN.value(),
					"Invalid PI Token in x-authorization");

		} catch (RemoteCallException e) {
			requestStatus = logError(e, response,
					HttpStatus.BAD_GATEWAY.value(),
					"Error calling DataSecure REST");

		} catch (Exception e) {
			if ((e.getCause() != null)
					&& e.getCause().getClass().getSimpleName()
							.contains("MongoServerSelectionException")) {
				requestStatus = logError(e, response,
						HttpStatus.INTERNAL_SERVER_ERROR.value(),
						"Mongo DB not started");
			} else {
				requestStatus = logError(e, response,
						HttpStatus.INTERNAL_SERVER_ERROR.value(),
						"Unknown Exception");
			}
		}
		return requestStatus;
	}

	public boolean logError(Exception e, HttpServletResponse response,
			int httpStatus, String exceptionMessage) throws IOException {
		LOG.error("Error is logged from AuthFilter.preHandle() method", e);
		response.setStatus(httpStatus);
		response.getWriter().write(exceptionMessage);
		
		return false;
	}

	/**
	 * This method is part of handler not required for MyTest
	 */
	
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		
	}

	/**
	 * This method is part of handler not required for MyTest
	 */
	
//	@Bean
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		
	}

}
