package com.pearson.ptb.framework;
import java.util.UUID;

/**
 * Thread local wrapper to store values which would remain in thread context.
 * 
 * @author
 *
 */
public class CurrentThreadContext {

	/**
	 * The Thread local instance of UUID type to store the thread related
	 * variables.
	 * 
	 * @author
	 */
	private static ThreadLocal<UUID> threadLocal = new ThreadLocal<UUID>();

	private CurrentThreadContext() {
	}

	/**
	 * get method to fetch the universal request id for the current thread.
	 * 
	 * @author
	 * @return The universal request id for the thread.
	 */
	public static String getUniversalId() {

		
		if (threadLocal.get() == null) {

		
			threadLocal.set(UUID.randomUUID());

		}

		return threadLocal.get().toString();

	}

}
