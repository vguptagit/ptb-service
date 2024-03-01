package com.pearson.ptb.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.pearson.ptb.util.Swagger;
import com.pearson.ptb.bean.TestEnvelop;
import com.pearson.ptb.bean.TestMetadata;
import com.pearson.ptb.bean.TestResult;
import com.pearson.ptb.bean.TestVersionInfo;
import com.pearson.ptb.service.MyTestService;
import com.pearson.ptb.service.TestVersionService;
import com.pearson.ptb.util.UserHelper;

import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * To save the user created test and get the list of all created tests for the
 * given folder and
 *
 */

@Tag(name = "MyTests", description = "My Tests")
@Controller

public class MyTestsController extends BaseController {

	@Autowired
	@Qualifier("myTestService")
	private MyTestService myTestService;

	@Autowired
	@Qualifier("testVersionService")
	private TestVersionService testVersionService;

	/**
	 * To save or update the user test
	 * 
	 * @param testEnvelop
	 * @param folderId
	 * @param request
	 * @param response
	 * @return TestResult
	 */

	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Success") })
	@Operation(summary = Swagger.SAVE_TEST_VALUE, description = Swagger.SAVE_TEST_NOTE)
	@RequestMapping(value = "my/folders/{folderId}/tests", method = RequestMethod.POST)
	@ResponseBody
	public TestResult saveTestEnvelop(@Parameter(name = "body") @RequestBody TestEnvelop testEnvelop,

			@PathVariable String folderId, HttpServletRequest request, HttpServletResponse response) {
		TestResult result = myTestService.saveTest(testEnvelop, UserHelper.getUserId(request), folderId);
		response.setStatus(HttpServletResponse.SC_CREATED);
		return result;
	}

	/**
	 * To fetch user test for the given folder
	 * 
	 * @param folderId
	 * @param request
	 * @return List<TestMetadata> of metadata
	 */

	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Success") })
	@Operation(summary = Swagger.GET_MYFOLDER_TESTS_VALUE, description = Swagger.GET_MYFOLDER_TESTS_NOTE)
	@RequestMapping(value = "my/folders/{folderId}/tests", method = RequestMethod.GET)

	@ResponseBody
	public List<TestMetadata> getMyFolderTests(@PathVariable String folderId, HttpServletRequest request,
			boolean flat) {
		return myTestService.getMyFolderTests(UserHelper.getUserId(request), folderId, flat);
	}

	/**
	 * To fetch the versions for the given test
	 * 
	 * @param versionInfo
	 * @param testId
	 * @param request
	 * @param response
	 * @return List<TestResult> of TestResult
	 */

	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Success") })
	@Operation(summary = Swagger.CREATE_TEST_VERSION, description = Swagger.CREATE_TEST_VERSION_NOTE)
	@RequestMapping(value = "my/tests/{testId}/versions", method = RequestMethod.POST)

	@ResponseBody
	public List<TestResult> createVersions(

			@Parameter(name = "body") @RequestBody TestVersionInfo versionInfo,

			@PathVariable String testId, HttpServletRequest request, HttpServletResponse response) {

		List<TestResult> result = testVersionService.createVersionTests(versionInfo, testId,
				UserHelper.getUserId(request));
		response.setStatus(HttpServletResponse.SC_CREATED);

		return result;

	}

	/**
	 * Importing the test by uploading the package
	 * 
	 * @param file
	 * @param request
	 * @return
	 */
	@Operation(summary = "Importing test", description = "Importing test")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Success") })

	@RequestMapping(value = "my/folders/{folderId}/tests/import", method = RequestMethod.POST)

	@ResponseBody
	public TestResult importTest(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
		return myTestService.importTest(file, UserHelper.getUserId(request));
	}
	
	
	@DeleteMapping("/delete/{folderId}")
	public void deleteFolder( @PathVariable String  folderId) {
		myTestService.deleteFolder(folderId);
		
	}

}
