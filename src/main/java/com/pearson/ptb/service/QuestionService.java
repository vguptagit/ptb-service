package com.pearson.ptb.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.pearson.ptb.bean.Container;
import com.pearson.ptb.bean.QuestionBinding;
import com.pearson.ptb.bean.QuestionEnvelop;
import com.pearson.ptb.bean.QuestionMetadata;
import com.pearson.ptb.bean.QuestionOutput;
import com.pearson.ptb.bean.UserQuestionsFolder;
import com.pearson.ptb.framework.CacheWrapper;
import com.pearson.ptb.framework.ConfigurationManager;
import com.pearson.ptb.framework.exception.ConfigException;
import com.pearson.ptb.framework.exception.InternalException;
import com.pearson.ptb.framework.exception.NotFoundException;
import com.pearson.ptb.proxy.ContainerDelegate;
import com.pearson.ptb.proxy.QuestionDelegate;
import com.pearson.ptb.util.CacheKey;
import com.pearson.ptb.util.SearchHelper;

import jakarta.annotation.PostConstruct;

/**
 * This <code>QuestionService</code> is responsible to fetch the questions for
 * book, container from PAf and save the user created questions to appropriate
 * folder.
 */
@Service("questionService")
public class QuestionService {

	@Autowired
	@Qualifier("questions")
	private QuestionDelegate questionRepo;

	@Autowired
	@Qualifier("containerService")
	private ContainerService containerService;

	@Autowired
	@Qualifier("userFolderService")
	private UserFolderService userFolderService;

	@Autowired
	@Qualifier("metadataService")
	private MetadataService metadataService;

	@Autowired
	@Qualifier("container")
	private ContainerDelegate containerRepo;

	private static CacheWrapper CACHE;
	private Integer cacheExpiryTimeForBookQuestions;
	private Integer cacheExpiryTimeForBookContainerQuestions;
	private Integer cacheExpiryTimeForQuestionXML;

	/**
	 * This constructor initializes the instance of the cache wrapper object for
	 * caching operation.
	 * 
	 */
	public QuestionService() {
		/*
		 * CACHE = CacheWrapper.getInstance();
		 * cacheExpiryTimeForBookQuestions=ConfigurationManager.getInstance().
		 * getCacheExpiryInSecondsForBookQuestions();
		 * cacheExpiryTimeForBookContainerQuestions=ConfigurationManager.
		 * getInstance(). getCacheExpiryInSecondsForBookContainerQuestions();
		 * cacheExpiryTimeForQuestionXML=ConfigurationManager.getInstance().
		 * getCacheExpiryInSecondsForQuestionXML();
		 */}
	public void initializeCache() {
		CACHE = CacheWrapper.getInstance();
		cacheExpiryTimeForBookQuestions=ConfigurationManager.getInstance().getCacheExpiryInSecondsForBookQuestions();
		cacheExpiryTimeForBookContainerQuestions=ConfigurationManager.getInstance().getCacheExpiryInSecondsForBookContainerQuestions();
		cacheExpiryTimeForQuestionXML=ConfigurationManager.getInstance().getCacheExpiryInSecondsForQuestionXML();

	}
	
	/*
	 * @PostConstruct private void initializeCache() { CACHE =
	 * CacheWrapper.getInstance(); cacheExpiryTimeForBookQuestions =
	 * ConfigurationManager.getInstance()
	 * .getCacheExpiryInSecondsForBookQuestions();
	 * cacheExpiryTimeForBookContainerQuestions = ConfigurationManager
	 * .getInstance() .getCacheExpiryInSecondsForBookContainerQuestions();
	 * cacheExpiryTimeForQuestionXML = ConfigurationManager.getInstance()
	 * .getCacheExpiryInSecondsForQuestionXML(); }
	 */
	/**
	 * Router method that will call the method getAllQuestions and getQuestions
	 * depending on the input includeInnerContainer
	 * 
	 * @param bookId
	 *            , Book id
	 * @param containerId
	 *            , Chapter or Topic id
	 * @param filterCriteria
	 *            , Metadata which are get as querystring
	 * @param includeInnerContainer
	 *            , whether to consider questions inside subfolder
	 * @return
	 * @throws InternalException
	 */
	public List<QuestionMetadata> getQuestions(String bookId,
			String containerId, Map<String, String> filterCriteria,
			boolean flat) {
		if (flat) {
			return getQuestionsFlatView(bookId, containerId, filterCriteria);
		} else {
			Map<String, String> containerParentChild = new HashMap<String, String>();
			return getThreadedViewQuestions(bookId, containerId, filterCriteria,
					containerParentChild);
		}
	}

	/**
	 * This method will get all questions of quiz types passed for a given
	 * course.
	 * 
	 * @param bookId
	 * @param quizTypes
	 * @return list of question meta data
	 */
	public List<QuestionMetadata> getQuestions(String bookId) {
		List<QuestionMetadata> questions;
		String questionsCacheKey = String.format(
				CacheKey.BOOKID_QUESTIONS_FORMAT, bookId,
				cacheExpiryTimeForBookQuestions);
		questions = CACHE.get(questionsCacheKey);
		if (questions == null) {
			questions = questionRepo.getQuestions(bookId);
			CACHE.set(questionsCacheKey,
					(ArrayList<QuestionMetadata>) questions,
					cacheExpiryTimeForBookQuestions);
		}
		return questions;
	}

	/**
	 * Returns list of all Questions for given Book and Container
	 * 
	 * @param bookId
	 * @param containerId
	 * @param filterCriteria
	 * @return
	 * @throws InternalException
	 */
	private List<QuestionMetadata> getThreadedViewQuestions(String bookId,
			String containerId, Map<String, String> filterCriteria,
			Map<String, String> containerParentChild) {
		String questionsCacheKey = String.format(
				CacheKey.PUBLISHED_QUESTIONS_FORMAT, bookId, containerId);
		List<QuestionMetadata> questions = CACHE.get(questionsCacheKey,
				cacheExpiryTimeForBookContainerQuestions);
		if (questions == null || !filterCriteria.isEmpty()) {
			questions = getContainerQuestions(bookId, containerId,
					filterCriteria, containerParentChild);
			if (filterCriteria.isEmpty()) {
				CACHE.set(questionsCacheKey,
						(ArrayList<QuestionMetadata>) questions,
						cacheExpiryTimeForBookContainerQuestions);
			}
		}
		getQuestionsHierarchy(questions, containerId, containerParentChild);
		return questions;
	}

	/**
	 * This will gets the hierarchy for questions which belongs to given
	 * container
	 * 
	 * @param questions
	 * @param containerId
	 * @param parentChild
	 */
	private void getQuestionsHierarchy(List<QuestionMetadata> questions,
			String containerId, Map<String, String> parentChild) {
		List<String> hirarchy = new ArrayList<String>();
		hirarchy.add(containerId);
		getContainerHierarchy(containerId, parentChild, hirarchy);

		for (QuestionMetadata bookQuestion : questions) {
			bookQuestion.setQuestionHierarchy(hirarchy);
		}
	}

	/**
	 * This method will get all questions of the given book from cache/PAF Then
	 * it will gets the question bindings for the given container and filters
	 * the questions which belongs to the given container.
	 * 
	 * @param bookId
	 * @param containerId
	 * @return list of question meta data
	 */
	private List<QuestionMetadata> getContainerQuestions(String bookId,
			String containerId, Map<String, String> filterCriteria,
			Map<String, String> containerParentChild) {
		List<QuestionMetadata> containerQuestions = new ArrayList<QuestionMetadata>();
		List<QuestionMetadata> bookQuestions = this.getQuestions(bookId);
		List<String> questionBindings = containerRepo
				.getQuestionBindings(bookId, containerId);
		for (QuestionMetadata bookQuestion : bookQuestions) {
			if (questionBindings.contains(bookQuestion.getGuid())) {
				SearchHelper.filterQuestionBySearchCriteria(bookQuestion,
						filterCriteria, containerQuestions);
			}
		}
		return containerQuestions;
	}

	/*
	 * To get the parent of the container recursively
	 */
	private void getContainerHierarchy(String containerId,
			Map<String, String> containerParentChild, List<String> hierarchy) {
		for (Map.Entry<String, String> entry : containerParentChild
				.entrySet()) {
			if (entry.getValue() == containerId) {
				hierarchy.add(entry.getKey());
				getContainerHierarchy(entry.getKey(), containerParentChild,
						hierarchy);
				break;
			}
		}
	}

	/**
	 * Returns list of all Questions for given Book and Container including the
	 * questions inside the inner container
	 * 
	 * @param bookId
	 * @param containerId
	 * @param filterCriteria
	 * @return
	 * @throws InternalException
	 */
	private List<QuestionMetadata> getQuestionsFlatView(String bookId,
			String containerId, Map<String, String> filterCriteria) {

		// getting the container list
		List<QuestionMetadata> questions = new ArrayList<QuestionMetadata>();
		Map<String, String> containerParentChild = new HashMap<String, String>();
		fillQuestions(questions, bookId, containerId, filterCriteria,
				containerParentChild);

		return questions;
	}

	/**
	 * Recursive method to fill the questions to given question list for each
	 * container. In each recursion, container list will get for each parent
	 * container. If there is no container, questions will be fetched for that
	 * container and fill it to the given question list.
	 * 
	 * @param questions,
	 *            list of question which need to fill
	 * @param bookId,
	 *            Book identification
	 * @param containerID,
	 *            Container identification
	 * @param filterCriteria
	 *            , key value pair of metadata and its value which is user's
	 *            input as query string to controller
	 * 
	 */
	private void fillQuestions(List<QuestionMetadata> questions, String bookId,
			String containerID, Map<String, String> filterCriteria,
			Map<String, String> containerParentChild) {

		List<Container> containers;

		containers = containerService.getContainerChildrenById(containerID);

		for (Container container : containers) {
			containerParentChild.put(containerID, container.getGuid());
			fillQuestions(questions, bookId, container.getGuid(),
					filterCriteria, containerParentChild);
		}

		questions.addAll(getThreadedViewQuestions(bookId, containerID,
				filterCriteria, containerParentChild));
	}

	/**
	 * This will help user to fetch the question for given test id
	 * 
	 * @param questionId
	 * @return
	 * @throws NotFoundException
	 * @throws InternalException
	 */
	public String getQuestionXmlById(String questionId) {
		String questionCacheKey = String.format(CacheKey.QUESTION_XML_FORMAT,
				questionId);
		String questionXML = CACHE.get(questionCacheKey,
				cacheExpiryTimeForQuestionXML);
		if (questionXML == null) {
			questionXML = questionRepo.getQuestionXmlById(questionId);
			CACHE.set(questionCacheKey, questionXML,
					cacheExpiryTimeForQuestionXML);
		}
		return questionXML;
	}

	/**
	 * This method will help to fetch the user creates questions for the given
	 * user id
	 * 
	 * @param folderId
	 *            ,represents folder of the test.
	 * @param userId
	 *            ,represents the user.
	 */
	public List<QuestionOutput> getUserQuestions(String userId, String folderId,
			boolean flat) {

		if (flat) {
			return getUserQuestionsFlat(userId, folderId);
		} else {
			return getUserQuestions(userId, folderId);
		}
	}

	private List<QuestionOutput> getUserQuestionsFlat(String userId,
			String folderId) {

		List<UserQuestionsFolder> folders = new ArrayList<UserQuestionsFolder>();
		UserQuestionsFolder myQuestionsFolder = userFolderService
				.getMyQuestionsFolder(userId, folderId);
		folders.add(myQuestionsFolder);
		fillUserQuestionFolders(folders, folderId);

		List<String> questionIds = new ArrayList<String>();

		for (UserQuestionsFolder folder : folders) {
			List<QuestionBinding> questionBindings = folder
					.getQuestionBindings();

			for (QuestionBinding questionBinding : questionBindings) {
				questionIds.add(questionBinding.getQuestionId());
			}
		}

		return getQuestions(questionIds);
	}

	private void fillUserQuestionFolders(List<UserQuestionsFolder> folders,
			String folderId) {

		List<UserQuestionsFolder> childFolders = userFolderService
				.getChildQuestionFolders(folderId);

		for (UserQuestionsFolder folder : childFolders) {
			folders.add(folder);
			fillUserQuestionFolders(folders, folder.getGuid());
		}
	}

	private List<QuestionOutput> getUserQuestions(String userId,
			String folderId) {
		UserQuestionsFolder myQuestionsFolder;
		if (folderId == null) {
			myQuestionsFolder = userFolderService.getMyQuestionsFolder(userId);
		} else {
			myQuestionsFolder = userFolderService.getMyQuestionsFolder(userId,
					folderId);
		}

		String userQuestionsCacheKey = String.format(
				CacheKey.USER_QUESTIONS_FORMAT, myQuestionsFolder.getGuid());

		List<QuestionOutput> questions = CACHE.get(userQuestionsCacheKey);

		if (questions == null) {

			List<QuestionBinding> questionBindings = myQuestionsFolder
					.getQuestionBindings();

			List<String> questionIds = new ArrayList<String>();

			for (QuestionBinding questionBinding : questionBindings) {
				questionIds.add(questionBinding.getQuestionId());
			}

			questions = getQuestions(questionIds);

			CACHE.set(userQuestionsCacheKey,
					(ArrayList<QuestionOutput>) questions);
		}

		return questions;
	}

	/**
	 * This method is used to get the user created questions count for the given
	 * user id
	 * 
	 * @param userId
	 *            ,represents the user.
	 * @return user created questions count.
	 */
	public int getUserQuestionsCount(String userId) {

		UserQuestionsFolder myQuestionsFolder = userFolderService
				.getMyQuestionsFolder(userId);
		List<QuestionBinding> questionBindings = myQuestionsFolder
				.getQuestionBindings();

		if (questionBindings.isEmpty()) {
			return userFolderService.getMyQuestionsFolders(userId).size();
		} else {
			return questionBindings.size();
		}
	}

	/**
	 * This method will get the book for the given bookid and container from the
	 * given bookid and send with input question envelop bean data to Question
	 * repo.
	 * 
	 * @param question
	 *            , QuestionEnvelop type
	 * @param bookID
	 *            , id of the book
	 * @param chapterID
	 *            , id of the container
	 * @return response message from the repository
	 * @throws NotFoundException
	 * @throws ConfigException
	 * @throws InternalException
	 */
	public String saveQuestion(QuestionEnvelop question, String bookID,
			String chapterID) {
		String questionResult;
		questionResult = questionRepo.saveQuestion(question, bookID, chapterID);

		//TODO - Enable caching 
		/*String metadataCacheKey = String.format(CacheKey.METADATA_FORMAT,
				question.getmetadata().getGuid());
		CACHE.set(metadataCacheKey, question.getmetadata());

		String questionCacheKey = String.format(CacheKey.QUESTION_XML_FORMAT,
				question.getmetadata().getGuid());
		CACHE.set(questionCacheKey, question.getBody(),
				cacheExpiryTimeForQuestionXML);*/

		return questionResult;
	}

	public List<String> saveQuestions(List<QuestionEnvelop> questions,
			String userId, UserQuestionsFolder myQuestionsFolder) {

		if (myQuestionsFolder == null) {
			myQuestionsFolder = userFolderService.getMyQuestionsFolder(userId);
		}

		List<String> questionResults = new ArrayList<String>();
		String questionResult;

		Boolean isAnyQuestionEdited = false;

		for (QuestionEnvelop question : questions) {

			if (question.getmetadata().getGuid() == null) {

				isAnyQuestionEdited = true;

				String guid = UUID.randomUUID().toString();
				question.getmetadata().setGuid(guid);
				question.setBody(question.getBody().replaceAll("QUESTION-X",
						"QUESTION-" + guid));

				questionResult = this.saveQuestion(question, userId,
						myQuestionsFolder.getGuid());

				updateUserQuestionsFolder(myQuestionsFolder, guid, userId);
			} else {
				questionResult = "[{\"guid\":\""
						+ question.getmetadata().getGuid() + "\"}]";
			}
			questionResults.add(questionResult);
		}

		if (isAnyQuestionEdited) {
			// TODO -- Enable Cache
			/*String userQuestionsCacheKey = String.format(
					CacheKey.USER_QUESTIONS_FORMAT,
					myQuestionsFolder.getGuid());
			CACHE.delete(userQuestionsCacheKey);*/
		}

		return questionResults;
	}

	private void updateUserQuestionsFolder(
			UserQuestionsFolder myQuestionsFolder, String guid, String userId) {
		double sequence = 1.0;
		List<QuestionBinding> questionBindings = myQuestionsFolder
				.getQuestionBindings();
		if (!questionBindings.isEmpty()) {
			sequence = questionBindings.get(questionBindings.size() - 1)
					.getSequence() + 1;
		}

		QuestionBinding questionBinding = new QuestionBinding();
		questionBinding.setQuestionId(guid);
		questionBinding.setSequence(sequence);
		questionBindings.add(questionBinding);
		myQuestionsFolder.setQuestionBindings(questionBindings);

		userFolderService.updateUserQuestionFolder(myQuestionsFolder, userId);
	}

	public List<QuestionOutput> getQuestions(List<String> questionIds) {
		List<QuestionOutput> questions = new ArrayList<QuestionOutput>();
		QuestionOutput questionOutput = null;
		for (String questionId : questionIds) {
			questionOutput = new QuestionOutput();
			questionOutput.setQtixml(getQuestionXmlById(questionId));
			questionOutput.setMetadata(metadataService.getMetadata(questionId));
			questionOutput.setGuid(questionId);
			questions.add(questionOutput);
		}
		return questions;
	}

}
