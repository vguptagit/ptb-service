package com.pearson.ptb.proxy.repo;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.pearson.ptb.bean.PrintSettings;
import com.pearson.ptb.bean.UserSettings;
import com.pearson.ptb.dataaccess.GenericMongoRepository;
import com.pearson.ptb.framework.CacheWrapper;
import com.pearson.ptb.framework.exception.BadDataException;
import com.pearson.ptb.framework.exception.NotFoundException;
import com.pearson.ptb.proxy.BookDelegate;
import com.pearson.ptb.proxy.UserSettingsDelegate;
import com.pearson.ptb.util.UserHelper;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation class which got implemented from interface UserPrefDelegate
 * 
 * @see com.pearson.ptb.proxy.UserSettingsDelegate
 *
 */
@Repository("usersettings")

public class UserSettingsRepo implements UserSettingsDelegate {

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

		UserSettings userSettings = null;

		userSettings = CACHE.get(userid);
		if (userSettings == null) {
			userSettings = genericMongoRepository.findById(userid);
			if (userSettings == null) {

				
				userSettings = UserHelper.getDefaultSettings(userid);
				genericMongoRepository.save(userSettings);
			}
			CACHE.set(userid, userSettings);
		}

		return userSettings;
	}

	@Override
	public void saveUserSettings(UserSettings userSettings) {
		genericMongoRepository.save(userSettings);
		CACHE.set(userSettings.getUserid(), userSettings);
	}

	@Override
	public List<String> getUserDisciplines(String userid) {
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
			throw new BadDataException(
					"Invalid book ids: " + invalidBooks.toString());
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
		
		UserSettings userSettings = genericMongoRepository.findById(userid);
		List<String> userDiscplines = userSettings.getDisciplines();
		List<String> updateddiscipline = disciplines.stream().filter(discipline -> !userDiscplines.contains(discipline)).collect(Collectors.toList());
		userDiscplines.addAll(updateddiscipline);
		userSettings.setDisciplines(userDiscplines);
		  genericMongoRepository.save(userSettings);
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
