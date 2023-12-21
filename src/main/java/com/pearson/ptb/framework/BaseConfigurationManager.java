package com.pearson.ptb.framework;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.pearson.ptb.framework.exception.ConfigException;

public abstract class BaseConfigurationManager {

	/**
	 * This is the map containing all configuration property key value pair
	 * 
	 * @author
	 */
	protected static Map<String, String> configurationProperties = new HashMap<String, String>();

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
		// load all properties from properties file
		loadPropertyFile();
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
	private void loadPropertyFile() {

		// create Properties object
		Properties properties = new Properties();
		String key = null;
		String value = null;
		@SuppressWarnings("rawtypes")
		Enumeration enumeration = null;

		try {

			// loads configuration property file
			properties
					.load(this.getClass().getResourceAsStream(getConfigPath()));
		} catch (IOException ex) {

			throw new ConfigException("Unable to read config file", ex);
		}
		// Store all the keys of property file in Enumeration
		enumeration = properties.keys();

		// iterate over all the elements in Enumeration
		while (enumeration.hasMoreElements()) {

			// fetch key from Enumeration
			key = (String) enumeration.nextElement();
			// fetch value from Enumeration
			value = ((String) properties.get(key))
					.replace(SPECIAL_CHARACTER_STRING, "");
			// put the key value pair in configurationProperties map
			configurationProperties.put(key, value);
		}
	}

}