package com.pearson.ptb.framework;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;

/**
 * This class will be used as the wrapper of the Slf4jLog class.
 * 
 * @author History:
 */
public final class LogWrapper implements Logger {

	/**
	 * This is the logger.
	 * 
	 * @author
	 */
	private Logger log;

	/**
	 * This is the exception log template
	 */
	private static final String EXCEPTION_LOG_TEMPLATE = "%s ~ %s ~ %s ~ %s ~ Exception";

	/**
	 * This is the string template for the debug log when method returns.
	 * 
	 * @author
	 */
	private static final String TRACE_EXIT_LOG_TEMPLATE = "%s ~ %s ~ %s ~ %s ~ Exit";

	/**
	 * This is the string template for the debug log when method is called.
	 * 
	 * @author
	 */
	private static final String TRACE_ENTRY_LOG_TEMPLATE = "%s ~ %s ~ %s ~ %s ~ Entry";

	/**
	 * This is the string template for the exception log.
	 * 
	 * @author
	 * 
	 *         / private static final String EXCEPTION_LOG_TEMPLATE = "%s ~ %s ~
	 *         %s ~ Exception";
	 * 
	 *         /** This is the string template for the Controller Entry log.
	 * @author
	 */
	private static final String TRACE_CONTROLLER_ENTRY_LOG_TEMPLATE = "%s ~ %s ~ %s ~ %s ~ %s ~ %s ~ Controller ~ Entry";

	/**
	 * This is used for API
	 */
	private static final String LOG_API_TEMPLATE = "%s ~ %s ~ %s ~ %s";

	/**
	 * This is used for CQL
	 */
	private static final String LOG_CQL = " CQL ~ %s ~  ~ %s ~ %d";

	/**
	 * This is the string template for the Controller Exit log.
	 * 
	 * @author
	 */
	private static final String TRACE_CONTROLLER_EXIT_LOG_TEMPLATE = "%s ~ %s ~ %s ~ %s ~ %s ~ %s ~ %s ~ Controller ~ Exit";

	/**
	 * This is the private constructor of the class.
	 * 
	 * @author
	 */
	private LogWrapper() {

	}

	/**
	 * This method returns the instance of the Logger.
	 * 
	 * @return logger returns the instance of Logger class.
	 * @param class
	 *            the returned logger will be named after class.
	 * @author
	 */
	@SuppressWarnings("rawtypes")
	public static LogWrapper getInstance(Class clazz) {
		
		LogWrapper logWrapper = new LogWrapper();
		
		logWrapper.log = LoggerFactory.getLogger(clazz);
		
		return logWrapper;
	}

	/**
	 * @see Logger.getName
	 * @author
	 */
	@Override
	public String getName() {
		return log.getName();
	}

	/**
	 * @see Logger.isTraceEnabled
	 * @author
	 */
	@Override
	public boolean isTraceEnabled() {
		return log.isTraceEnabled();
	}

	/**
	 * @see Logger.trace
	 * @author
	 */
	@Override
	public void trace(String message) {
		
		log.trace(message);
	}

	/**
	 * @see Logger.trace
	 * @author
	 */
	@Override
	public void trace(String format, Object arg) {
		
		log.trace(format, arg);

	}

	/**
	 * @see Logger.trace
	 * @author
	 */
	@Override
	public void trace(String format, Object arg1, Object arg2) {
		
		log.trace(format, arg1, arg2);

	}

	/**
	 * @see Logger.trace
	 * @author
	 */
	@Override
	public void trace(String format, Object... argArray) {
		
		log.trace(format, argArray);
	}

	/**
	 * @see Logger.trace
	 * @author
	 */
	@Override
	public void trace(String message, Throwable throwable) {
		
		log.trace(message, throwable);
	}

	/**
	 * @see isTraceEnabled()
	 * @author
	 */
	@Override
	public boolean isTraceEnabled(Marker marker) {
		return log.isTraceEnabled(marker);
	}

	/**
	 * @see Logger.trace
	 * @author
	 */
	@Override
	public void trace(Marker marker, String message) {
		
		log.trace(marker, message);
	}

	/**
	 * @see Logger.trace
	 * @author
	 */
	@Override
	public void trace(Marker marker, String format, Object arg) {
		
		log.trace(marker, format, arg);

	}

	/**
	 * @see Logger.trace
	 * @author
	 */
	@Override
	public void trace(Marker marker, String format, Object arg1, Object arg2) {
		
		log.trace(marker, format, arg1, arg2);
	}

	/**
	 * @see Logger.trace
	 * @author
	 */
	@Override
	public void trace(Marker marker, String format, Object... argArray) {
		
		log.trace(marker, format, argArray);
	}

	/**
	 * @see Logger.trace
	 * @author
	 */
	@Override
	public void trace(Marker marker, String message, Throwable throwable) {
		
		log.trace(marker, message, throwable);

	}

	/**
	 * @see Logger.isDebugEnabled
	 * @author
	 */
	@Override
	public boolean isDebugEnabled() {
		return log.isDebugEnabled();
	}

	/**
	 * @see Logger.debug
	 * @author
	 */
	@Override
	public void debug(String message) {
		log.debug(message);
	}

	/**
	 * @see Logger.debug
	 * @author
	 */
	@Override
	public void debug(String format, Object arg) {
		
		log.debug(format, arg);
	}

	/**
	 * @see Logger.debug
	 * @author
	 */
	@Override
	public void debug(String format, Object arg1, Object arg2) {
		
		log.debug(format, arg1, arg2);

	}

	/**
	 * @see Logger.debug
	 * @author
	 */
	@Override
	public void debug(String format, Object... argArray) {
		
		log.debug(format, argArray);

	}

	/**
	 * @see Logger.debug
	 * @author
	 */
	@Override
	public void debug(String message, Throwable throwable) {
		
		log.debug(message, throwable);

	}

	/**
	 * @see Logger.isDebugEnabled
	 * @author
	 */
	@Override
	public boolean isDebugEnabled(Marker marker) {
		return log.isDebugEnabled(marker);
	}

	/**
	 * @see Logger.debug
	 * @author
	 */
	@Override
	public void debug(Marker marker, String message) {
		
		log.debug(marker, message);
	}

	/**
	 * @see Logger.debug
	 * @author
	 */
	@Override
	public void debug(Marker marker, String format, Object argument) {
		
		log.debug(marker, format, argument);

	}

	/**
	 * @see Logger.debug
	 * @author
	 */
	@Override
	public void debug(Marker marker, String format, Object arg1, Object arg2) {
		
		log.debug(marker, format, arg1, arg2);
	}

	/**
	 * @see Logger.debug
	 * @author
	 */
	@Override
	public void debug(Marker marker, String format, Object... argArray) {
		
		log.debug(marker, format, argArray);

	}

	/**
	 * @see Logger.debug
	 * @author
	 */
	@Override
	public void debug(Marker marker, String msg, Throwable throwable) {
		
		log.debug(marker, msg, throwable);

	}

	/**
	 * @see Logger.isInfoEnabled
	 * @author
	 */
	@Override
	public boolean isInfoEnabled() {
		return log.isInfoEnabled();
	}

	/**
	 * @see Logger.info
	 * @author
	 */
	@Override
	public void info(String message) {
		
		log.info(message);
	}

	/**
	 * @see Logger.info
	 * @author
	 */
	@Override
	public void info(String format, Object arg) {
		
		log.info(format, arg);

	}

	/**
	 * @see Logger.info
	 * @author
	 */
	@Override
	public void info(String format, Object arg1, Object arg2) {
	
		log.info(format, arg1, arg2);
	}

	/**
	 * @see Logger.info
	 * @author
	 */
	@Override
	public void info(String format, Object... argArray) {
		
		log.info(format, argArray);

	}

	/**
	 * @see Logger.info
	 * @author
	 */
	@Override
	public void info(String message, Throwable throwable) {
		
		log.info(message, throwable);

	}

	/**
	 * @see Logger.isInfoEnabled
	 * @author
	 */
	@Override
	public boolean isInfoEnabled(Marker marker) {
		return log.isInfoEnabled(marker);
	}

	/**
	 * @see Logger.info
	 * @author
	 */
	@Override
	public void info(Marker marker, String message) {
		
		log.info(marker, message);
	}

	/**
	 * @see Logger.info
	 * @author
	 */
	@Override
	public void info(Marker marker, String format, Object arg) {
		
		log.info(marker, format, arg);

	}

	/**
	 * @see Logger.info
	 * @author
	 */
	@Override
	public void info(Marker marker, String format, Object arg1, Object arg2) {
		
		log.info(marker, format, arg1, arg2);

	}

	/**
	 * @see Logger.info
	 * @author
	 */
	@Override
	public void info(Marker marker, String format, Object... argArray) {
		
		log.info(marker, format, argArray);

	}

	/**
	 * @see Logger.info
	 * @author
	 */
	@Override
	public void info(Marker marker, String messsage, Throwable throwable) {
	
		log.info(marker, messsage, throwable);

	}

	/**
	 * @see Logger.isWarnEnabled
	 * @author
	 */
	@Override
	public boolean isWarnEnabled() {
		return log.isWarnEnabled();
	}

	/**
	 * @see Logger.warn
	 * @author
	 */
	@Override
	public void warn(String message) {
		
		log.warn(message);
	}

	/**
	 * @see Logger.warn
	 * @author Method History:
	 */
	@Override
	public void warn(String format, Object arg) {
		
		log.warn(format, arg);
	}

	/**
	 * @see Logger.warn
	 * @author
	 */
	@Override
	public void warn(String format, Object... argArray) {
		
		log.warn(format, argArray);
	}

	/**
	 * @see Logger.warn
	 * @author
	 */
	@Override
	public void warn(String format, Object arg1, Object arg2) {
		
		log.warn(format, arg1, arg2);
	}

	/**
	 * @see Logger.warn
	 * @author
	 */
	@Override
	public void warn(String message, Throwable throwable) {
		
		log.warn(message, throwable);
	}

	/**
	 * @see Logger.isWarnEnabled
	 * @author
	 */
	@Override
	public boolean isWarnEnabled(Marker marker) {
		return log.isWarnEnabled(marker);
	}

	/**
	 * @see Logger.warn
	 * @author
	 */
	@Override
	public void warn(Marker marker, String message) {
		
		log.warn(marker, message);
	}

	/**
	 * @see Logger.warn
	 * @author
	 */
	@Override
	public void warn(Marker marker, String format, Object arg) {
		
		log.warn(marker, format, arg);

	}

	/**
	 * @see Logger.warn
	 * @author
	 */
	@Override
	public void warn(Marker marker, String format, Object arg1, Object arg2) {
		
		log.warn(marker, format, arg1, arg2);
	}

	/**
	 * @see Logger.warn
	 * @author
	 */
	@Override
	public void warn(Marker marker, String format, Object... argArray) {
		
		log.warn(marker, format, argArray);
	}

	/**
	 * @see Logger.warn
	 * @author
	 */
	@Override
	public void warn(Marker marker, String message, Throwable throwable) {
		
		log.warn(marker, message, throwable);
	}

	/**
	 * @see Logger.isErrorEnabled
	 * @author
	 */
	@Override
	public boolean isErrorEnabled() {
		return log.isErrorEnabled();
	}

	/**
	 * @see Logger.error
	 * @author
	 */
	@Override
	public void error(String message) {
		log.error(message);
	}

	/**
	 * @see Logger.error
	 * @author
	 */
	@Override
	public void error(String format, Object arg) {
		
		log.error(format, arg);

	}

	/**
	 * @see Logger.error
	 * @author
	 */
	@Override
	public void error(String format, Object arg1, Object arg2) {
		
		log.error(format, arg1, arg2);
	}

	/**
	 * @see Logger.error
	 * @author
	 */
	@Override
	public void error(String format, Object... argArray) {
		
		log.error(format, argArray);
	}

	/**
	 * @see Logger.error
	 * @author
	 */
	@Override
	public void error(String message, Throwable throwable) {
	
		log.error(message, throwable);

	}

	/**
	 * @see Logger.isErrorEnabled
	 * @author
	 */
	@Override
	public boolean isErrorEnabled(Marker marker) {
		return log.isErrorEnabled(marker);
	}

	/**
	 * @see Logger.error
	 * @author
	 */
	@Override
	public void error(Marker marker, String message) {
		
		log.error(marker, message);

	}

	/**
	 * @see Logger.error
	 * @author
	 */
	@Override
	public void error(Marker marker, String format, Object arg) {
		
		log.error(marker, format, arg);
	}

	/**
	 * @see Logger.error
	 * @author
	 */
	@Override
	public void error(Marker marker, String format, Object arg1, Object arg2) {
		
		log.error(marker, format, arg1, arg2);
	}

	/**
	 * @see Logger.error
	 * @author
	 */
	@Override
	public void error(Marker marker, String format, Object... argArray) {
		
		log.error(marker, format, argArray);
	}

	/**
	 * @see Logger.error
	 * @author
	 */
	@Override
	public void error(Marker marker, String message, Throwable throwable) {
		
		log.error(marker, message, throwable);

	}

	/**
	 * This method logs debug message when method is called.
	 * 
	 * @param classname
	 *            The class name which needs to be logged.
	 * @param methodName
	 *            The method name which needs to be logged.
	 * @param parameters
	 *            The array of arguments which needs to be logged.
	 * @author
	 */
	public void logTraceMethodEntry(String classname, String methodName,
			Object[] parameters) {
		
		log.debug(String.format(LogWrapper.TRACE_ENTRY_LOG_TEMPLATE,
				CurrentThreadContext.getUniversalId(), classname, methodName,
				Arrays.toString(parameters)));
	}

	/**
	 * This method logs debug message when method returns.
	 * 
	 * @param classname
	 *            The class name which needs to be logged.
	 * @param methodName
	 *            The method name which needs to be logged.
	 * @param returnvalue
	 *            The Object which is returned from the target method.
	 * @author
	 */
	public void logTraceMethodExit(String classname, String methodName,
			Object returnvalue) {
		
		log.debug(String.format(LogWrapper.TRACE_EXIT_LOG_TEMPLATE,
				CurrentThreadContext.getUniversalId(), classname, methodName,
				returnvalue.toString()));
	}

	/**
	 * This method logs debug message when method returns.
	 * 
	 * @param classname
	 *            The class name which needs to be logged.
	 * @param methodName
	 *            The method name which needs to be logged.
	 * @param exception
	 *            This is the exception that occurred.
	 * @author
	 */
	public void logTraceMethodException(String classname, String methodName,
			Exception exception) {
		
		log.error(String.format(LogWrapper.EXCEPTION_LOG_TEMPLATE,
				CurrentThreadContext.getUniversalId(), classname, methodName,
				this.convertStackTraceToString(exception)));

	}

	/**
	 * This method logs debug message when Controller is invoked.
	 * 
	 * @param uri
	 *            The corresponding URI.
	 * @param queryString
	 *            The Map Consisting the key value pair in the query string.
	 * @param header
	 *            The String array consisting the header information.
	 * @param cookie
	 *            The String array consisting the cookie information.
	 * @param body
	 *            The String consisting the body of the Rest call.
	 * @author
	 */
	public void logTraceControllerEntry(String uri,
			Map<String, Object> queryString, String[] header, String[] cookie,
			String body) {
		
		log.debug(String.format(LogWrapper.TRACE_CONTROLLER_ENTRY_LOG_TEMPLATE,
				CurrentThreadContext.getUniversalId(), uri,
				this.convertKeyValueMapToString(queryString),
				this.convertListToString(header),
				this.convertListToString(cookie), body));
	}

	/**
	 * This method logs an api
	 * 
	 * @param uri
	 *            This is the url
	 * @param queryString
	 *            This is the quesry String
	 * @param header
	 *            This is the header
	 * @param cookie
	 *            This is the cookie
	 * @param exception
	 *            This is the exception
	 */
	public void logAPI(String api, Exception exception) {
		
		log.info(String.format(LogWrapper.LOG_API_TEMPLATE,
				CurrentThreadContext.getUniversalId(), api,
				exception == null ? "Success" : "Fail",
				exception == null ? "" : exception.toString()));
	}

	/**
	 * This method logs debug message when Controller is exiting.
	 * 
	 * @param uri
	 *            The corresponding URI.
	 * @param queryString
	 *            The Map Consisting the key value pair in the query string.
	 * @param header
	 *            The String array consisting the header information.
	 * @param cookie
	 *            The String array consisting the cookie information.
	 * @param body
	 *            The String consisting the body of the Rest call.
	 * @param returnvalue
	 *            The Object which is returned by the controller.
	 * @author
	 */
	public void logTraceControllerExit(String uri,
			Map<String, Object> queryString, String[] header, String[] cookie,
			String body, Object returnvalue) {
		
		log.debug(String.format(LogWrapper.TRACE_CONTROLLER_EXIT_LOG_TEMPLATE,
				CurrentThreadContext.getUniversalId(), uri,
				this.convertKeyValueMapToString(queryString),
				this.convertListToString(header),
				this.convertListToString(cookie), body,
				returnvalue.toString()));
	}

	/**
	 * This method converts the Key value Map to the formatted string for
	 * logging.
	 * 
	 * @param map
	 *            This is the map being logged.
	 * @return A formatted key value String which is to be logged.
	 * @author
	 */
	public String convertKeyValueMapToString(Map<String, Object> map) {
		StringBuilder stringBuilder = new StringBuilder();

		
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			
			String key = entry.getKey();
			
			Object value = entry.getValue();

			
			stringBuilder.append(key + " : " + value.toString() + " --- ");
		}
		
		return stringBuilder.toString();
	}

	/**
	 * This method converts the list of string to the formatted string for
	 * logging.
	 * 
	 * @param stringList
	 *            The String array which needs to be formatted.
	 * @return A formatted string which is to be logged.
	 * @author
	 */
	public String convertListToString(String[] stringList) {
		StringBuilder stringBuilder = new StringBuilder();

		
		for (String item : stringList) {
		
			stringBuilder.append(item + " --- ");
		}
		
		return stringBuilder.toString();
	}

	/**
	 * This method converts the Stack trace to the formatted string for logging.
	 * 
	 * @param exception
	 *            This is the exception being logged.
	 * @return A string which is the formatted stack trace.
	 * @author
	 */
	public String convertStackTraceToString(Exception exception) {
		StringBuilder stringBuilder = new StringBuilder();
		
		stringBuilder.append(exception.toString() + "---");
		
		for (int i = 0; i < exception.getStackTrace().length; i++) {
			
			stringBuilder.append(exception.getStackTrace()[i] + "---");
		}
		
		return stringBuilder.toString();
	}

	/**
	 * This method logs a CQL
	 * 
	 * @param cql
	 *            the CQL Query
	 * @param startTime
	 *            Start Time
	 * @param endTime
	 *            End Time
	 */
	public void logCQL(String cql, Date startTime, Date endTime) {
		
		long timeTaken = endTime.getTime() - startTime.getTime();
		
		log.info(String.format(LogWrapper.LOG_CQL,
				CurrentThreadContext.getUniversalId(), cql, timeTaken));
	}

}