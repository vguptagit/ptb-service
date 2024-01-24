package com.pearson.ptb.framework;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.pearson.ptb.framework.exception.ConfigException;

public abstract class BaseConfigurationManager {

	
	
	protected static Properties configurationProperties;
	/**
	 * This is the map containing all configuration property key value pair
	 * 
	 * @author
	 */
	

	/**
	 * This is the special character regular expression
	 * 
	 * @author
	 */
	protected static String SPECIAL_CHARACTER_STRING = "\\s+";

	protected static String HEALTH_SPECIAL_CHARACTER = "_";

	/**
	 * This is a lock object.
	 * 
	 * @author
	 */
	protected static Object mutex = new Object();

	/**
	 * <p>
	 * This is the private constructor of the class. It is used to make
	 * ConfigurationManager as a singleton
	 * </p>
	 * 
	 * @throws ConfigException
	 */
	protected BaseConfigurationManager() {
		

		configurationProperties = new Properties();
		try (InputStream inStream = getClass()
				.getResourceAsStream("/application.properties")) {
			if (inStream != null) {
				configurationProperties.load(inStream);
				String property = configurationProperties
						.getProperty("cache.cacheExpiryInSeconds");
				System.out.println(property + "heloooooooooooooooooooooooo");
			} else {
				throw new IOException(
						"Failed to load properties file. InputStream is null.");
			}
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}

	
	
	protected abstract String getConfigPath();

	/**
	 * <p>
	 * This method loads the property file from resource directory and
	 * initialize configurationProperties map
	 * </p>
	 * 
	 * @throws ConfigException
	 */

}