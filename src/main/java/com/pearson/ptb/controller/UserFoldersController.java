package com.pearson.ptb.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.pearson.ptb.util.Swagger;
import com.pearson.ptb.bean.UserFolder;
import com.pearson.ptb.bean.UserQuestionsFolder;
import com.pearson.ptb.dtos.ApiResponseMessage;
import com.pearson.ptb.service.UserFolderService;
import com.pearson.ptb.util.UserHelper;

import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * To list all the user level Folders,Tests and Questions
 *
 */

@RestController
@Tag(name = "User folders", description = "User folders API")
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

	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Success") })
	@Operation(summary = Swagger.GET_ROOT_FOLDERS_VALUE, description = Swagger.GET_ROOT_FOLDERS_NOTE)

	@RequestMapping(value = "/my/testfolders", method = RequestMethod.GET)

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

	@Operation(summary = Swagger.GET_TEST_ROOT_VALUE, description = Swagger.GET_TEST_ROOT_NOTE)
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Success") })

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

	@Operation(summary = Swagger.GET_FOLDERS_VALUE, description = Swagger.GET_FOLDERS_NOTE)
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Success") })

	@RequestMapping(value = "/my/folders/{folderId}/folders", method = RequestMethod.GET)

	@ResponseBody
	public List<UserFolder> getFolders(@PathVariable String folderId, HttpServletRequest request) {

		return userFolderService.getFolders(UserHelper.getUserId(request), folderId);
	}

	/**
	 * To save instructor created custom folder
	 * 
	 * @param folder
	 * @param request
	 * @param response
	 * @return UserFolder
	 */

	@Operation(summary = Swagger.SAVE_FOLDERS_VALUE, description = Swagger.SAVE_FOLDERS_NOTE)
	@ApiResponses(value = { @ApiResponse(responseCode = "201", description = "Created") })

	@RequestMapping(value = "/my/testfolders", method = RequestMethod.POST)

	@ResponseBody
	public UserFolder saveFolders(@Valid @RequestBody UserFolder folder, HttpServletRequest request,
			HttpServletResponse response) {

		response.setStatus(HttpServletResponse.SC_CREATED);
		return userFolderService.saveFolder(folder, UserHelper.getUserId(request));

	}

	/**
	 * To update instructor folder
	 * 
	 * @param folder
	 * @param request
	 * @param response
	 * @return UserFolder
	 */

	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Success") })
	@Operation(summary = Swagger.UPDATE_FOLDERS_VALUE, description = Swagger.UPDATE_FOLDERS_NOTE)
	@RequestMapping(value = "/my/testfolders", method = RequestMethod.PUT)

	@ResponseBody
	public UserFolder updateFolder(@Valid @RequestBody UserFolder folder, HttpServletRequest request,
			HttpServletResponse response) {

		response.setStatus(HttpServletResponse.SC_CREATED);
		return userFolderService.updateFolder(folder, UserHelper.getUserId(request));

	}

	/**
	 * To save instructor created Question Folder
	 * 
	 * @param folder
	 * @param request
	 * @param response
	 * @return UserQuestionsFolder
	 */

	@ApiResponses(value = { @ApiResponse(responseCode = "201", description = "Created") })
	@Operation(summary = Swagger.SAVE_USERQUESTION_FOLDERS_VALUE, description = Swagger.SAVE_USERQUESTION_FOLDERS_NOTE)
	@RequestMapping(value = "/my/questionfolders", method = RequestMethod.POST)

	@ResponseBody
	public UserQuestionsFolder saveUserQuestionFolder(@Valid @RequestBody UserQuestionsFolder folder,
			HttpServletRequest request, HttpServletResponse response) {

		response.setStatus(HttpServletResponse.SC_CREATED);
		return userFolderService.saveUserQuestionFolder(folder, UserHelper.getUserId(request));

	}

	/**
	 * To update instructor created Question Folder
	 * 
	 * @param folder
	 * @param request
	 * @param response
	 * @return UserQuestionsFolder
	 */

	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Success") })
	@Operation(summary = Swagger.UPDATE_USERQUESTION_FOLDERS_VALUE, description = Swagger.UPDATE_USERQUESTION_FOLDERS_NOTE)
	@RequestMapping(value = "/my/questionfolders", method = RequestMethod.PUT)

	@ResponseBody
	public UserQuestionsFolder updateUserQuestionFolder(@Valid @RequestBody UserQuestionsFolder folder,
			HttpServletRequest request, HttpServletResponse response) {

		response.setStatus(HttpServletResponse.SC_CREATED);
		return userFolderService.updateUserQuestionFolder(folder, UserHelper.getUserId(request));

	}

	/**
	 * To get a list of all root level question folders
	 * 
	 * @param request
	 * @return List of UserQuestionsFolder
	 */

	@Operation(summary = Swagger.GET_USERQUESTION_FOLDERS_VALUE, description = Swagger.GET_USERQUESTION_FOLDERS_NOTE)
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Success") })

	@RequestMapping(value = "/my/questionfolders", method = RequestMethod.GET)

	@ResponseBody
	public List<UserQuestionsFolder> getUserQuestionFolders(HttpServletRequest request) {

		String userId = UserHelper.getUserId(request);
		return userFolderService.getMyQuestionsFolders(userId);
	}

	/**
	 * To get a list of child level question folders for a particular parent folder
	 * 
	 * @param parent folder id
	 * @return List of UserQuestionsFolder
	 */

	@Operation(summary = Swagger.GET_CHILDQUESTION_FOLDERS_VALUE, description = Swagger.GET_CHILDQUESTION_FOLDERS_NOTE)

	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Success") })

	@RequestMapping(value = "/my/questionfolders/{folderId}/folders", method = RequestMethod.GET)

	@ResponseBody
	public List<UserQuestionsFolder> getChildQuestionFolders(@PathVariable String folderId) {

		return userFolderService.getChildQuestionFolders(folderId);
	}

	/**
	 * To get root level question folder
	 * 
	 * @param request
	 * @return UserFolder
	 */
	

	@RequestMapping(value = "/my/questionfoldersroot", method = RequestMethod.GET)

	@ResponseBody
	public UserFolder getQuestionFoldersRoot(HttpServletRequest request) {

		String userId = UserHelper.getUserId(request);
		return userFolderService.getQuestionFoldersRoot(userId);
	}
	

	@Operation(summary = "To swap the test between the question folders")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Question swapped successfully"),
			@ApiResponse(responseCode = "400", description = "Question swap failed") })
	@PutMapping("my/questions/{sFolderId}/folders/{dFolderId}/{questionID}")
	public ResponseEntity<ApiResponseMessage> updateQuestionBindings(@PathVariable String sFolderId, @PathVariable String dFolderId, @PathVariable String questionID , HttpServletRequest request) {
		String userId = UserHelper.getUserId(request);
		userFolderService.updateQuestionBindings(userId , sFolderId , dFolderId ,questionID);
		ApiResponseMessage apiResponseMessage = ApiResponseMessage.builder()
				.status(HttpStatus.OK)
				.message("question swapped successfully..")
				.success(true)
				.build();
		return new ResponseEntity<>(apiResponseMessage , HttpStatus.OK);
	}
	
	

	
	
	
	
	
	
}
