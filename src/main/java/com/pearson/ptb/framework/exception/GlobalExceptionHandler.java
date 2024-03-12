package com.pearson.ptb.framework.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.pearson.ptb.dtos.ApiResponseMessage;

@RestControllerAdvice
public class GlobalExceptionHandler {

	private Logger logger= LoggerFactory.getLogger(GlobalExceptionHandler.class);
	
	@ExceptionHandler({ResourceNotFoundException.class})
	public ResponseEntity<ApiResponseMessage> resourceResponseMessage(ResourceNotFoundException exception){
		ApiResponseMessage apiResponseMessage = ApiResponseMessage.builder().message(exception.getMessage()).status(HttpStatus.NOT_FOUND).success(false).build();
		logger.info("Exception handler invocked!!!");
		return new ResponseEntity<>(apiResponseMessage , HttpStatus.NOT_FOUND);
	}
}
