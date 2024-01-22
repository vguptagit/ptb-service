package com.pearson.ptb.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.pearson.ptb.util.Swagger;
import com.pearson.ptb.bean.ArchiveItem;
import com.pearson.ptb.bean.ArchivedFolder;
import com.pearson.ptb.bean.TestMetadata;
import com.pearson.ptb.bean.UserFolder;
import com.pearson.ptb.service.ArchiveService;
import com.pearson.ptb.util.UserHelper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Provides the archive, restore and delete functionality for test and folder
 *
 */
@Tag(name = "Archive", description = "Archive restore APIs")
@RestController
@ApiResponse(description = "Archive restore APIs")
public class ArchiveController extends BaseController {

	@Autowired
	@Qualifier("archiveService")
	private ArchiveService archiveService;

	/**
	 * @param archiveItem
	 *            item to be archived
	 * @return <code>ArchivedFolder</code>
	 */

	@Operation(summary = Swagger.ARCHIVEFOLDER_VALUE, description = 
	 Swagger.ARCHIVEFOLDER_NOTE)
	@RequestMapping(value = "/my/archive/folders", method = RequestMethod.POST)
	@ResponseBody
	// done
	public ArchivedFolder archiveFolder(@RequestBody ArchiveItem archiveItem) {
		return archiveService.archiveFolder(archiveItem.getId());
	}

	/**
	 * To restore the archived folder
	 * 
	 * @param archiveItem
	 * @return UserFolder
	 */
	 @Operation(summary = Swagger.RESTOREFOLDER_VALUE, description =
	 Swagger.RESTOREFOLDER_NOTE)

	@RequestMapping(value = "/my/restore/folders", method = RequestMethod.POST)
	@ResponseBody
	public UserFolder restoreFolder(@RequestBody ArchiveItem archiveItem) {
		return archiveService.restoreFolder(archiveItem.getId());
	}

	/**
	 * To delete folder
	 * 
	 * @param folderId
	 *            as String
	 */
	 @Operation(summary = Swagger.DELETEFOLDER_VALUE, description = 
	 Swagger.DELETEFOLDER_NOTE)

	@RequestMapping(value = "/my/delete/folders/{folderId}", method = RequestMethod.DELETE)

	@ResponseBody
	public void deleteFolder(@PathVariable String folderId) {
		archiveService.deleteFolder(folderId);
	}

	/**
	 * To get a list of all archive root folders
	 * 
	 * @param request
	 * @return List<ArchivedFolder> of the archived folder
	 */

	 @Operation(summary = Swagger.GET_ARCHIVEROOT_FOLDERS_VALUE, description =
	 Swagger.GET_ARCHIVEROOT_FOLDERS_NOTE)

	@RequestMapping(value = "/my/archive/folders", method = RequestMethod.GET)

	@ResponseBody
	public List<ArchivedFolder> getRootFolders(HttpServletRequest request) {
		return archiveService.getFolders(UserHelper.getUserId(request), null);
	}

	/**
	 * To get a list of children folders of a given archive folder
	 * 
	 * @param folderId
	 *            as String
	 * @return List<ArchivedFolder> of the archived folder
	 */

	 @Operation(summary = Swagger.GET_ARCHIVEFOLDERS_VALUE, description =
	 Swagger.GET_ARCHIVEFOLDERS_NOTE)

	@RequestMapping(value = "/my/archive/folders/{folderId}/folders", method = RequestMethod.GET)

	@ResponseBody
	public List<ArchivedFolder> getFolders(@PathVariable String folderId,
			HttpServletRequest request) {
		return archiveService.getFolders(UserHelper.getUserId(request),
				folderId);
	}

	/**
	 * To archive test
	 * 
	 * @param archiveItem
	 * @param request
	 * @return ArchivedFolder
	 */

	@RequestMapping(value = "/my/archive/tests", method = RequestMethod.POST)

	@ResponseBody
	public ArchivedFolder archiveTest(

			@RequestBody ArchiveItem archiveItem, HttpServletRequest request) {

		String userId = UserHelper.getUserId(request);

		return archiveService.archiveTest(userId, archiveItem.getId(),
				archiveItem.getFolderId());
	}

	/**
	 * To restore Archived test
	 * 
	 * @param archiveItem
	 * @param request
	 * @return UserFolder
	 */

	@RequestMapping(value = "/my/restore/tests", method = RequestMethod.POST)

	@ResponseBody
	public UserFolder restoreTest(

			@RequestBody ArchiveItem archiveItem, HttpServletRequest request) {

		String userId = UserHelper.getUserId(request);

		return archiveService.restoreTest(userId, archiveItem.getId(),
				archiveItem.getFolderId());
	}

	/**
	 * To delete test
	 * 
	 * @param folderId
	 *            as String
	 * @param testId
	 *            as String
	 * @param request
	 */

	@Operation(summary = Swagger.DELETETEST_VALUE, description =
	 Swagger.DELETETEST_NOTE)

	@RequestMapping(value = "/my/delete/folders/{folderId}/tests/{testId}", method = RequestMethod.DELETE)

	@ResponseBody
	public void deleteTest(

			@PathVariable String folderId,

			@PathVariable String testId, HttpServletRequest request) {

		String userId = UserHelper.getUserId(request);
		archiveService.deleteTest(folderId, testId, userId);
	}

	/**
	 * To delete test at root level
	 * 
	 * @param testId
	 */

	@Operation(summary = Swagger.DELETEROOTTEST_VALUE, description =
	 Swagger.DELETEROOTTEST_NOTE)

	@RequestMapping(value = "/my/delete/tests/{testId}", method = RequestMethod.DELETE)

	@ResponseBody
	public void deleteRootTest(

			@PathVariable String testId, HttpServletRequest request) {

		String userId = UserHelper.getUserId(request);
		archiveService.deleteTest(null, testId, userId);
	}

	/**
	 * To list of all created tests for the given archive folder identified
	 * through the {folderid}
	 * 
	 * @param folderId
	 * @return List<TestMetadata>, list of test metadata
	 */
	@Operation(summary = Swagger.GET_ARCHIVEFOLDER_TESTS_VALUE, description =
	 Swagger.GET_ARCHIVEFOLDER_TESTS_NOTE)

	@RequestMapping(value = "/my/archive/folders/{folderId}/tests", method = RequestMethod.GET)

	@ResponseBody
	public List<TestMetadata> getTests(@PathVariable String folderId,
			HttpServletRequest request) {

		return archiveService.getTests(UserHelper.getUserId(request), folderId);
	}

}
