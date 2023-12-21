package com.pearson.ptb.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pearson.ptb.bean.UserFolder;
import com.pearson.ptb.bean.UserQuestionsFolder;
import com.pearson.ptb.service.UserFolderService;
import com.pearson.ptb.util.UserHelper;
import com.wordnik.swagger.annotations.Api;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * To list all the user level Folders,Tests and Questions
 *
 */

@Controller

@Api(value = "User folders", description = "User folders API")
public class UserFoldersController extends BaseController {

	@Autowired

	@Qualifier("userFolderService")
	private UserFolderService userFolderService;

	/**
	 * To get a list of all root folders
	 * 
	 * @param request
	 * @return List of UserFolder
	 */

	// @ApiOperation(value = Swagger.GET_ROOT_FOLDERS_VALUE, notes =
	// Swagger.GET_ROOT_FOLDERS_NOTE)

	@RequestMapping(value = "/my/folders", method = RequestMethod.GET)

	@ResponseBody
	public List<UserFolder> getRootFolders(HttpServletRequest request) {

		String userId = UserHelper.getUserId(request);
		return userFolderService.getFolders(userId, null);
	}

	/**
	 * To get folder used for storing root level tests
	 * 
	 * @param request
	 * @return UserFolder
	 */

	// @ApiOperation(value = Swagger.GET_TEST_ROOT_VALUE, notes =
	// Swagger.GET_TEST_ROOT_NOTE)

	@RequestMapping(value = "/my/testroot", method = RequestMethod.GET)

	@ResponseBody
	public UserFolder getMyTestRoot(HttpServletRequest request) {

		String userId = UserHelper.getUserId(request);
		return userFolderService.getMyTestRoot(userId);
	}

	/**
	 * To get a list of children folders of a given folder
	 * 
	 * @param folderId
	 * @param request
	 * @return List of UserFolder
	 */

	// @ApiOperation(value = Swagger.GET_FOLDERS_VALUE, notes =
	// Swagger.GET_FOLDERS_NOTE)

	@RequestMapping(value = "/my/folders/{folderId}/folders", method = RequestMethod.GET)

	@ResponseBody
	public List<UserFolder> getFolders(@PathVariable String folderId,
			HttpServletRequest request) {

		return userFolderService.getFolders(UserHelper.getUserId(request),
				folderId);
	}

	/**
	 * To save instructor created custom folder
	 * 
	 * @param folder
	 * @param request
	 * @param response
	 * @return UserFolder
	 */

	// @ApiOperation(value = Swagger.SAVE_FOLDERS_VALUE, notes =
	// Swagger.SAVE_FOLDERS_NOTE)

	@RequestMapping(value = "/my/folders", method = RequestMethod.POST)

	@ResponseBody
	public UserFolder saveFolders(@Valid @RequestBody UserFolder folder,
			HttpServletRequest request, HttpServletResponse response) {

		response.setStatus(HttpServletResponse.SC_CREATED);
		return userFolderService.saveFolder(folder,
				UserHelper.getUserId(request));

	}

	/**
	 * To update instructor folder
	 * 
	 * @param folder
	 * @param request
	 * @param response
	 * @return UserFolder
	 */

	// @ApiOperation(value = Swagger.UPDATE_FOLDERS_VALUE, notes =
	// Swagger.UPDATE_FOLDERS_NOTE)

	@RequestMapping(value = "/my/folders", method = RequestMethod.PUT)

	@ResponseBody
	public UserFolder updateFolder(@Valid @RequestBody UserFolder folder,
			HttpServletRequest request, HttpServletResponse response) {

		response.setStatus(HttpServletResponse.SC_CREATED);
		return userFolderService.updateFolder(folder,
				UserHelper.getUserId(request));

	}

	/**
	 * To save instructor created Question Folder
	 * 
	 * @param folder
	 * @param request
	 * @param response
	 * @return UserQuestionsFolder
	 */

	// @ApiOperation(value = Swagger.SAVE_USERQUESTION_FOLDERS_VALUE, notes =
	// Swagger.SAVE_USERQUESTION_FOLDERS_NOTE)

	@RequestMapping(value = "/my/questionfolders", method = RequestMethod.POST)

	@ResponseBody
	public UserQuestionsFolder saveUserQuestionFolder(
			@Valid @RequestBody UserQuestionsFolder folder,
			HttpServletRequest request, HttpServletResponse response) {

		response.setStatus(HttpServletResponse.SC_CREATED);
		return userFolderService.saveUserQuestionFolder(folder,
				UserHelper.getUserId(request));

	}

	/**
	 * To update instructor created Question Folder
	 * 
	 * @param folder
	 * @param request
	 * @param response
	 * @return UserQuestionsFolder
	 */

	// @ApiOperation(value = Swagger.UPDATE_USERQUESTION_FOLDERS_VALUE, notes =
	// Swagger.UPDATE_USERQUESTION_FOLDERS_NOTE)

	@RequestMapping(value = "/my/questionfolders", method = RequestMethod.PUT)

	@ResponseBody
	public UserQuestionsFolder updateUserQuestionFolder(
			@Valid @RequestBody UserQuestionsFolder folder,
			HttpServletRequest request, HttpServletResponse response) {

		response.setStatus(HttpServletResponse.SC_CREATED);
		return userFolderService.updateUserQuestionFolder(folder,
				UserHelper.getUserId(request));

	}

	/**
	 * To get a list of all root level question folders
	 * 
	 * @param request
	 * @return List of UserQuestionsFolder
	 */

	// @ApiOperation(value = Swagger.GET_USERQUESTION_FOLDERS_VALUE, notes =
	// Swagger.GET_USERQUESTION_FOLDERS_NOTE)

	@RequestMapping(value = "/my/questionfolders", method = RequestMethod.GET)

	@ResponseBody
	public List<UserQuestionsFolder> getUserQuestionFolders(
			HttpServletRequest request) {

		String userId = UserHelper.getUserId(request);
		return userFolderService.getMyQuestionsFolders(userId);
	}

	/**
	 * To get a list of child level question folders for a particular parent
	 * folder
	 * 
	 * @param parent
	 *            folder id
	 * @return List of UserQuestionsFolder
	 */

	// @ApiOperation(value = Swagger.GET_CHILDQUESTION_FOLDERS_VALUE, notes =
	// Swagger.GET_CHILDQUESTION_FOLDERS_NOTE)

	@RequestMapping(value = "/my/questionfolders/{folderId}/folders", method = RequestMethod.GET)

	@ResponseBody
	public List<UserQuestionsFolder> getChildQuestionFolders(
			@PathVariable String folderId) {

		return userFolderService.getChildQuestionFolders(folderId);
	}

	/**
	 * To get root level question folder
	 * 
	 * @param request
	 * @return UserFolder
	 */
	// @ApiOperation(value = Swagger.GET_QUESTIONFOLDERS_ROOT_VALUE, notes =
	// Swagger.GET_QUESTIONFOLDERS_ROOT_NOTE)

	@RequestMapping(value = "/my/questionfoldersroot", method = RequestMethod.GET)

	@ResponseBody
	public UserFolder getQuestionFoldersRoot(HttpServletRequest request) {

		String userId = UserHelper.getUserId(request);
		return userFolderService.getQuestionFoldersRoot(userId);
	}
}
