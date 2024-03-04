package com.pearson.ptb.proxy.repo;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.pearson.ptb.bean.PrintSettings;
import com.pearson.ptb.bean.UserSettings;
import com.pearson.ptb.dataaccess.GenericMongoRepository;
import com.pearson.ptb.framework.CacheWrapper;
import com.pearson.ptb.framework.exception.BadDataException;
import com.pearson.ptb.framework.exception.NotFoundException;
import com.pearson.ptb.proxy.BookDelegate;
import com.pearson.ptb.proxy.UserSettingsDelegate;
import com.pearson.ptb.util.UserHelper;

/**
 * Implementation class which got implemented from interface UserPrefDelegate
 * 
 * @see com.pearson.ptb.proxy.UserSettingsDelegate
 *
 */
@Repository("usersettings")

public class UserSettingsRepo implements UserSettingsDelegate {
	private static final Logger logger = LoggerFactory.getLogger(UserSettingsRepo.class);

	@Autowired
	@Qualifier("book")
	private BookDelegate bookDelegate;

	private static CacheWrapper CACHE;

	@Autowired
	private GenericMongoRepository<UserSettings, String> genericMongoRepository;

	public UserSettingsRepo() {
		CACHE = CacheWrapper.getInstance();
	}

	@Override
	public UserSettings getUserSettings(String userid) {
		logger.info("Getting user settings for user with ID: {}", userid);

		UserSettings userSettings = null;

		userSettings = CACHE.get(userid);
		if (userSettings == null) {
			logger.info("User settings not found in cache for user with ID: {}", userid);
			userSettings = genericMongoRepository.findById(userid);
			if (userSettings == null) {
				logger.info("User settings not found in MongoDB for user with ID: {}", userid);
				userSettings = UserHelper.getDefaultSettings(userid);
				logger.info("Default settings retrieved for user with ID: {}", userid);
				genericMongoRepository.save(userSettings);
				logger.info("Default settings saved to MongoDB for user with ID: {}", userid);
			}
			CACHE.set(userid, userSettings);
		}
		logger.info("returning the user from user settings");
		return userSettings;
	}

	@Override
	public void saveUserSettings(UserSettings userSettings) {
		genericMongoRepository.save(userSettings);
		CACHE.set(userSettings.getUserid(), userSettings);
	}

	@Override
	public List<String> getUserDisciplines(String userid) {
		logger.info("passing the userId  ID: {}"+userid);
		return getUserSettings(userid).getDisciplines();

	}

	@Override
	public List<String> getUserBooks(String userid) {
		UserSettings userSettings = getUserSettings(userid);
		return userSettings.getBooks();
	}

	private void validateBookIds(List<String> books) {
		List<String> invalidBooks = new ArrayList<String>();
		for (String bookid : books) {
			try {
				bookDelegate.getBookByID(bookid);
			} catch (NotFoundException ex) { // NOSONAR
				invalidBooks.add(bookid);
			}
		}
		if (!invalidBooks.isEmpty()) {
			throw new BadDataException("Invalid book ids: " + invalidBooks.toString());
		}
	}

	@Override
	public void saveUserBooks(String userid, List<String> books) {
		validateBookIds(books);
		UserSettings userSettings = getUserSettings(userid);
		userSettings.setBooks(books);
		saveUserSettings(userSettings);

	}

	@Override
	public void saveUserDisciplines(String userid, List<String> disciplines) {
		logger.info("user id" + userid);
		UserSettings userSettings = getUserSettings(userid);

		List<String> userDiscplines = userSettings.getDisciplines();
		List<String> updateddiscipline = disciplines.stream().filter(discipline -> !userDiscplines.contains(discipline))
				.collect(Collectors.toList());
		
		userDiscplines.addAll(updateddiscipline);
		userSettings.setDisciplines(userDiscplines);
		genericMongoRepository.save(userSettings);
		logger.info("Disciplines saved successfully for user with ID: {}", userid);

	}

	@Override
	public void savePrintSettings(String userid, PrintSettings printSettings) {
		printSettings.validate();

		if (!printSettings.isMultipleVersions()) {
			printSettings.setNumberOfVersions(1);
			printSettings.setScrambleOrder("Scramble question order");

		}
		UserSettings userSettings = getUserSettings(userid);
		userSettings.setPrintSettings(printSettings);

		saveUserSettings(userSettings);
	}

	@Override
	public PrintSettings getPrintSettings(String userid) {
		UserSettings userSettings = getUserSettings(userid);
		return userSettings.getPrintSettings();
	}

	@Override
	public List<String> getQuestionMetadata(String userid) {
		UserSettings userSettings = getUserSettings(userid);
		return userSettings.getQuestionMetadata();
	}

	@Override
	public void saveQuestionMetadata(String userid, List<String> questionMetadata) {
		UserSettings userSettings = getUserSettings(userid);
		userSettings.setQuestionMetadata(questionMetadata);

		saveUserSettings(userSettings);

	}

}
