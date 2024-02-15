package com.pearson.ptb.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.pearson.ptb.bean.AssignmentBinding;
import com.pearson.ptb.bean.AssignmentContent;
import com.pearson.ptb.bean.Metadata;
import com.pearson.ptb.bean.QuestionEnvelop;
import com.pearson.ptb.bean.Test;
import com.pearson.ptb.bean.TestBinding;
import com.pearson.ptb.bean.TestEnvelop;
import com.pearson.ptb.bean.TestMetadata;
import com.pearson.ptb.bean.TestResult;
import com.pearson.ptb.bean.UserFolder;
import com.pearson.ptb.bean.UserQuestionsFolder;
import com.pearson.ptb.framework.CacheWrapper;
import com.pearson.ptb.framework.LogWrapper;
import com.pearson.ptb.framework.TestImporter;
import com.pearson.ptb.framework.exception.BadDataException;
import com.pearson.ptb.framework.exception.ConfigException;
import com.pearson.ptb.framework.exception.DuplicateTitleException;
import com.pearson.ptb.framework.exception.InternalException;
import com.pearson.ptb.framework.exception.NotFoundException;
import com.pearson.ptb.proxy.MyTestDelegate;
import com.pearson.ptb.proxy.aws.bean.QuestionResponse;
import com.pearson.ptb.util.CacheKey;
import com.pearson.ptb.util.Converter;

@Service("myTestService")
public class MyTestService {

	@Autowired
	@Qualifier("myTestsRepo")
	private MyTestDelegate myTestsRepo;

	@Autowired
	@Qualifier("metadataService")
	private MetadataService metadataService;

	@Autowired
	@Qualifier("userFolderService")
	private UserFolderService userFolderService;

	@Autowired
	@Qualifier("imageService")
	private ImageService imageService;

	@Autowired
	@Qualifier("questionService")
	private QuestionService questionService;

	private static CacheWrapper CACHE;

	/**
	 * This constructor initializes the instance of the cache wrapper object for
	 * caching operations.
	 * 
	 */
	public MyTestService() {
		
	}

	public void initializeCache() {
		CACHE = CacheWrapper.getInstance();
	}

	/**
	 * Create paper test inside the specified user folder
	 * 
	 * @param test
	 *            paper test envelope contains test and meta data
	 * @param userId
	 *            test has been created for the user.
	 * @param folderId
	 *            The folder Id in which test should be created
	 * @return Newly created test details
	 * @throws NotFoundException
	 * @throws ConfigException
	 * @throws InternalException
	 * @throws BadDataException
	 */
	public TestResult saveTest(TestEnvelop test, String userId,
			String folderId) {
		TestResult result = null;
		UserFolder folder = this.getUserFolder(userId, folderId);

		if (test.getmetadata().getGuid() == null) {

			result = myTestsRepo.create(test);

			if (folder != null) {
				double sequence = 1.0;

				List<TestBinding> testBindings = folder.getTestBindings();
				if (!testBindings.isEmpty()) {
					sequence = testBindings.get(testBindings.size() - 1)
							.getSequence() + 1;
				}

				TestBinding testBinding = new TestBinding();
				testBinding.setTestId(result.getGuid());
				testBinding.setSequence(sequence);

				testBindings.add(testBinding);

				folder.setTestBindings(testBindings);

				userFolderService.updateFolder(folder);
			}
		} else {

			result = myTestsRepo.update(test);

			CACHE.delete(String.format(CacheKey.TEST_FORMAT,
					test.getmetadata().getGuid()));
			CACHE.delete(String.format(CacheKey.METADATA_FORMAT,
					test.getmetadata().getGuid()));
			CACHE.delete(String.format(CacheKey.TEST_QUESTIONS_FORMAT,
					test.getmetadata().getGuid()));
		}

		return result;
	}

	/**
	 * Get the test lists from the repository which are created under specific
	 * folder
	 * 
	 * @param userId
	 *            get all tests created by User
	 * @param folderId
	 *            folder Id where all created tests are accessed
	 * @return list of test which are listed under folder having folderID
	 * @throws NotFoundException
	 *             Application custom exception
	 * @throws InternalException
	 *             Application custom exception
	 * @throws BadDataException
	 *             Application custom exception
	 */
	public List<TestMetadata> getMyFolderTests(String userId, String folderId,
			boolean flat) {

		UserFolder folder;
		if (folderId == null || folderId.equals("null")) {
			folder = userFolderService.getMyTestRoot(userId);
		} else {
			folder = this.getUserFolder(userId, folderId);
		}

		if (flat) {
			List<TestMetadata> tests = new ArrayList<TestMetadata>();
			fillTestsFlatView(tests, folder);
			return tests;
		} else {
			return this.getMyFolderTests(folder);
		}

	}

	private void fillTestsFlatView(List<TestMetadata> tests,
			UserFolder folder) {
		List<UserFolder> folders;
		folders = userFolderService.getFolders(folder.getGuid());
		tests.addAll(getMyFolderTests(folder));
		for (UserFolder userFolder : folders) {
			fillTestsFlatView(tests, userFolder);
		}
	}
	/**
	 * Get the test lists from the repository which are created under specific
	 * folder
	 * 
	 * @param folder
	 *            get all tests available inside the folder
	 * @return list of test which are listed under folder having folderID
	 */
	public List<TestMetadata> getMyFolderTests(UserFolder folder) {

		List<TestMetadata> tests = new ArrayList<TestMetadata>();

		String testId;
		for (TestBinding testBinding : folder.getTestBindings()) {

			testId = testBinding.getTestId();
			if (testId != null) {

				try {
					Metadata metadata = metadataService.getMetadata(testId);
					TestMetadata testMetadata = Converter.getDestinationBean(
							metadata, TestMetadata.class, Metadata.class);

					tests.add(testMetadata);
				} catch (NotFoundException ex) {
					final Logger LOG = LogWrapper
							.getInstance(ArchiveService.class);
					LOG.error("Test Id = " + testId + " not found");
					jakarta.servlet.http.HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder
							.currentRequestAttributes()).getResponse();
					response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
				}
			}
		}

		return tests;
	}

	/**
	 * Gets the max sequence of a test available inside the folder
	 * 
	 * @param folder
	 *            the folder object is used to get max sequence of tests
	 *            available inside folder
	 * @return the max tests sequence of the give folder
	 */
	public double getFolderTestsMaxExtMetadataSequence(UserFolder folder) {

		List<TestBinding> testBindings = folder.getTestBindings();
		double sequence = 0.0;

		int testsLastIndex = testBindings.size();
		sequence = testsLastIndex;
		double extMetadataSequence = testBindings.get(testsLastIndex - 1)
				.getSequence();
		if (extMetadataSequence > 0.0) {
			sequence = extMetadataSequence;
		}

		return sequence;
	}

	/**
	 * Get the user folder and if folder is not available then return null
	 * 
	 * @param userId
	 *            the User id
	 * @param folderId
	 *            the folder id
	 * @return folder object
	 */
	private UserFolder getUserFolder(String userId, String folderId) {

		UserFolder folder = new UserFolder();
		if (folderId == null || "null".equals(folderId)) {
			folder = userFolderService.getMyTestRoot(userId);
		} else {
			folder = userFolderService.getFolder(userId, folderId);
		}

		return folder;
	}

	/**
	 * Importing the test which will be in zip package and store it in YourTest
	 * tab. 1. Validating the size and format of the uploaded file 2. Initiating
	 * TestImporter class meant for extracting the zip file. 3. Validating the
	 * Test title duplication 4. Validating the package consists of required
	 * files. 5. Saving the images which identified in package. 6. Getting the
	 * list of Question envelpe from package. 7. Getting the question folder
	 * having the title has test title. 8. Creating the question folder if there
	 * is no question folder with test title. 9. Saving the question in the PAF.
	 * 10. Building the test envelope 11. Saving the test to PAF. 12. Deleting
	 * the UserQuestions folder is there is any exception
	 * 
	 * @param file,
	 *            uploaded Multipart file
	 * @param userID
	 * @return
	 */
	public TestResult importTest(MultipartFile file, String userID) {
		TestResult testResult = null;
		TestImporter importer = null;
		try {
			initialValidation(file);

			importer = new TestImporter(file);

			importer.validatePackage();

			String testTitle = "Import - " + importer.getTestTitle();
			validateDuplicateTestTitle(userID, testTitle);

			saveImages(importer);

			List<QuestionEnvelop> questions = importer.getQuestions();

			UserQuestionsFolder questionFolder = userFolderService
					.getMyQuestionsFolder(userID);
			String parentId = questionFolder.getGuid();
			questionFolder = userFolderService.getFolderByTitle(testTitle,
					parentId, userID);

			boolean isQuestionFolderExists = true;
			if (questionFolder == null) {
				isQuestionFolderExists = false;
				questionFolder = buildNewQuestionsFolder(userID, testTitle,
						parentId);
			}

			try {
				List<QuestionResponse> questionResults = questionService.saveQuestions(
						questions, userID,
						(UserQuestionsFolder) questionFolder);

				TestEnvelop testEnvelop = buildTestEnvelope(testTitle,
						questionResults);

				testResult = saveTest(testEnvelop, userID, null);
				if (testResult == null) {
					throw new InternalException("Fail to save test");
				}
			} catch (Exception e) {
				if (!isQuestionFolderExists) {
					userFolderService
							.deleteQuestionFolder(questionFolder.getGuid());
				} else
					userFolderService.saveUserQuestionFolder(questionFolder,
							userID);
				throw new InternalException("Fail to save test");
			}
		} finally {
			importer.clearData();
		}
		return testResult;
	}

	/**
	 * Building the test envelope
	 * 
	 * @param testTitle
	 * @param questionResults
	 * @return
	 */
	private TestEnvelop buildTestEnvelope(final String testTitle,
			List<QuestionResponse> questionResults) {
		TestEnvelop testEnvelop = new TestEnvelop();
		Metadata metadata = new Metadata();
		metadata.setCrawlable("true");
		Test test = new Test();
		test.setContext(
				"http://purl.org/pearson/paf/v1/ctx/core/StructuredAssignment");
		test.setType("StructuredAssignment");

		AssignmentContent assignmentContent = new AssignmentContent();
		assignmentContent.setContentType(
				"application/vnd.pearson.paf.v1.assignment+json");

		assignmentContent.setBinding(new ArrayList<AssignmentBinding>());
		JsonParser parser = new JsonParser();
		int i = 0;
		JsonArray array;
		JsonObject object;
		JsonElement id;
		for (QuestionResponse result : questionResults) {

			AssignmentBinding assignmentBinding = new AssignmentBinding();
			assignmentBinding.setGuid(result.getGuid());
			assignmentBinding.setActivityFormat(
					"application/vnd.pearson.qti.v2p1.asi+xml");
			assignmentBinding.setBindingIndex(i);
			i++;
			assignmentContent.getBinding().add(assignmentBinding);
		}

		test.setAssignmentContents(assignmentContent);
		test.setTitle(testTitle);
		metadata.setTitle(testTitle);

		testEnvelop.setBody(test);
		testEnvelop.setmetadata(metadata);
		return testEnvelop;
	}

	/**
	 * Building the new questions folder with uploaded test title and login user
	 * 
	 * @param userID
	 * @param testTitle
	 * @param parentId
	 * @return
	 */
	private UserQuestionsFolder buildNewQuestionsFolder(String userID,
			final String testTitle, String parentId) {
		UserQuestionsFolder questionFolder;
		double userQuestionsfolderSeq = userFolderService
				.getUserQuestionsFolderMinSeq(userID);
		userQuestionsfolderSeq = userQuestionsfolderSeq / Math.pow(2, 1);
		UserQuestionsFolder newQuestionFolder = new UserQuestionsFolder();
		newQuestionFolder.setTitle(testTitle);
		newQuestionFolder.setGuid(null);
		newQuestionFolder.setSequence(userQuestionsfolderSeq);
		newQuestionFolder.setParentId(parentId);
		questionFolder = userFolderService
				.saveUserQuestionFolder(newQuestionFolder, userID);
		return questionFolder;
	}
	/**
	 * Saving images to EPS and adding it to map of TestImporter
	 * 
	 * @param importer
	 */
	private void saveImages(TestImporter importer) {
		Map<String, MultipartFile> imageMap = importer.getImageMap();
		String imageUrl;
		for (Map.Entry<String, MultipartFile> entry : imageMap.entrySet()) {
			imageUrl = imageService.uploadImage(entry.getValue());
			importer.addImage(entry.getKey(), imageUrl);
		}
	}
	/**
	 * Validating the uploaded test title is already present
	 * 
	 * @param userID
	 * @param testTitle
	 */
	private void validateDuplicateTestTitle(String userID, String testTitle) {
		List<TestMetadata> tests = getMyFolderTests(userID, null, false);
		
		boolean existTest = false;
		for (TestMetadata testMetadata : tests) {
			if (testMetadata.getTitle() != null
					&& testMetadata.getTitle().equals(testTitle)) {
				existTest = true;
				break;
			}
		}

		if (existTest) {
			throw new DuplicateTitleException("Test already present");
		}
	}
	/**
	 * Validating the size and format of the uploaded file.
	 * 
	 * @param file
	 */
	private void initialValidation(MultipartFile file) {
		if (isInValidPackage(file.getContentType())) {
			throw new BadDataException("Invalid file");
		}
		
		if (file.getSize() > (1024 * 1024 * 4)) {
			throw new BadDataException("Exceed file size limit");
		}
	}
	private boolean isInValidPackage(String contentType) {
		switch (contentType) {
			case "application/octet-stream" :
			case "application/x-rar-compressed" :
			case "application/zip" :
			case "application/x-zip-compressed" :
				return false;
		}
		return true;
	}

}
