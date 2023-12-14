package com.pearson.ptb.provider.pi.service;

import java.io.IOException;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
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

@ComponentScan
public class AuthFilter implements HandlerInterceptor {

	private static final Logger LOG = LogWrapper.getInstance(AuthFilter.class);
		
//	@Override
	@Bean
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		boolean requestStatus = false;
		try {			
			//This is for download usecase
			String keyword = "/books/import";
			String requestURI = request.getRequestURI();
			if (requestURI.contains(keyword)) {
				requestStatus = true;
				
			}else if(requestURI.contains("/books/import")){     

				SortedMap<String, String> alphaSortedMap = null;
				alphaSortedMap = new TreeMap<String, String>();

				OAuthMessageSigner signer = new OAuthMessageSigner();

				String authorizationHeader = request.getHeader("Authorization");
				if (authorizationHeader != null && authorizationHeader.contains(OAuthUtil.AUTH_SCHEME)) {
					Map<String, String> oauthParameters = OAuthUtil.decodeAuthorization(authorizationHeader);

					// if realm is in here get rid of it
					oauthParameters.remove("realm");

					alphaSortedMap.putAll(oauthParameters);
				}

				//This secret key is from QUAD.
				String secret = ConfigurationManager.getInstance().getQUADDataSecretKey();				
				String url = request.getRequestURL().toString();
				String signature = alphaSortedMap.remove(OAuthHeaderConstants.SIGNATURE_PARAM);
				String calculatedSignature = null;
				String method = request.getMethod();    


				calculatedSignature = signer.sign(secret,OAuthUtil.mapToJava(alphaSortedMap
										.get(OAuthHeaderConstants.SIGNATURE_METHOD_PARAM)),
										method, url, alphaSortedMap);
				
				if (signature.equals(calculatedSignature)) {
					requestStatus = true;
				}


			} else {

				String piAuthtoken = request.getHeader("x-authorization");

				if (piAuthtoken == null || piAuthtoken.isEmpty()) {
					response.setStatus(HttpStatus.BAD_REQUEST.value());
					response.getWriter().write("x-authorization header not found");

					// abort the request
					requestStatus = false;
				}else {
				AuthenticationProvider pi = new AuthenticationProvider();
				request.setAttribute("extUserId", pi.authenticate(piAuthtoken));

				// continue with the request
				requestStatus = true;
				}

			}
		} catch (TokenParseException | InvalidTokenException e) {

			requestStatus = logError(e, response, HttpStatus.FORBIDDEN.value(),"Invalid PI Token in x-authorization");

		} catch (RemoteCallException e) {
			requestStatus = logError(e, response,HttpStatus.BAD_GATEWAY.value(),"Error calling DataSecure REST");

		} catch (Exception e) {
			if ((e.getCause() != null)
					&& e.getCause().getClass().getSimpleName().contains("MongoServerSelectionException")) {
				requestStatus = logError(e, response, HttpStatus.INTERNAL_SERVER_ERROR.value(),"Mongo DB not started");
			} else {
				requestStatus = logError(e, response, HttpStatus.INTERNAL_SERVER_ERROR.value(),"Unknown Exception");
			}
		}
		return requestStatus;
	}

	@Bean
	private boolean logError(Exception e,HttpServletResponse response,int httpStatus,String exceptionMessage) throws IOException{
		LOG.error("Error is logged from AuthFilter.preHandle() method",e);
		response.setStatus(httpStatus);
		response.getWriter().write(exceptionMessage);
		// abort the request
		return false;
	}

	/**
	 * This method is part of handler not required for MyTest
	 */
	//@Override
	@Bean
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		//This is part of handler not required for MyTest		
	}

	/**
	 * This method is part of handler not required for MyTest
	 */
	//@Override
	@Bean
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		//This is part of handler not required for MyTest		
	}	

}
