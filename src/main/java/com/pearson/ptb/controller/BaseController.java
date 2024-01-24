package com.pearson.ptb.controller;

import java.text.ParseException;
import java.util.List;

import org.slf4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonMappingException.Reference;
import com.mongodb.MongoException;
import com.pearson.ptb.framework.CurrentThreadContext;
import com.pearson.ptb.framework.LogWrapper;
import com.pearson.ptb.framework.exception.AccessDeniedException;
import com.pearson.ptb.framework.exception.BadDataException;
import com.pearson.ptb.framework.exception.BaseException;
import com.pearson.ptb.framework.exception.ConfigException;
import com.pearson.ptb.framework.exception.DuplicateTitleException;
import com.pearson.ptb.framework.exception.ErrorResponse;
import com.pearson.ptb.framework.exception.ExpectationException;
import com.pearson.ptb.framework.exception.InternalException;
import com.pearson.ptb.framework.exception.NotFoundException;
import com.pearson.ptb.framework.exception.ServiceUnavailableException;

@RestController
public abstract class BaseController {

	/**
	 * This is the logger.
	 */
	private static final Logger LOG = LogWrapper.getInstance(BaseException.class);

	/**
	 * This is the string template for the basic error log when an exception is
	 * made.
	 * 
	 */
	private static final String BASIC_LOG_TEMPLATE = "%s ~ ExceptionClass:%s ~ Message:%s ~ StackTrace:%s";

	/**
	 * This is the string template for the detailed error log when an exception is
	 * made.
	 * 
	 */
	private static final String DETAILED_LOG_TEMPLATE = "%s ~ExceptionClass:%s ~ Message:%s ~ Exception:%s ~ StackTrace:%s";

	/**
	 * This method converts the Stack trace to the formatted string for logging.
	 * 
	 * @param throwable This is the exception being logged.
	 * @return A string which is the formatted stack trace.
	 */
	public String convertStackTraceToString(Throwable cause) {

		if (cause == null) {
			return "---nil---";
		}
		StringBuilder stringBuilder = new StringBuilder();
		
		stringBuilder.append(cause.toString() + "---");
		
		for (int i = 0; i < cause.getStackTrace().length; i++) {
			stringBuilder.append(cause.getStackTrace()[i] + "---");
		}
		
		return stringBuilder.toString();
	}

	/**
	 * This method converts an exception into a given http fault
	 * 
	 * @param exception  This is the name of the exception
	 * @param httpStatus This is the http status
	 * @return A response string with given http status
	 */
	private ResponseEntity<ErrorResponse> handleException(BaseException exception, HttpStatus httpStatus) {
		
		HttpHeaders responseHeaders = new HttpHeaders();

	
		responseHeaders.add("Content-Type", "application/json; charset=utf-8");

		
		ErrorResponse errorResponse = new ErrorResponse();

		
		errorResponse.setMessage(exception.getMessage());

	
		return new ResponseEntity<ErrorResponse>(errorResponse, responseHeaders, httpStatus);
	}

	/**
	 * This method catches an exception of type NotFoundException. Which will
	 * further map to 404 (Not Found).
	 * 
	 * @param exception This is the Exception instance which is caught.
	 * @return responseEntity: This is the response which will be sent in case of
	 *         error.
	 */
	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<ErrorResponse> notFoundExceptionHandler(NotFoundException exception) {

		return this.handleException(exception, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(DuplicateTitleException.class)
	public ResponseEntity<ErrorResponse> duplicateTitleExceptionHandler(DuplicateTitleException exception) {

		return this.handleException(exception, HttpStatus.CONFLICT);
	}

	/**
	 * This method catches an exception of type BadDataException. Which will further
	 * map to 400 (Bad Request).
	 * 
	 * @param exception This is the Exception instance which is caught.
	 * @return responseEntity: This is the response which will be sent in case of
	 *         error.
	 */
	@ExceptionHandler(BadDataException.class)
	public ResponseEntity<ErrorResponse> badDataExceptionHandler(BadDataException ex) {

		BaseController.LOG.error(String.format(BaseController.DETAILED_LOG_TEMPLATE,
				CurrentThreadContext.getUniversalId(), this.getClass().getName(), ex.getMessage(), ex.toString(),
				this.convertStackTraceToString(ex.getCause())));

		return this.handleException(ex, HttpStatus.BAD_REQUEST);
	}

	/**
	 * This method catches an exception of type AccessDeniedException. Which will
	 * further map to 401 (Un Authorized).
	 * 
	 * @param exception This is the Exception instance which is caught.
	 * @return responseEntity: This is the response which will be sent in case of
	 *         error.
	 */
	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<ErrorResponse> accessDeniedExceptionHandler(AccessDeniedException ex) {

		BaseController.LOG.error(String.format(BaseController.DETAILED_LOG_TEMPLATE,
				CurrentThreadContext.getUniversalId(), this.getClass().getName(), ex.getMessage(), ex.toString(),
				this.convertStackTraceToString(ex.getCause())));

		return this.handleException(ex, HttpStatus.UNAUTHORIZED);
	}

	/**
	 * This method catches an exception of type ServiceUnavailableException. Which
	 * will further map to 502 (Bad Gateway).
	 * 
	 * @param exception This is the Exception instance which is caught.
	 * @return responseEntity: This is the response which will be sent in case of
	 *         error.
	 */
	@ExceptionHandler(ServiceUnavailableException.class)
	public ResponseEntity<ErrorResponse> serviceUnavailableExceptionHandler(ServiceUnavailableException ex) {

		BaseController.LOG.error(String.format(BaseController.DETAILED_LOG_TEMPLATE,
				CurrentThreadContext.getUniversalId(), this.getClass().getName(), ex.getMessage(), ex.toString(),
				this.convertStackTraceToString(ex.getCause())));

		return this.handleException(ex, HttpStatus.BAD_GATEWAY);
	}

	/**
	 * This method catches an exception when expectation is not met while performing
	 * any use case.This will inform user what expectation is not met with status
	 * code 417
	 * 
	 * @param exception This is the Exception instance which is caught.
	 * @return responseEntity: This is the response which will be sent in case of
	 *         error.
	 */
	@ExceptionHandler(ExpectationException.class)
	public ResponseEntity<ErrorResponse> expectationExceptionHandler(ExpectationException exception) {

		return this.handleException(exception, HttpStatus.EXPECTATION_FAILED);
	}

	/**
	 * This method catches an exception of type JSON parse. Which will further map
	 * to 400 (Bad Request).
	 * 
	 * @param exception This is the Exception instance which is caught.
	 * @return responseEntity: This is the response which will be sent in case of
	 *         error.
	 */
	@ExceptionHandler(JsonParseException.class)
	public ResponseEntity<ErrorResponse> jsonParseExceptionExceptionHandler(JsonParseException exception) {

		return this.handleException(new BadDataException(exception.toString()), HttpStatus.BAD_REQUEST);
	}

	/**
	 * This method catches an exception of type Json mapper. Which will further map
	 * to 400 (Bad Request).
	 * 
	 * @param exception This is the Exception instance which is caught.
	 * @return responseEntity: This is the response which will be sent in case of
	 *         error.
	 */
	@ExceptionHandler(JsonMappingException.class)
	public ResponseEntity<ErrorResponse> jsonMappingExceptionExceptionHandler(JsonMappingException exception) {

		return this.handleException(new BadDataException(exception.toString()), HttpStatus.BAD_REQUEST);
	}

	/**
	 * This method catches an exception for illegal argument. Which will further map
	 * to 400 (Bad Request).
	 * 
	 * @param exception This is the Exception instance which is caught.
	 * @return responseEntity: This is the response which will be sent in case of
	 *         error.
	 */
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ErrorResponse> illegalArgumentExceptionHandler(IllegalArgumentException ex) {

		BaseController.LOG.error(String.format(BaseController.DETAILED_LOG_TEMPLATE,
				CurrentThreadContext.getUniversalId(), this.getClass().getName(), ex.getMessage(), this.toString(),
				this.convertStackTraceToString(ex.getCause())));

		return this.handleException(new BadDataException(ex.toString()), HttpStatus.BAD_REQUEST);

	}

	/**
	 * This method catches an exception for text parsing. Which will further map to
	 * 400 (Bad Request).
	 * 
	 * @param exception This is the Exception instance which is caught.
	 * @return responseEntity: This is the response which will be sent in case of
	 *         error.
	 */
	@ExceptionHandler(ParseException.class)
	public ResponseEntity<ErrorResponse> parseExceptionHandler(ParseException exception) {

		return this.handleException(new BadDataException(exception.toString()), HttpStatus.BAD_REQUEST);
	}

	/**
	 * This method catches an exception for internal server error. Which will
	 * further map to 500 (Internal server error).
	 * 
	 * @param exception This is the Exception instance which is caught.
	 * @return responseEntity: This is the response which will be sent in case of
	 *         error.
	 */
	@ExceptionHandler(InternalException.class)
	public ResponseEntity<ErrorResponse> baseInternalExceptionHandler(InternalException ex) {

		BaseController.LOG.error(String.format(BaseController.DETAILED_LOG_TEMPLATE,
				CurrentThreadContext.getUniversalId(), this.getClass().getName(), ex.getMessage(), ex.toString(),
				this.convertStackTraceToString(ex.getCause())));

		return this.handleException(ex, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	/**
	 * This method catches an exception for MongoDB not started. Which will further
	 * map to 500 (Internal server error).
	 * 
	 * @param exception This is the Exception instance which is caught.
	 * @return responseEntity: This is the response which will be sent in case of
	 *         error.
	 */
	@ExceptionHandler(MongoException.class)
	public ResponseEntity<ErrorResponse> mongoServerSelectionExceptionHandler(MongoException ex) {

		BaseController.LOG.error(String.format(BaseController.BASIC_LOG_TEMPLATE, CurrentThreadContext.getUniversalId(),
				this.getClass().getName(), ex.getMessage(), this.convertStackTraceToString(ex.getCause())));

		return this.handleException(new ServiceUnavailableException("Mongo DB not started"),
				HttpStatus.INTERNAL_SERVER_ERROR);
	}

	/**
	 * This method catches an exception while reading configuration keys . Which
	 * will further map to 500 (Internal server error).
	 * 
	 * @param exception This is the Exception instance which is caught.
	 * @return responseEntity: This is the response which will be sent in case of
	 *         error.
	 */
	@ExceptionHandler(ConfigException.class)
	public ResponseEntity<ErrorResponse> configExceptionHandler(ConfigException ex) {

		BaseController.LOG.error(String.format(BaseController.DETAILED_LOG_TEMPLATE,
				CurrentThreadContext.getUniversalId(), this.getClass().getName(), ex.getMessage(), ex.toString(),
				this.convertStackTraceToString(ex.getCause())));

		return this.handleException(ex, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	/**
	 * This method catches an exception for invalid data in @RequestBody. Which will
	 * further map to 400 (Bad Request).
	 * 
	 * @param exception This is the Exception instance which is caught.
	 * @return responseEntity: This is the response which will be sent in case of
	 *         error.
	 */
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(HttpMessageNotReadableException error) {

		String errorSource = null;
		JsonMappingException jme = (JsonMappingException) error.getCause();
		List<Reference> errorObj = jme.getPath();

		for (Reference r : errorObj) {
			errorSource = r.getFieldName();
			break;
		}
		return this.handleException(new BadDataException("Invalid data for " + errorSource), HttpStatus.BAD_REQUEST);
	}
}
