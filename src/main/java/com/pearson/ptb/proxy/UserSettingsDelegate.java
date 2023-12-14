/**
 * 
 */
package com.pearson.ptb.proxy;

import java.util.List;

import com.pearson.ptb.bean.PrintSettings;
import com.pearson.ptb.bean.UserSettings;
import com.pearson.ptb.framework.exception.BaseException;
import com.pearson.ptb.framework.exception.NotFoundException;
import com.pearson.ptb.framework.exception.ServiceUnavailableException;


/**
 * This interface defines the contract for accessing the books from repository
 *
 */
public interface UserSettingsDelegate {

	/**
	 * Get book preferences from the repository
	 * @throws ServiceUnavailableException 
	 * 
	 * @throws BaseException
	 *             The application custom exceptions
	 */
	UserSettings getUserSettings(String userid);

	/**
	 * Update book level preferences for the given book in the
	 * repository. Return <code>null</code> if there is no book exist.
	 * 
	 * @param bookPref
	 *            Book id property exists within this.
	 * @throws ServiceUnavailableException 
	 * @throws BaseException
	 *             The application custom exceptions
	 */
	void saveUserSettings(UserSettings userSettings);

	/**
	 * Get user selected disciplines 
	 * @return
	 * @throws NotFoundException 
	 * @throws ServiceUnavailableException 
	 */
	List<String> getUserDisciplines(String userid);

	/**
	 * Get user selected books 
	 * @return
	 * @throws NotFoundException 
	 * @throws ServiceUnavailableException 
	 */
	List<String> getUserBooks(String userid);
	
	/**
	 * To save user selected books 
	 * @param userid
	 * @param books, list of books
	 */
	void saveUserBooks(String userid, List<String> books);

	/**
	 * To save user selected Disciplines 
	 * @param userid
	 * @param disciplines, list of disciplines
	 */
	void saveUserDisciplines(String userid, List<String> disciplines);

	/**
	 * To save user specific Print Settings
	 * @param userid
	 * @param printSettings
	 */
	void savePrintSettings(String userid, PrintSettings printSettings) ;

	/**
	 * To get user specific Print Settings
	 * @param userid
	 * @return printSettings
	 */
	PrintSettings getPrintSettings(String userid) ;

	/**
	 * To get user selected Question Metadata
	 * @param userid
	 * @return QuestionMetadata
	 */
	List<String> getQuestionMetadata(String userid);

	/**
	 * To save user selected Question Metadata
	 * @param userid
	 * @param QuestionMetadata
	 */
	void saveQuestionMetadata(String userid, List<String> disciplines);
}
