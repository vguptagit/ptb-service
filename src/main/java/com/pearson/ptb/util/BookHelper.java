package com.pearson.ptb.util;

import java.util.HashMap;
import java.util.Map;

public class BookHelper {

	private static Map<String, String> filtercriteriaMap = null;
	
	
	private BookHelper() {

	}

	/**
	 * 
	 * @return
	 */
	public static synchronized Map<String, String> getFilterCriteriaMap() {
		if (filtercriteriaMap == null || filtercriteriaMap.isEmpty()) {
			filtercriteriaMap = new HashMap<String, String>();
			filtercriteriaMap.put("guid", "guid");
			filtercriteriaMap.put("title", "title");
			filtercriteriaMap.put("author", "authors");
			filtercriteriaMap.put("isbn", "isbn");
			filtercriteriaMap.put("publisher", "publisher");
			filtercriteriaMap.put("discipline", "discipline");
		}

		return filtercriteriaMap;
	}

}
