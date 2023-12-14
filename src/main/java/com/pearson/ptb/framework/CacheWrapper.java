package com.pearson.ptb.framework;

import java.io.IOException;
import java.io.Serializable;

import org.apache.commons.lang.SerializationUtils;

import com.pearson.ptb.framework.exception.ConfigException;
import com.pearson.ptb.framework.exception.ServiceUnavailableException;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public final class CacheWrapper {

	/**
	 * A private instance of cache wrapper which manages the pool
	 */
	private static CacheWrapper cacheWrapper = null;

	/**
	 * This is the instance of the pool
	 */
	private JedisPool jedisPool = null;

	/**
	 * This is the instance of the object
	 */
	private static Object mutex = new Object();

	/**
	 * This is the instance of the log wrapper.
	 */
	private static final LogWrapper LOG = LogWrapper
			.getInstance(CacheWrapper.class);

	/**
	 * This is the instance of the configuration manager.
	 */
	private static ConfigurationManager CONFIG;

	/**
	 * This is the instance of the configuration manager.
	 */
	private static CacheConfigManager CACHECONFIG;

	/**
	 * This is template message for CacheWrapper.
	 */
	private static final String CLASSNAME = "CacheWrapper";

	/**
	 * This is the private constructor
	 * 
	 * @throws ConfigException
	 */
	private CacheWrapper(){

		CONFIG = ConfigurationManager.getInstance();
		CACHECONFIG = CacheConfigManager.getInstance();
		// create an instance of pool
		jedisPool = new JedisPool(new JedisPoolConfig(),
				CACHECONFIG.getCacheServer());
	}

	/**
	 * This method gets an instance of the cache wrapper and initializes the
	 * pool
	 * 
	 * @return The instance of cache wrapper
	 * @throws IOException
	 *             If there is an error in accessing the cache.
	 * @throws ServiceUnavailableException
	 */
	public static CacheWrapper getInstance(){
		try {
			if (CacheWrapper.cacheWrapper == null) {
				// null check in synchronization for race condition
				synchronized (CacheWrapper.mutex) {
					if (CacheWrapper.cacheWrapper == null) {
						// create cache wrapper instance as singleton
						CacheWrapper.cacheWrapper = new CacheWrapper();
					}
				}
			}
		} catch (ConfigException ex) {
			throw new ServiceUnavailableException(
					"Unable to connect to cache server", ex);
		}

		return CacheWrapper.cacheWrapper;
	}

	/**
	 * This method sets an item on cache
	 * 
	 * @param key
	 *            The key
	 * @param value
	 *            The value
	 * @throws IOException
	 *             If there is an error in accessing the cache
	 */
	public void set(String key, Serializable value) {
		this.set(key, value, CACHECONFIG.getCacheExpiryInSeconds());
	}

	/**
	 * This method sets an item on cache
	 * 
	 * @param key
	 *            The key
	 * @param value
	 *            The value
	 * @param timeOut
	 *            The time out in seconds for this entity
	 * @throws IOException
	 *             If there is an access error
	 */
	public void set(String key, Serializable value, int timeOut) {
		Jedis jedis = null;
		if (CACHECONFIG.getIsCacheEnabled()) {
			try {

				jedis = jedisPool.getResource();

				// create the key as app name + string so that we may use the
				// same
				// server for multiple apps.
				String cacheKey = CONFIG.environmentName() + ":"
						+ CONFIG.applicationName() + ":" + key;
				jedis.setex(SerializationUtils.serialize(cacheKey), timeOut,
						SerializationUtils.serialize(value));

				// return the client to pool
				if (jedis != null) {
					jedisPool.returnResource(jedis);
				}
			} catch (Exception ex) {
				// on error return broken client
				if (jedis != null) {
					jedisPool.returnBrokenResource(jedis);
				}

				LOG.logTraceMethodException(CLASSNAME, "set", ex);
			}
		}

	}

	/**
	 * This method gets an object for the key
	 * 
	 * @param key
	 *            The key
	 * @return The object
	 * @throws IOException
	 *             If there is an access issue
	 */
	public <T> T get(String key) {
		return this.get(key, CACHECONFIG.getCacheExpiryInSeconds());
	}

	/**
	 * This method gets an object for the key
	 * 
	 * @param key
	 *            The key
	 * @return The object
	 * @param timeOut
	 *            The time out in seconds for this entity
	 * @throws IOException
	 *             If there is an access issue
	 */
	@SuppressWarnings("unchecked")
	public <T> T get(String key, int timeout) {
		Jedis jedis = null;
		Object object = null;
		if (CACHECONFIG.getIsCacheEnabled()) {
			try {
				jedis = jedisPool.getResource();

				String cacheKey = CONFIG.environmentName() + ":"
						+ CONFIG.applicationName() + ":" + key;

				byte[] byteArray = jedis.get(SerializationUtils
						.serialize(cacheKey));

				if (byteArray != null) {
					// if not null then generate the original Object
					object = SerializationUtils.deserialize(byteArray);
				}

				// slide expiration time on every get
				jedis.expire(SerializationUtils.serialize(cacheKey), timeout);

				// return the client to pool
				if (jedis != null) {
					jedisPool.returnResource(jedis);
				}
			} catch (Exception ex) {
				// on error return broken client
				if (jedis != null) {
					jedisPool.returnBrokenResource(jedis);
				}

				LOG.logTraceMethodException(CLASSNAME, "get", ex);
			}
		}
		if (object == null) {
			return null;
		} else {
			return (T) object;
		}

	}

	/**
	 * This method flushes the cache
	 */
	public void flush() {
		Jedis jedis = null;
		if (CACHECONFIG.getIsCacheEnabled()) {
			try {
				// get an instance of resource
				jedis = this.jedisPool.getResource();

				jedis.flushAll();
				if (jedis != null) {
					this.jedisPool.returnResource(jedis);
				}
			} catch (Exception ex) {
				// on error return broken client
				if (jedis != null) {
					jedisPool.returnBrokenResource(jedis);
				}
				LOG.logTraceMethodException(CLASSNAME, "flush", ex);
			}
		}
	}

	/**
	 * This method deletes the key from cache
	 * 
	 * @param key
	 *            This is the key
	 * @throws IOException
	 */
	public void delete(String key) {

		// get an instance of resource
		Jedis jedis = null;
		if (CACHECONFIG.getIsCacheEnabled()) {
			try {
				jedis = this.jedisPool.getResource();
				String cacheKey = CONFIG.environmentName() + ":"
						+ CONFIG.applicationName() + ":" + key;
				jedis.del(SerializationUtils.serialize(cacheKey));
				if (jedis != null) {
					this.jedisPool.returnResource(jedis);
				}
			} catch (Exception ex) {
				// on error return broken client
				if (jedis != null) {
					jedisPool.returnBrokenResource(jedis);
				}
				LOG.logTraceMethodException(CLASSNAME, "delete", ex);
			}
		}
	}
}
