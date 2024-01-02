package com.pearson.ptb.framework;

import com.pearson.ptb.framework.exception.ConfigException;

public final class CacheConfigManager extends BaseConfigurationManager {

	/**
	 * This is the class name for instantiating.
	 */
	private static CacheConfigManager cacheConfigManager = null;

	/**
	 * This is the property file path and name
	 */
	private static final String PROPERTY_FILE_PATH = "/config.properties";

	protected CacheConfigManager() {
		// Calling parent class Constructor to load all properties from cache
		// properties file
		super();
	}

	/**
	 * <p>
	 * To get the cache property file path
	 * </p>
	 * 
	 * @return Returns file path of the cache property file
	 */
	protected String getConfigPath() {
		return PROPERTY_FILE_PATH;
	}

	public Boolean getIsCacheEnabled() {
		return Boolean.valueOf(((String) configurationProperties
				.get("cache.isCacheEnabled")).replaceAll(SPECIAL_CHARACTER_STRING,""));
	}
	
	public Object getCacheServer(){
		return configurationProperties.get("cache.cacheServer");
	}
	
	public Integer getCacheExpiryInSeconds(){
		return Integer.valueOf((String) configurationProperties.get("cache.cacheExpiryInSeconds"));
	}

	/**
	 * <p>
	 * Instantiating the singleton
	 * </p>
	 * 
	 * @return Cache configuration Manager returns the instance of
	 *         CacheConfigManager class.
	 * @throws ConfigException
	 */
	protected static CacheConfigManager getInstance() {
		// synchronized block for creating CacheConfigManager singleton
		if (cacheConfigManager == null) {
			// null check in synchronization for race condition
			synchronized (CacheConfigManager.mutex) {
				if (cacheConfigManager == null) {
					// create CacheConfigManager instance as singleton
					cacheConfigManager = new CacheConfigManager();
				}
			}
		}
		// return CacheConfigManager
		return cacheConfigManager;
	}

}
