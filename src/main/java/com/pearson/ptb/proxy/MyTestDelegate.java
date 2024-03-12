package com.pearson.ptb.proxy;

import com.pearson.ptb.bean.TestEnvelop;
import com.pearson.ptb.bean.TestResult;
import com.pearson.ptb.framework.exception.BadDataException;
import com.pearson.ptb.framework.exception.InternalException;

/**
 * This interface defines the contract for create, update and delete of tests
 * from PAF repository
 */
public interface MyTestDelegate {

	/**
	 * Create the new test data <code>test</code> under the user folder
	 * <code>userFolder</code> in repository
	 * 
	 * @param test
	 *            Paper test envelope with meta data and paper test
	 * @param folder
	 *            Target folder to save the test
	 * @return test created status
	 * @throws InternalException
	 *             Application custom exceptions
	 * @throws BadDataException
	 *             Application custom exceptions
	 */
	TestResult create(TestEnvelop test);

	/**
	 * Update the test data <code>test</code> under the user folder
	 * <code>userFolder</code> in repository
	 * 
	 * @param test
	 *            Paper test envelope with meta data and paper test
	 * @param folder
	 *            Target folder to save the test
	 * @return test updated status
	 * @throws InternalException
	 *             Application custom exceptions
	 * @throws BadDataException
	 *             Application custom exceptions
	 */
	TestResult update(TestEnvelop test);

	/**
	 * Delete the test data based on test id in repository
	 * 
	 * @param testId
	 */
	//void delete(String folderId ,String testId);
	
	public void deleteTest(String folderId, String testId);
	
    TestEnvelop getTest(String testId);
    
     public void deleteTestBindings(String testId);
     
     public void deleteLinkedDataRecursively(String id);
    

}
