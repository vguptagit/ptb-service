package com.pearson.ptb.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.pearson.ptb.bean.PrintSettings;
import com.pearson.ptb.bean.UserSettings;
import com.pearson.ptb.framework.exception.BadDataException;
import com.pearson.ptb.framework.exception.InternalException;
import com.pearson.ptb.framework.exception.NotFoundException;
import com.pearson.ptb.framework.exception.ServiceUnavailableException;
import com.pearson.ptb.proxy.UserSettingsDelegate;

import org.springframework.stereotype.Service;

/**
 * 
 * Serves user settings related data
 *
 */
@Service("userSettingService")
public class UserSettingsService {

	@Autowired
	@Qualifier("usersettings")
	private UserSettingsDelegate userSettingsDelegate;

	/**
	 * 
	 * @param userid
	 * @return
	 * @throws NotFoundException
	 * @throws ServiceUnavailableException
	 */
	public UserSettings getUserSettings(String userid) {
		return userSettingsDelegate.getUserSettings(userid);

	}

	/**
	 * 
	 * @param userSettings
	 * @throws InternalException
	 * @throws ValidationFailedException
	 * @throws ServiceUnavailableException
	 */
	public void saveUserSettings(UserSettings userSettings) {

		userSettingsDelegate.saveUserSettings(userSettings);
	}

	/**
	 * 
	 * @param userid
	 * @return
	 * @throws NotFoundException
	 * @throws ServiceUnavailableException
	 */
	public List<String> getUserDisciplines(String userid) {

		return userSettingsDelegate.getUserDisciplines(userid);
	}

	/**
	 * 
	 * @param userid
	 * @param disciplines
	 * @throws NotFoundException
	 * @throws ServiceUnavailableException
	 * @throws InternalException
	 * @throws ValidationFailedException
	 */
	public void saveUserDisciplines(String userid, List<String> disciplines) {
		userSettingsDelegate.saveUserDisciplines(userid, disciplines);
	}

	/**
	 * 
	 * @param userid
	 * @return
	 * @throws NotFoundException
	 * @throws ServiceUnavailableException
	 */
	public List<String> getUserBooks(String userid) {
		return userSettingsDelegate.getUserBooks(userid);
	}

	/**
	 * 
	 * @param userid
	 * @param books
	 * @throws InternalException
	 * @throws ValidationFailedException
	 * @throws NotFoundException
	 * @throws ServiceUnavailableException
	 * @throws BadDataException
	 */
	public void saveUserBooks(String userid, List<String> books) {
		userSettingsDelegate.saveUserBooks(userid, books);
	}

	/**
	 * 
	 * @param userid
	 * @return
	 * @throws NotFoundException
	 * @throws ServiceUnavailableException
	 */
	public List<String> getQuestionMetadata(String userid) {
		return userSettingsDelegate.getQuestionMetadata(userid);
	}

	/**
	 * 
	 * @param userid
	 * @param questionMetadata
	 * @throws NotFoundException
	 * @throws ServiceUnavailableException
	 * @throws InternalException
	 * @throws ValidationFailedException
	 */
	public void saveQuestionMetadata(String userid,
			List<String> questionMetadata) {
		userSettingsDelegate.saveQuestionMetadata(userid, questionMetadata);
	}

	/**
	 * 
	 * @param userid
	 * @return
	 * @throws NotFoundException
	 * @throws ServiceUnavailableException
	 */
	public PrintSettings getPrintSettings(String userid) {
		return userSettingsDelegate.getPrintSettings(userid);
	}

	/**
	 * 
	 * @param userid
	 * @param printSettings
	 * @throws NotFoundException
	 * @throws ServiceUnavailableException
	 * @throws InternalException
	 * @throws ValidationFailedException
	 */
	public void savePrintSettings(String userid, PrintSettings printSettings) {
		userSettingsDelegate.savePrintSettings(userid, printSettings);
	}

}
