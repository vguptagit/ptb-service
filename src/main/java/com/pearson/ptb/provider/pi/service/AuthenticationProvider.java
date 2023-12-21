/**
 * 
 */
package com.pearson.ptb.provider.pi.service;

import java.io.IOException;

import org.apache.http.impl.conn.BasicClientConnectionManager;

import com.pearson.ed.pi.authentication.authenticator.TokenAuthenticator;
import com.pearson.ed.pi.encryption.encryptor.impl.DataSecureEncryptor;
import com.pearson.ed.pi.encryption.exception.RemoteCallException;
import com.pearson.ed.pi.token.JWKSSource;
import com.pearson.ed.pi.token.JWKSStoreProxy;
import com.pearson.ptb.framework.ConfigurationManager;
import com.pearson.ptb.framework.exception.AuthTokenException;
import com.pearson.ptb.framework.exception.ConfigException;

/**
 * authentication provider for the PI users.
 */
public class AuthenticationProvider {

	/**
	 * This is a lock object.
	 */
	private static Object mutex = new Object();

	private static TokenAuthenticator tokenAuthenticator = null;

	/**
	 * @return The token authenticator object.
	 * @throws AuthTokenException
	 * @throws IOException
	 *             In case unable to connect to the database.
	 * @throws ConfigException
	 */
	private void setTokenAuthenticator() {

		try {
			ConfigurationManager config = ConfigurationManager.getInstance();

			if (tokenAuthenticator == null) {
				synchronized (AuthenticationProvider.mutex) {
					if (tokenAuthenticator == null) {

						String host = config.getDataSecureRestUrl();
						int socketTimeout = config.getSocketTimeout();
						int connectionTimeout = config.getConnectionTimeout();

						BasicClientConnectionManager connectionManager = new BasicClientConnectionManager();

						DataSecureEncryptor decrptor = new DataSecureEncryptor(
								host, connectionManager, socketTimeout,
								connectionTimeout);

						String keyMoniker = config.getKeyMoniker();
						String keyMap = config.getKeyMap();

						com.pearson.ed.pi.encryption.Keymap keymap = new com.pearson.ed.pi.encryption.Keymap(
								decrptor, keyMoniker, keyMap);

						JWKSSource jwksSource = new JWKSStoreProxy(
								config.getTokenUrl(), connectionTimeout,
								socketTimeout);
						tokenAuthenticator = new TokenAuthenticator(keymap,
								jwksSource);

					}
				}
			}
		} catch (ConfigException ex) {
			throw new AuthTokenException(
					"Exception while reading config in method setTokenAutheticator",
					ex);
		} catch (RemoteCallException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new AuthTokenException(
					"Exception in method setTokenAutheticator", ex);
		}
	}
	/**
	 * this method authenticates the pi auth token with the existing user id
	 * 
	 * @return existing user as a string
	 */
	public String authenticate(String piAuthtoken) {

		String extUserId = null;

		setTokenAuthenticator();
		extUserId = tokenAuthenticator
				.validateTokenAndDetermineIdentityForSystem(piAuthtoken,
						"piid");

		return extUserId;

	}

}