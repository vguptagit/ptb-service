package com.pearson.ptb.util;

import java.util.List;

public class ExceptionHelper {

	/**
	 * This method converts the stack trace to string.
	 * 
	 * @param cause
	 *            throwable object.
	 * @return stack trace message as string.
	 */
	public String convertStackTraceToString(Throwable cause) {
		StringBuilder stringBuilder = new StringBuilder();
		
		stringBuilder.append(cause.toString() + "---");
		
		for (int i = 0; i < cause.getStackTrace().length; i++) {
			stringBuilder.append(cause.getStackTrace()[i] + "---");
		}
		
		return stringBuilder.toString();
	}

	/**
	 * This method converts the list of messages to single message
	 * 
	 * @param messages
	 *            The list of messages
	 * @return concatenated message string
	 */
	public static String getMessage(List<String> messages) {
		StringBuilder builder = new StringBuilder();
		if (!messages.isEmpty()) {

			
			for (String msg : messages) {
				
				if (builder.length() == 0) {
					builder.append("The following input fields are invalid! ")
							.append(msg);
				} else {
					builder.append(", ").append(msg);
				}

			}
		}
		return builder.toString();
	}

}
