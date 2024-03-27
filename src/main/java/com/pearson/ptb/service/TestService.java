package com.pearson.ptb.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.pearson.ptb.bean.AssignmentBinding;
import com.pearson.ptb.bean.Book;
import com.pearson.ptb.bean.QuestionOutput;
import com.pearson.ptb.bean.Test;
import com.pearson.ptb.bean.TestEnvelop;
import com.pearson.ptb.framework.CacheWrapper;
import com.pearson.ptb.framework.exception.InternalException;
import com.pearson.ptb.framework.exception.NotFoundException;
import com.pearson.ptb.proxy.BookDelegate;
import com.pearson.ptb.proxy.TestDelegate;
import com.pearson.ptb.proxy.repo.MyTestRepo;
import com.pearson.ptb.util.CacheKey;

/**
 * This <code>TestService</code> is responsible to get the tests, test questions
 * and published tests from PAF.
 */
@Service("testService")
public class TestService {

	@Autowired
	@Qualifier("tests")
	private TestDelegate testRepo;
	
	@Autowired
	@Qualifier("myTestsRepo")
	private MyTestRepo myTestRepo;

	@Autowired
	@Qualifier("book")
	private BookDelegate bookRepo;

	@Autowired
	@Qualifier("questionService")
	private QuestionService questionService;

	private static CacheWrapper CACHE;

	/**
	 * This constructor initializes the instance of the cache wrapper object for
	 * caching operations.
	 * 
	 */
	public TestService() {
		
	}
	
	public void initializeCache() {
		CACHE = CacheWrapper.getInstance();
	}

	/**
	 * This will fetch the details of a specific my test
	 * 
	 * @param testId
	 * @return
	 * @throws InternalException
	 * @throws NotFoundException
	 */
	public Test getTestByID(String testId) {
//		CACHE = CacheWrapper.getInstance();
//		String testCacheKey = String.format(CacheKey.TEST_FORMAT, testId);
		Test test = null;
//				CACHE.get(testCacheKey);
		if (test == null) {
			test = testRepo.getTestByID(testId);
//			CACHE.set(testCacheKey, test);
		}
		return test;
	}
	/**
	 * This method will fetch the published tests for the given book id
	 * 
	 * @param bookId
	 *            , Book id
	 * @return list of tests
	 */
	public List<Test> getPublisherTestsByBookId(String bookId) {

		List<Test> tests;
//		String bookTestsCacheKey = String.format(CacheKey.BOOK_TESTS_FORMAT,
//				bookId);
		tests = null;
				//CACHE.get(bookTestsCacheKey);

		if (tests == null) {

			tests = new ArrayList<Test>();
			Book book = bookRepo.getBookByID(bookId);

			for (String testId : book.getTestBindings()) {
				try {

					tests.add(getTestByID(testId));
				} catch (Exception e) { 
				}
			}

			//CACHE.set(bookTestsCacheKey, (ArrayList<Test>) tests);
		}

		return tests;
	}

	/**
	 * This method will fetch the questions for the given test id
	 * 
	 * @param testId
	 *            , Test id
	 * @return list of questions.
	 */
	public List<QuestionOutput> getTestQuestions(String testId) {

		/*
		 * String testQuestionsCacheKey = String .format(CacheKey.TEST_QUESTIONS_FORMAT,
		 * testId);
		 */

		List<QuestionOutput> questions = null;
		//List<QuestionOutput> questions = CACHE.get(testQuestionsCacheKey);

		if (questions == null) {

			List<String> questionIds = new ArrayList<String>();
			TestEnvelop testEnvelop = myTestRepo.getTest(testId);
			for (AssignmentBinding questionBinding : testEnvelop.getBody()
					.getAssignmentContents().getBinding()) {
				questionIds.add(questionBinding.getGuid());
			}

			questions = questionService.getQuestions(questionIds);

			/*
			 * CACHE.set(testQuestionsCacheKey, (ArrayList<QuestionOutput>) questions);
			 */
		}

		return questions;
	}

}
