package com.pearson.ptb.service;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.pearson.ptb.bean.AssignmentBinding;
import com.pearson.ptb.bean.ExtMetadata;
import com.pearson.ptb.bean.Metadata;
import com.pearson.ptb.bean.QuestionEnvelop;
import com.pearson.ptb.bean.QuestionOutput;
import com.pearson.ptb.bean.Test;
import com.pearson.ptb.bean.TestBinding;
import com.pearson.ptb.bean.TestEnvelop;
import com.pearson.ptb.bean.TestResult;
import com.pearson.ptb.bean.TestScrambleType;
import com.pearson.ptb.bean.TestVersionInfo;
import com.pearson.ptb.bean.UserFolder;
import com.pearson.ptb.framework.QTIParser;
import com.pearson.ptb.framework.exception.BadDataException;
import com.pearson.ptb.framework.exception.ConfigException;
import com.pearson.ptb.framework.exception.InternalException;
import com.pearson.ptb.framework.exception.NotFoundException;
import com.pearson.ptb.framework.exception.ServiceUnavailableException;
import com.pearson.ptb.proxy.TestVersionDelegate;
import com.pearson.ptb.util.Common;
import com.pearson.ptb.util.QuestionTypes;

/**
 * The <code>TestVersionService</code> contains the operation which is requied
 * to manger test versions
 * 
 * @author nithinjain
 *
 */
@Service("testVersionService")
public class TestVersionService {

	@Autowired
	@Qualifier("userFolderService")
	private UserFolderService userFolderService;

	@Autowired
	@Qualifier("testService")
	private TestService testService;

	@Autowired
	@Qualifier("testVersionRepo")
	private TestVersionDelegate testVersionRepo;

	@Autowired
	@Qualifier("metadataService")
	private MetadataService metadataService;

	@Autowired
	@Qualifier("myTestService")
	private MyTestService myTestService;

	@Autowired
	@Qualifier("questionService")
	private QuestionService questionService;

	@Autowired
	@Qualifier("archiveService")
	private ArchiveService archiveService;

	QTIParser qtiParser = new QTIParser();
	List<String> versionedTestMetadata;
	

	/**
	 * creates the test from the given existing test by applying the question
	 * and answer key scramble
	 * 
	 * @param versionInfo
	 *            the information of version
	 * @param folderId
	 *            the folder in which version tests creates
	 * @param testId
	 *            the test id which is give to created version tests
	 * @param userId
	 *            the user id who creates the version test
	 * @return the newly created test result
	 * @throws BadDataException
	 * @throws ServiceUnavailableException
	 * @throws NotFoundException
	 * @throws InternalException
	 * @throws ConfigException
	 */
	public List<TestResult> createVersionTests(TestVersionInfo versionInfo,
			String testId, String userId) {

		UserFolder testFolder = userFolderService.getTestFolder(testId);
		
		versionedTestMetadata=new ArrayList<String>();
		versionedTestMetadata.add(testId);
		
		UserFolder folder = null;
		try {
			folder = userFolderService.getFolder(userId, testFolder.getGuid());
		} catch (NotFoundException e) {
			throw new BadDataException("This test does not belong to the user",
					e);
		}

		// Validate the version bean
		versionInfo.validateState();
		List<TestResult> results = createVersionTests(versionInfo, testId,
				folder);
		
		List<TestBinding> testBindings = testFolder.getTestBindings();
		int index = 0, 
		indexToInsert = 0;
		double sequence=0;
		double nextSequence=0;
		for (TestBinding test : testBindings) {
			if(versionedTestMetadata.contains(test.getTestId())){
				sequence=test.getSequence();
				indexToInsert = index;	
			}
			index = index + 1;
		}

		int startIndex=indexToInsert+1;
		int divideByTwo = 2;
		
		if(startIndex<testBindings.size()){
			nextSequence=testBindings.get(startIndex).getSequence();
		}
		
		for (TestResult result : results) {
			if(nextSequence==0){
				sequence=sequence+=1;
			}else{
				sequence=(sequence+nextSequence)/divideByTwo;
			}
			TestBinding testBinding = new TestBinding();
			testBinding.setTestId(result.getGuid());
			testBinding.setSequence(sequence);
			testBindings.add(startIndex, testBinding);
			startIndex+=1;
		}
		testFolder.setTestBindings(testBindings);
		userFolderService.saveFolder(testFolder, userId);

		return results;
	}

	/**
	 * creates the version tests
	 * 
	 * @param versionInfo
	 *            the information of version
	 * @param testId
	 *            the test id which is give to created version tests
	 * @return the newly created test result
	 * @throws InternalException
	 * @throws NotFoundException
	 * @throws BadDataException
	 */
	private List<TestResult> createVersionTests(TestVersionInfo versionInfo,
			String testId, UserFolder folder) {

		List<TestResult> results = new ArrayList<TestResult>();
		try {
			List<String> questionList = new ArrayList<String>();
			List<String> versionedTestNames = new ArrayList<String>();

			// Get the test envelop of a given test
			TestEnvelop testEnvelop = this.getTestEnvelop(testId);
			double maxSequence = myTestService
					.getFolderTestsMaxExtMetadataSequence(folder);
			// Get the source test title which is passed to create version tests
			String testTitile = testEnvelop.getBody().getTitle();

			List<AssignmentBinding> originalBindings = testEnvelop.getBody()
					.getAssignmentContents().getBinding();

			int possibleVersions = getFactorial(originalBindings.size());

			List<AssignmentBinding> copiedBindings = cloneBindings(originalBindings);

			fillVersionedTestQuestionsList(originalBindings, questionList, true);
			getTestVersionDetails(testId, questionList, versionedTestNames,
					testTitile, possibleVersions);

			int existingNoOfVersions = versionedTestNames.size();
			int totalVersions = existingNoOfVersions
					+ versionInfo.getNoOfVersions();

			// Loop through for number of version count to create that many
			// version tests
			for (int versionNo = 1; versionNo <= totalVersions; versionNo++) {
				boolean isNameExists = false;
				// New version activity title preparation
				String versionTestTitile = testTitile + "_v" + versionNo;
				for (String titleName : versionedTestNames) {
					if (titleName.equals(versionTestTitile)) {
						isNameExists = true;
						break;
					}
				}

				if (!isNameExists) {
					maxSequence = maxSequence + versionNo;
					// Updating the sequence extended metadata to show the newly
					// created test at last position
					this.updateSequenceExtMetadata(testEnvelop.getmetadata(),
							maxSequence);
					testEnvelop.getBody().setTitle(versionTestTitile);
					testEnvelop.getmetadata().setTitle(versionTestTitile);
					testEnvelop.getmetadata().setVersion(
							String.valueOf(versionNo));

					List<AssignmentBinding> shuffledBindings = getScrambledTestEnvelop(
							versionInfo.getScrambleType(), copiedBindings,
							folder, versionNo, existingNoOfVersions,
							possibleVersions, questionList);

					testEnvelop.getBody().getAssignmentContents()
							.setBinding(shuffledBindings);

					results.add(testVersionRepo.createVersionTest(testEnvelop,
							testId));
				}
			}
		} catch (Exception e) {
			throw new InternalException("", e);
		}

		return results;
	}

	/**
	 * Gets the list of test URLs of test versions for the test
	 * 
	 * @param testId
	 * @return list of URLs
	 */
	private void getTestVersionDetails(String testId,
			List<String> questionList, List<String> versionedTestNames,
			String testTitile, int possibleVersions) {
		String testMetadata;
		List<String> activityList = new ArrayList<String>();
		try {
			testMetadata = testVersionRepo.getTestVersions(testId);
			activityList = Arrays.asList(testMetadata.split("\r\n"));
			getVersionedTests(activityList, questionList, versionedTestNames,
					testTitile, possibleVersions);
		} catch (NotFoundException e) { // NOSONAR
			// Ignoring not found exception as there my be no versions for given
			// test.if there is no versions existing version count will be zero
		} catch (Exception e) {
			throw new InternalException("", e);
		}
	}

	/**
	 * 
	 * @param versionedTests
	 * @param questionList
	 */
	private void getVersionedTests(List<String> versionedTests,
			List<String> questionList, List<String> versionedTestNames,
			String testTitile, int possibleVersions) {
		List<String> activityList = new ArrayList<String>();

		for (String url : versionedTests) {
			activityList = Arrays.asList(url.split("/"));
			String versionedTestId=activityList.get(activityList.size() - 1);
			Metadata metadata = metadataService.getMetadata(versionedTestId);
			versionedTestMetadata.add(versionedTestId);
			if (metadata != null) {
				String versionedTestTitle = testTitile + "_v"
						+ metadata.getVersion();

				if (archiveService.getTestFolder(metadata.getGuid()) == null
						&& metadata.getTitle().equals(versionedTestTitle)) {
					versionedTestNames.add(metadata.getTitle());
					if (Integer.parseInt(metadata.getVersion()) < possibleVersions) {
						Test test = testService.getTestByID(activityList
								.get(activityList.size() - 1));
						if (test != null) {
							fillVersionedTestQuestionsList(test
									.getAssignmentContents().getBinding(),
									questionList, false);
						}
					}
				}
			}

		}
	}

	/**
	 * 
	 * @param bindingList
	 * @param questionList
	 */
	private void fillVersionedTestQuestionsList(
			List<AssignmentBinding> bindingList, List<String> questionList,
			boolean isParentTest) {
		StringBuilder questionString = new StringBuilder();
		String questionID;
		for (AssignmentBinding bindings : bindingList) {
			questionID = "";
			if (isParentTest) {
				questionID = bindings.getGuid();
			} else {
				Metadata metadata = metadataService.getMetadata(bindings
						.getGuid());
				if (metadata.getVersionOf() != null
						&& !metadata.getVersionOf().isEmpty()) {
					questionID = metadata.getVersionOf();
				} else {
					questionID = metadata.getGuid();
				}
			}

			if (questionString.toString().isEmpty()) {
				questionString.append(questionID);
			} else {
				questionString.append(",").append(questionID);
			}
		}
		questionList.add(questionString.toString());
	}

	/**
	 * This will initiate process of scrambling the test. Depending of the user
	 * selection on scrambling option, it will scramble only questions or
	 * answers choices or both.
	 * 
	 * @param scrambleType
	 * @param testEnvelop
	 * @param folder
	 * @return test envelop
	 */
	private List<AssignmentBinding> getScrambledTestEnvelop(
			TestScrambleType scrambleType, List<AssignmentBinding> bindings,
			UserFolder folder, int versionNumber, int existingNoOfVersions,
			int possibleVersions, List<String> questionList) {

		List<AssignmentBinding> bindingsToShuffle = cloneBindings(bindings);

		if (scrambleType == TestScrambleType.QUESTIONS
				|| scrambleType == TestScrambleType.BOTH) {
			shuffleQuestions(bindingsToShuffle, versionNumber,
					existingNoOfVersions, possibleVersions, questionList);
		}

		if (scrambleType == TestScrambleType.ANSWERS_KEY
				|| scrambleType == TestScrambleType.BOTH) {

			scrambleAnswerChoice(folder, bindingsToShuffle);
		}

		return bindingsToShuffle;
	}

	/**
	 * 
	 * @param bindingList
	 * @param versionNumber
	 * @param possibleVersions
	 * @param questionList
	 */
	private void shuffleQuestions(List<AssignmentBinding> bindingList,
			int versionNumber, int existingNoOfVersions, int possibleVersions,
			List<String> questionList) {
		boolean isVersionExists = false;
		Collections.shuffle(bindingList);

		if (versionNumber < possibleVersions) {
			StringBuilder questionString = new StringBuilder();
			for (AssignmentBinding bindings : bindingList) {
				if (questionString.toString().isEmpty()) {
					questionString.append(bindings.getGuid());
				} else {
					questionString.append(",").append(bindings.getGuid());
				}
			}
			if (!questionString.toString().isEmpty()) {
				for (String vquestions : questionList) {
					if (vquestions.equals(questionString.toString())) {
						isVersionExists = true;
						break;
					}
				}
				if (isVersionExists) {
					shuffleQuestions(bindingList, versionNumber,
							existingNoOfVersions, possibleVersions,
							questionList);
				} else {
					questionList.add(questionString.toString());
				}
			}

		}

	}

	/**
	 * This will extracts the QTI XML which is in the test and send it to
	 * QTIParse for answer scrambling.
	 * 
	 * @param folder
	 * @param bindings
	 */
	private void scrambleAnswerChoice(UserFolder folder,
			List<AssignmentBinding> bindings) {
		for (AssignmentBinding assignmentBinding : bindings) {
			String qtiXML = null;
			Metadata metadata = metadataService.getMetadata(assignmentBinding.getGuid());
			if(metadata.getQuizType().equalsIgnoreCase(QuestionTypes.MATCHING.toString()) 
					|| metadata.getQuizType().equalsIgnoreCase(QuestionTypes.MULTIPLECHOICE.toString())
					|| metadata.getQuizType().equalsIgnoreCase(QuestionTypes.MULTIPLERESPONSE.toString())
					|| metadata.getQuizType().equalsIgnoreCase(QuestionTypes.TRUEFALSE.toString())){
				qtiXML = questionService.getQuestionXmlById(assignmentBinding.getGuid());
				qtiParser.setXMLDocument(qtiXML);
				qtiXML = qtiParser.shuffleAnswerChoices();
				saveQuestion(folder, assignmentBinding, metadata , qtiXML);	
			}
		}
	}

	/**
	 * This will save the scrambled question as new question
	 * 
	 * @param folder
	 * @param assignmentBinding
	 * @param qtiXML
	 */
	private void saveQuestion(UserFolder folder, AssignmentBinding assignmentBinding, Metadata metadata, String qtiXML) {
		QuestionEnvelop questionEnvelop = getQuestionEnvelop(metadata,qtiXML);
		String response = questionService.saveQuestion(questionEnvelop,
				folder.getUserID(), folder.getGuid());
		QuestionOutput questionOutput = getQuestionResultBean(response);
		assignmentBinding.setGuid(questionOutput.getGuid());
	}

	/**
	 * Creates question envelop using question meta data body i.e XML Extracts
	 * the question meta data of original question and removes GUID and updates
	 * created and modified date.QTI XML will be added to body.
	 * 
	 * @param assignmentBinding
	 * @param qtiXML
	 * @return
	 */
	private QuestionEnvelop getQuestionEnvelop(Metadata metadata, String qtiXML) {
		QuestionEnvelop questionEnvelop = new QuestionEnvelop();
		metadata.setCreated(getUTCTime());
		metadata.setModified(getUTCTime());
		metadata.setVersionOf(metadata.getGuid());
		metadata.setGuid(null);
		questionEnvelop.setmetadata(metadata);
		questionEnvelop.setBody(qtiXML);
		return questionEnvelop;
	}

	private String getUTCTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
		return sdf.format(new Date());
	}

	/**
	 * Converts the JSON string which we got as a result of saving the question
	 * to object.
	 * 
	 * @param result
	 * @return
	 */
	public static QuestionOutput getQuestionResultBean(String result) {
		List<QuestionOutput> questionOutput = null;
		int firstQuestion = 0;
		Gson gson = new Gson();
		if (result != null && !result.isEmpty()) {
			// Get the type of test result bean list
			Type type = new TypeToken<List<QuestionOutput>>() {
				private static final long serialVersionUID = 1L;
			}.getType();
			questionOutput = gson.fromJson(result, type);

		} else {
			throw new BadDataException("Result not found ");
		}
		return questionOutput.get(firstQuestion);
	}

	/**
	 * Gets the test envelop
	 * 
	 * @param testId
	 *            the id of source version test
	 * @return the test envelop
	 * @throws InternalException
	 * @throws NotFoundException
	 */
	private TestEnvelop getTestEnvelop(String testId) {
		TestEnvelop testEnvelop = new TestEnvelop();
		Test test = testService.getTestByID(testId);
		Metadata metadata = metadataService.getMetadata(testId);
		metadata.setGuid(null);
		testEnvelop.setBody(test);
		testEnvelop.setmetadata(metadata);
		testEnvelop.getBody().setGuid(null);
		return testEnvelop;

	}

	/**
	 * Updates the sequence extended metadata
	 * 
	 * @param metadata
	 *            the metadata of a give activity
	 * @param sequence
	 *            the new sequence which needs to be updated
	 */
	private void updateSequenceExtMetadata(Metadata metadata, double sequence) {

		ExtMetadata extMetadata = metadata
				.getExtendedMetadataByName(Common.EXTENDED_METADATA_SEQUENCE);
		if (extMetadata != null) {
			extMetadata.setValue(Double.toString(sequence));
		} else {
			extMetadata = new ExtMetadata();
			extMetadata.setName(Common.EXTENDED_METADATA_SEQUENCE);
			extMetadata.setValue(Double.toString(sequence));
			metadata.addExtendedMetadata(extMetadata);

		}
	}

	private int getFactorial(int versions) {
		int result = 1;
		for (int i = 1; i <= versions; i++) {
			result = result * i;
		}
		return result;
	}

	private List<AssignmentBinding> cloneBindings(
			List<AssignmentBinding> originalBindings) {
		List<AssignmentBinding> copiedBindings = new ArrayList<AssignmentBinding>();
		for (AssignmentBinding bindings : originalBindings) {

			String credential = (new Gson()).toJson(bindings);
			copiedBindings.add((new Gson()).fromJson(credential,
					AssignmentBinding.class));
		}

		return copiedBindings;
	}

}
