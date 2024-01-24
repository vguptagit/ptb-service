package com.pearson.ptb.aspect;

import java.io.IOException;
import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;

import com.pearson.ptb.framework.ConfigurationManager;
import com.pearson.ptb.framework.LogWrapper;
import com.pearson.ptb.framework.RequestCorrelation;
import com.pearson.ptb.framework.exception.ConfigException;

/**
 * It handles various logging aspects;i.e. Logging at the start and end of a
 * function, Logging while there is an exception or error.
 * 
 * @author nithinjain
 */
@Aspect
public class LoggingAspect {

	/**
	 * Logger instance for the class
	 */
	private static final Logger LOG = LogWrapper
			.getInstance(LoggingAspect.class);

	/**
	 * Log template for the debug log when method is called
	 */
	private static final String TRACE_ENTRY_LOG_TEMPLATE = "Entry %s ~ %s ~ %s ~ %s ~ ";

	/**
	 * Log template for the debug log when method returns.
	 */
	private static final String EXIT_LOG_TEMPLATE = "Exit %s ~ %s ~ %s ~ %s ~ ";

	/**
	 * Log template for exception log
	 */
	private static final String ERROR_LOG_TEMPLATE = "Exception %s ~ %s ~ %s ~ %s ~ ";

	/**
	 * Logs debug message when a method corresponding to given point cut is
	 * called in any package.
	 * 
	 * @param joinPoint
	 *            join point being logged. It contains the caller information
	 * @throws ConfigException
	 */
	@Before("execution(* com.pearson.mytest.controller.*.*(..))")
	public void traceEntry(JoinPoint joinPoint) {

		
		if (ConfigurationManager.getInstance().isDebugEnabled()) {
			
			LoggingAspect.LOG
					.debug(String.format(LoggingAspect.TRACE_ENTRY_LOG_TEMPLATE,
							joinPoint.getTarget().getClass(),
							joinPoint.getSignature().getName(),
							Arrays.toString(joinPoint.getArgs()),
							RequestCorrelation.getId()));
		}
	}

	/**
	 * Logs debug message when a method corresponding to given point cut is
	 * returns in any package.
	 * 
	 * @param joinPoint
	 *            join point being logged. It contains the caller information.
	 * @param result
	 *            result returned from the method.
	 * @throws ConfigException
	 */
	@AfterReturning(pointcut = "execution(* com.pearson.mytest.controller.*.*(..))", returning = "result")
	public void traceExit(JoinPoint joinPoint, Object result) {

		
		if (ConfigurationManager.getInstance().isDebugEnabled()) {
			
			LoggingAspect.LOG
					.debug(String.format(LoggingAspect.EXIT_LOG_TEMPLATE,
							joinPoint.getTarget().getClass(),
							joinPoint.getSignature().getName(), result,
							RequestCorrelation.getId()));
		}
	}

	/**
	 * Logs an error message when a method corresponding to given point cut
	 * throw an exception in any package.
	 * 
	 * @param joinPoint
	 *            join point being logged. It contains the caller information
	 * @param throwable
	 *            exception that occurred.
	 * @throws IOException
	 *             This is thrown if there is an error in the I/O while reading
	 *             property file.
	 */
	@AfterThrowing(pointcut = "execution(* com.pearson.mytest.controller.*.*(..))", throwing = "throwable")
	public void traceException(JoinPoint joinPoint, Throwable throwable) {

		
		LoggingAspect.LOG.error(String.format(LoggingAspect.ERROR_LOG_TEMPLATE,
				joinPoint.getTarget().getClass(),
				joinPoint.getSignature().getName(),
				this.convertStackTraceToString(throwable),
				RequestCorrelation.getId()));

	}

	/**
	 * Converts the stack trace to the formatted string for logging.
	 * 
	 * @param throwable
	 *            exception being logged.
	 * @return the formatted stack trace.
	 */
	public String convertStackTraceToString(Throwable throwable) {

		
		StringBuilder stackTraceBuilder = new StringBuilder();
		stackTraceBuilder.append(throwable.toString() + "---");

		
		for (int i = 0; i < throwable.getStackTrace().length; i++) {
			stackTraceBuilder.append(throwable.getStackTrace()[i] + "---");
		}

		
		return stackTraceBuilder.toString();
	}
}