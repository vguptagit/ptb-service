/**
 * 
 */
package com.pearson.ptb.proxy;

import com.pearson.ptb.bean.Test;
import com.pearson.ptb.framework.exception.InternalException;
import com.pearson.ptb.framework.exception.NotFoundException;

/**
 * This interface defines the contract for accessing and saving the Test
 *
 */
public interface TestDelegate {

	/**
	 * Get the test from the repository which is having the ID number
	 * <code>testID</code>
	 * 
	 * @param testID
	 *            ID number of the test
	 * @return Test having the id number testID
	 * @throws InternalException
	 *             The application custom exceptions
	 * @throws NotFoundException
	 *             The test not found custom exceptions
	 */
	Test getTestByID(String testID) ;
	
}
