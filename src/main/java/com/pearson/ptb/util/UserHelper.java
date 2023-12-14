package com.pearson.ptb.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pearson.ptb.bean.AnswerAreas;
import com.pearson.ptb.bean.AnswerKeys;
import com.pearson.ptb.bean.DownloadFormat;
import com.pearson.ptb.bean.PrintSettings;
import com.pearson.ptb.bean.UserSettings;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Utility for my test
 * 
 * @author nithinjain
 */
public class UserHelper {

	/**
	 * Making the Constructor private.
	 */
	private UserHelper() {

	}

	/**
	 * Add the new key value to HashMap
	 * 
	 * @param criteria
	 *            To add the new values
	 * @param key
	 *            The criteria key
	 * @param vaule
	 *            The criteria value
	 * @return criteria with new values
	 */
	public static Map<String, String> addCriteria(Map<String, String> criteria,
			String key, String vaule) {
		Map<String, String> mapCriteria=criteria;
		if (vaule != null) {		
			if (mapCriteria == null) {
				mapCriteria = new HashMap<String, String>();
			}

			mapCriteria.put(key, vaule);
		}

		return mapCriteria;

	}

	/**
	 * This will give the user id from request object
	 * 
	 * @param request
	 *            The request object
	 * @return user id
	 */
	public static String getUserId(HttpServletRequest request) {
		String userId = null;

		if (request != null && request.getAttribute(Common.EXT_USER_ID) != null) {
			userId = request.getAttribute(Common.EXT_USER_ID).toString();
		} else {
			userId = "ffffffff54b3faa6e4b0f10ebd0747ce";
		}
		return userId;
	}
	
	/**
	 * This will give the default settings for a new user
	 * 
	 * @param request
	 *            The userid generated from request object
	 * @return UserSettings
	 */	
	public static UserSettings getDefaultSettings(String userid) {

		UserSettings userSettings = new UserSettings();
		
		userSettings.setUserid(userid);
		
		userSettings.setDisciplines(new ArrayList<String>());
		userSettings.setBooks(new ArrayList<String>());
		
		List<String> questionMetadata = new ArrayList<String>();
		questionMetadata.add("Difficulty");
		questionMetadata.add("Topic");
		questionMetadata.add("Objective");
		questionMetadata.add("PageReference");
		questionMetadata.add("Skill");
		questionMetadata.add("QuestionId");
		
		userSettings.setQuestionMetadata(questionMetadata);
		
		PrintSettings printSettings = new PrintSettings();
		printSettings.setLeftMargin("1.9");
		printSettings.setRightMargin("1.9");
		printSettings.setTopMargin("1.9");
		printSettings.setBottomMargin("1.9");		
		
		printSettings.setFont("Helvetica, Arial");
		printSettings.setFontSize("12");
		printSettings.setFooterSpace("1.2");
		printSettings.setHeaderSpace("1.2");

		printSettings.setMultipleVersions(true);		
		printSettings.setNumberOfVersions(1);
		printSettings.setScrambleOrder("Scramble question order");

		printSettings.setIncludeAreaForStudentResponse(AnswerAreas.NONE);
		
		printSettings.setIncludeAnswerKeyIn(AnswerKeys.NONE);				
		
		printSettings.setIncludeAnwserFeedback(true);
		printSettings.setIncludeQuestionHints(true);
		
		printSettings.setExportFileFormat(DownloadFormat.doc);
		userSettings.setPrintSettings(printSettings);
		
		return userSettings;
	}
	
}
