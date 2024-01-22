package com.pearson.ptb.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pearson.ptb.bean.AnswerAreas;
import com.pearson.ptb.bean.AnswerKeys;
import com.pearson.ptb.bean.DownloadFormat;
import com.pearson.ptb.bean.DownloadOutput;
import com.pearson.ptb.bean.PageNumberDisplay;
import com.pearson.ptb.framework.exception.AuthTokenException;
import com.pearson.ptb.framework.exception.ExpectationException;
import com.pearson.ptb.framework.exception.InternalException;
import com.pearson.ptb.provider.pi.service.AuthenticationProvider;
import com.pearson.ptb.service.DownloadService;

import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * To download the specific test with specified format
 *
 */
@Controller

@Tag(name = "Tests", description = "tests")
public class TestDownloadController extends BaseController {

	private static final int PARAMETER_LENGTH = 2;

	@Autowired
	@Qualifier("downloadService")
	private DownloadService downloadService;

	/**
	 * To download the specific test
	 * 
	 * @param id
	 *            testId
	 * @param format
	 * @param data
	 * @param response
	 */
	// @ApiOperation(value = "Download test", notes = Swagger.GET_TESTS_NOTE)
	@RequestMapping(value = "/tests/{id}/download/{format}", method = RequestMethod.GET)
	@ResponseBody
	public void downloadTest(@PathVariable String id,
			@PathVariable DownloadFormat format,
			@RequestParam(required = false) String data,

			HttpServletResponse response) {

		try {

			Map<String, String> map = new HashMap<String, String>();

			String decodedURL = StringUtils
					.newStringUtf8(Base64.decodeBase64(data));

			getDownloadParams(map, decodedURL);

			String authenticateUser = authenticateRequest(map);

			if (authenticateUser != null) {

				doDownload(id, format, response, map, authenticateUser);
			}

		} catch (ExpectationException e) {
			throw e;
		} catch (IOException e) {
			throw new InternalException(
					"Exception while rendering the document", e);
		}

	}

	private void doDownload(String id, DownloadFormat format,
			HttpServletResponse response, Map<String, String> map,
			String authenticateUser) throws IOException {

		DownloadOutput output = null;
		ByteArrayOutputStream dstStream = new ByteArrayOutputStream();

		AnswerKeys answerkeys = map.get("answerKey") == null
				? null
				: AnswerKeys.valueOf(map.get("answerKey"));
		AnswerAreas answerareas = map.get("answerArea") == null
				? null
				: AnswerAreas.valueOf(map.get("answerArea"));
		PageNumberDisplay pageNumberDisplay = map
				.get("pageNumberDisplay") == null
						? null
						: PageNumberDisplay
								.valueOf(map.get("pageNumberDisplay"));
		try {
			output = downloadService.downloadTest(dstStream, id,
					authenticateUser, format, answerkeys, answerareas,
					Boolean.parseBoolean(map.get("includeRandomizedTests")),
					Boolean.parseBoolean(map.get("includeStudentName")),
					Boolean.parseBoolean(map.get("saveSettings")),
					map.get("margin"), pageNumberDisplay);
			response.addHeader("contentType",
					"application/" + output.getContentType());
			response.addHeader("content-disposition",
					"attachment; filename=\"" + output.getFileName() + "\"");
			response.getOutputStream().write(dstStream.toByteArray());
		} catch (ExpectationException e) { // NOSONAR

			response.addHeader("contentType", "text/html");
			response.getOutputStream()
					.write("No versions are there for this test".getBytes());
		} finally {
			dstStream.close();
		}
	}

	private String authenticateRequest(Map<String, String> map) {
		String authenticateUser = null;
		try {

			String authToken = null;
			authToken = map.get("AT");
			AuthenticationProvider authProvider = new AuthenticationProvider();
			authenticateUser = authProvider.authenticate(authToken);

		} catch (AuthTokenException e) {
			throw new InternalException(
					"Exception while authenticating user ID:", e);

		}
		return authenticateUser;
	}

	private void getDownloadParams(Map<String, String> map, String decodedURL) {
		String[] params = decodedURL.split("[/$]");
		for (String param : params) {
			if (param.split("=").length == PARAMETER_LENGTH) {
				String name = param.split("=", PARAMETER_LENGTH)[0];
				String value = param.split("=", PARAMETER_LENGTH)[1];
				map.put(name, value);
			} else {
				String name = param.split("=", PARAMETER_LENGTH)[0];
				String value = param.substring(
						param.split("=", PARAMETER_LENGTH)[0].length() + 1);
				map.put(name, value);
			}

		}
	}
}
