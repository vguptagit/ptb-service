package com.pearson.ptb.service;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
//import com.pearson.mytest.proxy.mytest.repo.ArchiveRepo;
import org.springframework.stereotype.Service;

import com.pearson.ptb.bean.AnswerAreas;
import com.pearson.ptb.bean.AnswerKeys;
import com.pearson.ptb.bean.AssignmentBinding;
import com.pearson.ptb.bean.AssignmentBindingSorter;
import com.pearson.ptb.bean.DownloadFormat;
import com.pearson.ptb.bean.DownloadInfo;
import com.pearson.ptb.bean.DownloadOutput;
import com.pearson.ptb.bean.Metadata;
import com.pearson.ptb.bean.PageNumberDisplay;
import com.pearson.ptb.bean.PrintSettings;
import com.pearson.ptb.bean.Test;
import com.pearson.ptb.bean.UserSettings;
import com.pearson.ptb.download.DownloadFactory;
import com.pearson.ptb.download.TestDownload;
import com.pearson.ptb.framework.exception.ConfigException;
import com.pearson.ptb.framework.exception.ExpectationException;
import com.pearson.ptb.framework.exception.InternalException;
import com.pearson.ptb.framework.exception.NotFoundException;
import com.pearson.ptb.framework.exception.ServiceUnavailableException;
import com.pearson.ptb.proxy.QuestionDelegate;
import com.pearson.ptb.proxy.TestDelegate;
import com.pearson.ptb.proxy.TestVersionDelegate;
import com.pearson.ptb.proxy.UserSettingsDelegate;

/**
 * Download the test or question in the form of mentioned parameter of the
 * constructor. This may download in the form of word, pdf, qti, ...
 */
@Service
public class DownloadService {

	@Autowired
	@Qualifier("tests")
	private TestDelegate tests;

	@Autowired
	@Qualifier("usersettings")
	private UserSettingsDelegate usersettings;

	@Autowired
	@Qualifier("questions")
	private QuestionDelegate questions;

	@Autowired
	@Qualifier("testVersionRepo")
	private TestVersionDelegate testVersionRepo;

	@Autowired
	@Qualifier("metadataService")
	private MetadataService metadataService;

	/**
	 * Download the test based on the format mentioned in the
	 * <code>format</code>, constructor parameter
	 * 
	 * @param testID
	 *            , test id number which needs to be downloaded.
	 * @param request
	 *            , servlet request to get the user id for getting usersettings.
	 * @param response
	 *            , servlet response to save the stream and render the stream.
	 * @param pagesetup
	 *            : page setup details.
	 * @throws NotFoundException
	 * @throws InternalException
	 * @throws ConfigException
	 * @throws ServiceUnavailableException
	 * @throws UnknownHostException
	 */

	public DownloadOutput downloadTest(OutputStream stream, String testid,
			String userid, DownloadFormat format, AnswerKeys answerKey,
			AnswerAreas answerArea, boolean includeRandomizedTests,
			boolean includeStudentName, boolean saveSettings, String margin,
			PageNumberDisplay pageNumberDisplay) {

		DownloadOutput downloadOutput = null;
		TestDownload downloder = DownloadFactory.getDownloader(format);
		try {

			DownloadInfo downloadInfo = getDownloadInfo(testid, userid, format,
					answerKey, answerArea, includeRandomizedTests,
					includeStudentName, saveSettings, margin,
					pageNumberDisplay);
			downloadOutput = downloadFile(stream, testid,
					includeRandomizedTests, downloder, downloadInfo);

		} catch (ExpectationException e) {
			throw e;
		} catch (Exception e) {
			throw new InternalException(
					"Exception while downloading the document", e);
		}
		return downloadOutput;
	}

	/**
	 * 
	 * @param stream
	 * @param testid
	 * @param includeRandomizedTests
	 * @param downloder
	 * @param downloadInfo
	 * @return
	 * @throws IOException
	 */
	private DownloadOutput downloadFile(OutputStream stream, String testid,
			boolean includeRandomizedTests, TestDownload downloder,
			DownloadInfo downloadInfo) throws IOException {

		DownloadOutput downloadOutput = null;

		if (includeRandomizedTests) {
			downloadOutput = downloadAllVersions(stream, testid, downloder,
					downloadInfo);
		} else {
			downloadOutput = downloder.download(stream, downloadInfo);
		}
		return downloadOutput;
	}

	/**
	 * 
	 * @param downloadOutput
	 * @param stream
	 * @param testid
	 * @param downloder
	 * @param downloadInfo
	 * @return
	 * @throws IOException
	 */
	private DownloadOutput downloadAllVersions(OutputStream stream,
			String testid, TestDownload downloder, DownloadInfo downloadInfo)
			throws IOException {

		String testMetadata;
		List<String> activityURL = new ArrayList<String>();
		List<String> activityGUID = new ArrayList<String>();
		DownloadOutput downloadOutput = new DownloadOutput();
		List<DownloadInfo> validVersionlst = new ArrayList<DownloadInfo>();
		Set<String> addedNames = new HashSet<String>();
		DownloadInfo parentTest = downloadInfo.clone();
		addedNames.add(parentTest.getTestTitle());

		try {

			testMetadata = testVersionRepo.getTestVersions(testid);
			activityURL = Arrays.asList(testMetadata.split("\r\n?|\n"));

			int versionNo = 1;
			for (String url : activityURL) {
				activityGUID = Arrays.asList(url.split("/"));
				String guid = activityGUID.get(activityGUID.size() - 1);

				boolean isNewlyAdded = checkForValidVersionedTest(guid,
						parentTest.getTestTitle(), downloadInfo, addedNames,
						versionNo, validVersionlst);
				if (isNewlyAdded) {
					versionNo++;
				}
			}

		} catch (NotFoundException e) { 
		} catch (Exception e) {
			throw new InternalException("Exception while getting versions", e);
		}

		if (!validVersionlst.isEmpty()) {

			ZipOutputStream zipOuputStream = new ZipOutputStream(
					new BufferedOutputStream(stream));
			String zipName = parentTest.getTestTitle() + ".zip";
			downloadTestAnswerKeys(zipOuputStream, downloder, parentTest);

			for (DownloadInfo validdownloadInfo : validVersionlst) {
				downloadTestAnswerKeys(zipOuputStream, downloder,
						validdownloadInfo);
			}

			zipOuputStream.close();

			downloadOutput.setContentType("zip");
			downloadOutput.setFileName(zipName);
		} else {
			throw new ExpectationException(
					"No versions are there for this test");
		}

		return downloadOutput;
	}

	/**
	 * 
	 * @param guid
	 * @param parentTestTitle
	 * @param downloadInfo
	 * @param addedNames
	 * @param versionNo
	 * @return
	 */
	private boolean checkForValidVersionedTest(String guid,
			String parentTestTitle, DownloadInfo downloadInfo,
			Set<String> addedNames, int versionNo,
			List<DownloadInfo> validVersionLst) {
		boolean isNewlyAdded = false;
		
		Metadata metadata = metadataService.getMetadata(guid);
		if (metadata != null) {
//			String versionedTestTitle = parentTestTitle + "_v"
//					+ metadata.getVersion();

			/*
			 * if (archiveRepo.getTestFolder(metadata.getGuid()) == null &&
			 * metadata.getTitle().equals(versionedTestTitle)) {
			 * 
			 * setTestDetails(guid, downloadInfo);
			 * 
			 * if (!addedNames.add(downloadInfo.getTestTitle())) {
			 * downloadInfo.setTestTitle(metadata.getTitle() + "(" + versionNo +
			 * ")"); addedNames.add(downloadInfo.getTestTitle()); isNewlyAdded =
			 * true; } validVersionLst.add(downloadInfo.clone()); }
			 */
		}
		return isNewlyAdded;
	}

	/**
	 * 
	 * @param zipOuputStream
	 * @param downloader
	 * @param downloadInfo
	 * @param docs
	 */
	private void downloadTestAnswerKeys(ZipOutputStream zipOuputStream,
			TestDownload downloader, DownloadInfo downloadInfo) {

		try {

			if (downloadInfo.getPrintSettings().getIncludeAnswerKeyIn()
					.equals(AnswerKeys.SEPARATEFILE)) {

				
				downloadInfo.getPrintSettings()
						.setIncludeAnswerKeyIn(AnswerKeys.NONE);
				downloadToZip(zipOuputStream, downloader, downloadInfo, false);

				
				downloadInfo.getPrintSettings()
						.setIncludeAnswerKeyIn(AnswerKeys.SEPARATEFILE);
				downloadToZip(zipOuputStream, downloader, downloadInfo, true);

			} else {

				
				downloadToZip(zipOuputStream, downloader, downloadInfo, false);

			}
		} catch (Exception e) {
			throw new InternalException(
					"Exception while downloading the document", e);
		}
	}

	/***
	 * 
	 * @param zipOuputStream
	 * @param downloader
	 * @param downloadInfo
	 */
	private void downloadToZip(ZipOutputStream zipOuputStream,
			TestDownload downloader, DownloadInfo downloadInfo,
			boolean isAnsweyKey) {

		try {
			zipOuputStream.putNextEntry(new ZipEntry(
					getZipEntryTitle(downloader, downloadInfo, isAnsweyKey)));
			downloader.download(zipOuputStream, downloadInfo);
			zipOuputStream.closeEntry();
		} catch (IOException e) {
			throw new InternalException(
					"Error in creating zip for test download", e);
		}
	}

	/***
	 * Returns the file name of the file to be downloaded
	 * 
	 * @param downloader
	 * @param downloadInfo
	 * @return
	 */
	private String getZipEntryTitle(TestDownload downloader,
			DownloadInfo downloadInfo, boolean isAnsweyKey) {
		if (isAnsweyKey) {
			return downloadInfo.getTestTitle() + "_AnswerKeys"
					+ downloader.extension();

		} else {
			return downloadInfo.getTestTitle() + downloader.extension();
		}

	}

	/**
	 * Gets the information which is need to write the document
	 * 
	 * @param testID
	 * @param userID
	 * @param downLoadOption
	 * @return
	 * @throws NotFoundException
	 * @throws InternalException
	 * @throws ServiceUnavailableException
	 * @throws ConfigException
	 * @throws UnknownHostException
	 */
	private DownloadInfo getDownloadInfo(String testid, String userid,
			DownloadFormat format, AnswerKeys answerKey, AnswerAreas answerArea,
			boolean includeRandomizedTests, boolean includeStudentName,
			boolean saveSettings, String margin,
			PageNumberDisplay pageNumberDisplay) {
		DownloadInfo downloadInfo = new DownloadInfo();

		setTestDetails(testid, downloadInfo);

		setPrintSettings(userid, format, answerKey, answerArea,
				includeRandomizedTests, includeStudentName, saveSettings,
				downloadInfo, margin, pageNumberDisplay);

		return downloadInfo;
	}

	/**
	 * Gets the test details including question xml
	 * 
	 * @param testID
	 * @return
	 * @throws InternalException
	 * @throws NotFoundException
	 */

	private void setTestDetails(String testid, DownloadInfo downloadInfo) {
		Test testDetail = tests.getTestByID(testid);
		downloadInfo.setTestTitle(testDetail.getTitle());
		downloadInfo.setQuestions(getQuestions(testDetail));
	}

	/**
	 * 
	 * @param userid
	 * @param format
	 * @param answerKey
	 * @param answerArea
	 * @param includeRandomizedTests
	 * @param includeStudentName
	 * @param saveSettings
	 * @param downloadInfo
	 */
	private void setPrintSettings(String userid, DownloadFormat format,
			AnswerKeys answerKey, AnswerAreas answerArea,
			boolean includeRandomizedTests, boolean includeStudentName,
			boolean saveSettings, DownloadInfo downloadInfo, String margin,
			PageNumberDisplay pageNumberDisplay) {
		PrintSettings printSettings = getPrintSettings(userid);
		printSettings.setFont("HelveticaNeue-Light");
		printSettings.setFontSize("12");

		if (format != null) {
			printSettings.setExportFileFormat(format);
		}
		if (format != null) {
			printSettings.setIncludeAreaForStudentResponse(answerArea);
		}
		if (answerArea != null) {
			printSettings.setIncludeAnswerKeyIn(answerKey);
		}
		printSettings.setIncludeRandomizedTests(includeRandomizedTests);
		printSettings.setIncludeStudentName(includeStudentName);
		if (margin != null) {
			printSettings.setLeftMargin(margin);
			printSettings.setRightMargin(margin);
			printSettings.setBottomMargin(margin);
			printSettings.setTopMargin(margin);
		}

		if (pageNumberDisplay != null) {
			printSettings.setPageNumberDisplay(pageNumberDisplay);
		}

		if (saveSettings) {
			usersettings.savePrintSettings(userid, printSettings);
		}

		downloadInfo.setPrintSettings(printSettings);
	}

	/**
	 * Extracts the question XML out of test and fills in to hash map
	 * 
	 * @param testDetail
	 * @return
	 * @throws NotFoundException
	 * @throws InternalException
	 */
	private Queue<String> getQuestions(Test testdetail) {

		Queue<String> questionQueue = new LinkedList<String>();

		List<AssignmentBinding> bindings = testdetail.getAssignmentContents()
				.getBinding();

		Collections.sort(bindings, new AssignmentBindingSorter());

		for (AssignmentBinding assignmentBinding : bindings) {
			String qtiXML = null;
			qtiXML = questions.getQuestionXmlById(assignmentBinding.getGuid());
			questionQueue.add(qtiXML);
		}

		return questionQueue;
	}

	/**
	 * Gets user print settings details.
	 * 
	 * @param userID
	 * @return
	 * @throws NotFoundException
	 * @throws ServiceUnavailableException
	 * @throws ConfigException
	 * @throws UnknownHostException
	 */
	private PrintSettings getPrintSettings(String userid) {
		UserSettings userSettings = usersettings.getUserSettings(userid);
		return userSettings.getPrintSettings();
	}

}
