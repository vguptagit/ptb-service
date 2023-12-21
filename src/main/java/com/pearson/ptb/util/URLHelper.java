package com.pearson.ptb.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import com.pearson.ptb.framework.exception.InternalException;

public class URLHelper {

	private URLHelper() {

	}

	/**
	 * @param query
	 *            The query string's
	 * @return The query string in HashMap type
	 * @throws UnsupportedEncodingException
	 */
	public static Map<String, String> getQueryMap(String query) {
		return getQueryMap(query, null);
	}

	/**
	 * @param query
	 *            : The query string's
	 * @param keyMap
	 *            : mapping for the key value
	 * @return The query string in HashMap type
	 * @throws UnsupportedEncodingException
	 */
	public static Map<String, String> getQueryMap(String query,
			Map<String, String> keyMap) {
		Map<String, String> map = new HashMap<String, String>();
		try {
			if (query != null) {
				String[] params = query.split("&");
				for (String param : params) {
					String[] keyValue = param.split("=");
					String name = keyValue[0];
					String value = URLDecoder.decode(keyValue[1],
							Common.UTF_8_CHAR_ENCODING);
					if (keyMap != null) {
						if (keyMap.containsKey(name)) {
							name = keyMap.get(name);
							map.put(name, value);
						}
					} else {

						map.put(name, value);
					}
				}
			}
		} catch (UnsupportedEncodingException e) {
			throw new InternalException("Not able to decode the querystring",
					e);
		} catch (Exception e) {
			throw new InternalException("Not able read the querystring", e);
		}
		return map;
	}
}
