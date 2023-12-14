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
		// add to the message the string representation of the error
		stringBuilder.append(cause.toString() + "---");
		// for each class in its part add the trace
		for (int i = 0; i < cause.getStackTrace().length; i++) {
			stringBuilder.append(cause.getStackTrace()[i] + "---");
		}
		// return message with exception details as a string
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

            // Loop through with all messages list to build the single message
            for (String msg : messages) {
                // Check for first message is adding to string builder
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
