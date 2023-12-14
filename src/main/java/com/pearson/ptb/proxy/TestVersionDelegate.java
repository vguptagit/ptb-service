package com.pearson.ptb.proxy;

import com.pearson.ptb.bean.TestEnvelop;
import com.pearson.ptb.bean.TestResult;
import com.pearson.ptb.framework.exception.BadDataException;


/**
 * This interface defines the contract for accessing and saving the Test
 *
 */
public interface TestVersionDelegate {

	/**
	 * Creates the test version from a give test
	 * 
	 * @param test the MyTest envelop
	 * @param folder the folder to save the newly creating version test
	 * @return returns the newly created version test result
	 * @throws BadDataException
	 *             The application custom exceptions
	 */
	TestResult createVersionTest(TestEnvelop test, 
			String testId);
	
	/**
	 * This gets the test versions versions created  from the given test 
	 * @param testID
	 * @return the string which contains the test URL 
	 */
	String getTestVersions(String testID) ;

}
