package com.pearson.ptb.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pearson.ptb.bean.PrintSettings;
import com.pearson.ptb.bean.UserSettings;
import com.pearson.ptb.service.UserSettingsService;
import com.pearson.ptb.util.UserHelper;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * User Preference APIs
 *
 */
@Controller
@Api(value = "User Test Preferences", description = "User Preference APIs")
public class UserSettingsController extends BaseController {

	/**
	 * @Qualifier annotation searched for the value books in
	 *            appServlet-servlet.xml file created instance
	 */
	@Autowired
	@Qualifier("userSettingService")
	private UserSettingsService userSettingService;

	/**
	 * To get the User level preference settings
	 * 
	 * @param request
	 * @return UserSettings
	 */
	@ApiOperation(value = "Returns User level preference settings")
	@RequestMapping(value = "/settings", method = RequestMethod.GET)
	@ResponseBody
	public UserSettings getUserSettings(HttpServletRequest request) {

		String userid = UserHelper.getUserId(request);

		return userSettingService.getUserSettings(userid);

	}

	/**
	 * To save the User preference settings
	 * 
	 * @param request
	 * @param response
	 * @param userSettings
	 */
	@RequestMapping(value = "/settings", method = RequestMethod.POST)
	public void saveUserSettings(HttpServletRequest request,
			HttpServletResponse response,
			@Valid @RequestBody UserSettings userSettings) {

		String userid = UserHelper.getUserId(request);

		userSettings.setUserid(userid);
		userSettingService.saveUserSettings(userSettings);

		response.setStatus(HttpServletResponse.SC_CREATED);
	}

	/**
	 * To get the User preference disciplines
	 * 
	 * @param request
	 * @return List of disciplines
	 */
	@RequestMapping(value = "/settings/disciplines", method = RequestMethod.GET)
	@ResponseBody
	public List<String> getUserDisciplines(HttpServletRequest request) {

		String userid = UserHelper.getUserId(request);

		return userSettingService.getUserDisciplines(userid);
	}

	/**
	 * To save the User preference disciplines
	 * 
	 * @param request
	 * @param response
	 * @param disciplines
	 */
	@RequestMapping(value = "/settings/disciplines", method = RequestMethod.POST)
	@ResponseBody
	public void saveUserDisciplines(HttpServletRequest request,
			HttpServletResponse response,
			@RequestBody List<String> disciplines) {

		String userid = UserHelper.getUserId(request);

		userSettingService.saveUserDisciplines(userid, disciplines);

		response.setStatus(HttpServletResponse.SC_CREATED);
	}

	/**
	 * To get the User preference Books
	 * 
	 * @param request
	 * @return list of books
	 */
	@RequestMapping(value = "/settings/books", method = RequestMethod.GET)
	@ResponseBody
	public List<String> getUserBooks(HttpServletRequest request) {

		String userid = UserHelper.getUserId(request);

		List<String> books = null;
		books = userSettingService.getUserBooks(userid);

		return books;
	}

	/**
	 * To save the User preference books
	 * 
	 * @param request
	 * @param response
	 * @param books
	 */
	@RequestMapping(value = "/settings/books", method = RequestMethod.POST)
	@ResponseBody
	public void saveUserBooks(HttpServletRequest request,
			HttpServletResponse response, @RequestBody List<String> books) {

		String userid = UserHelper.getUserId(request);

		userSettingService.saveUserBooks(userid, books);

		response.setStatus(HttpServletResponse.SC_CREATED);
	}

	/**
	 * To get the User preference Question Metadata
	 * 
	 * @param request
	 * @return List of Question Metadata
	 */
	@RequestMapping(value = "/settings/questionmetadata", method = RequestMethod.GET)
	@ResponseBody
	public List<String> getQuestionMetadata(HttpServletRequest request) {

		String userid = UserHelper.getUserId(request);

		List<String> questionMetadata = null;
		questionMetadata = userSettingService.getQuestionMetadata(userid);

		return questionMetadata;
	}

	/**
	 * To save the User preference books
	 * 
	 * @param request
	 * @param response
	 * @param questionMetadata
	 */
	@RequestMapping(value = "/settings/questionmetadata", method = RequestMethod.POST)
	@ResponseBody
	public void saveQuestionMetadata(HttpServletRequest request,
			HttpServletResponse response,
			@RequestBody List<String> questionMetadata) {

		String userid = UserHelper.getUserId(request);

		userSettingService.saveQuestionMetadata(userid, questionMetadata);

		response.setStatus(HttpServletResponse.SC_CREATED);
	}

	/**
	 * To get the User preference Print Settings
	 * 
	 * @param request
	 * @return PrintSettings
	 */
	@RequestMapping(value = "/settings/printsettings", method = RequestMethod.GET)
	@ResponseBody
	public PrintSettings getPrintSettings(HttpServletRequest request) {

		String userid = UserHelper.getUserId(request);

		PrintSettings printSettings = null;
		printSettings = userSettingService.getPrintSettings(userid);

		return printSettings;
	}

	/**
	 * To save the User preference Print Settings
	 * 
	 * @param request
	 * @param response
	 * @param printSettings
	 */
	@RequestMapping(value = "/settings/printsettings", method = RequestMethod.POST)
	@ResponseBody
	public void savePrintsettings(HttpServletRequest request,
			HttpServletResponse response,
			@RequestBody PrintSettings printSettings) {

		String userid = UserHelper.getUserId(request);

		userSettingService.savePrintSettings(userid, printSettings);

		response.setStatus(HttpServletResponse.SC_CREATED);
	}
}
