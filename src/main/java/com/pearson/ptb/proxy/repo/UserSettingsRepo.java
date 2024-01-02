package com.pearson.ptb.proxy.repo;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.pearson.ptb.bean.PrintSettings;
import com.pearson.ptb.bean.UserSettings;
import com.pearson.ptb.framework.CacheWrapper;
import com.pearson.ptb.framework.exception.BadDataException;
import com.pearson.ptb.framework.exception.ConfigException;
import com.pearson.ptb.framework.exception.NotFoundException;
import com.pearson.ptb.framework.exception.ServiceUnavailableException;
import com.pearson.ptb.proxy.BookDelegate;
import com.pearson.ptb.proxy.UserSettingsDelegate;
import com.pearson.ptb.util.UserHelper;

import org.springframework.stereotype.Repository;

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

	@Override
	public UserSettings getUserSettings(String userid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveUserSettings(UserSettings userSettings) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<String> getUserDisciplines(String userid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getUserBooks(String userid) {
		UserSettings userSettings = getUserSettings(userid);
		return userSettings.getBooks();
	}

	@Override
	public void saveUserBooks(String userid, List<String> books) {
		// TODO Auto-generated method stub

	}

	@Override
	public void saveUserDisciplines(String userid, List<String> disciplines) {
		// TODO Auto-generated method stub

	}

	@Override
	public void savePrintSettings(String userid, PrintSettings printSettings) {
		// TODO Auto-generated method stub

	}

	@Override
	public PrintSettings getPrintSettings(String userid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getQuestionMetadata(String userid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveQuestionMetadata(String userid, List<String> disciplines) {
		// TODO Auto-generated method stub

	}

	/*
	 * private DataAccessHelper<UserSettings> accessor;
	 * 
	 * private static CacheWrapper CACHE;
	 *//**
		 * Constructor to instantiate the DataAccessHelper with BookPref bean.
		 * 
		 * @throws ConfigException
		 * @throws UnknownHostException
		 * @throws ServiceUnavailableException
		 *//*
			 * public UserSettingsRepo() { // accessor = new
			 * DataAccessHelper<UserSettings>(UserSettings.class); // CACHE =
			 * CacheWrapper.getInstance(); }
			 * 
			 * @Override public UserSettings getUserSettings(String userid){
			 * 
			 * UserSettings userSettings = null;
			 * 
			 * userSettings = CACHE.get(userid); if (userSettings == null) {
			 * userSettings = accessor.getById(userid); if (userSettings ==
			 * null) {
			 * 
			 * //user logging in for the first time and hence create the user
			 * with default settings userSettings =
			 * UserHelper.getDefaultSettings(userid);
			 * accessor.save(userSettings); } CACHE.set(userid, userSettings); }
			 * 
			 * return userSettings; }
			 * 
			 * @Override public void saveUserSettings(UserSettings
			 * userSettings){
			 * 
			 * accessor.save(userSettings);
			 * 
			 * CACHE.set(userSettings.getUserid(), userSettings); }
			 * 
			 * @Override public List<String> getUserDisciplines(String userid) {
			 * 
			 * return getUserSettings(userid).getDisciplines(); }
			 * 
			 * @Override public void saveUserDisciplines(String userid,
			 * List<String> disciplines){
			 * 
			 * UserSettings userSettings = getUserSettings(userid);
			 * userSettings.setDisciplines(disciplines);
			 * 
			 * saveUserSettings(userSettings); }
			 * 
			 * @Override public List<String> getUserBooks(String userid){
			 * 
			 * UserSettings userSettings = getUserSettings(userid); return
			 * userSettings.getBooks(); }
			 * 
			 * private void validateBookIds(List<String> books){ List<String>
			 * invalidBooks = new ArrayList<String>(); for (String bookid :
			 * books) { try { bookDelegate.getBookByID(bookid); }
			 * catch(NotFoundException ex) { // NOSONAR
			 * invalidBooks.add(bookid); } } if (!invalidBooks.isEmpty()) {
			 * throw new BadDataException("Invalid book ids: " +
			 * invalidBooks.toString()); } }
			 * 
			 * @Override public void saveUserBooks(String userid, List<String>
			 * books) {
			 * 
			 * validateBookIds(books);
			 * 
			 * UserSettings userSettings = getUserSettings(userid);
			 * userSettings.setBooks(books);
			 * 
			 * saveUserSettings(userSettings); }
			 * 
			 * @Override public List<String> getQuestionMetadata(String userid){
			 * 
			 * UserSettings userSettings = getUserSettings(userid); return
			 * userSettings.getQuestionMetadata();
			 * 
			 * }
			 * 
			 * @Override public void saveQuestionMetadata(String userid,
			 * List<String> questionMetadata){
			 * 
			 * UserSettings userSettings = getUserSettings(userid);
			 * userSettings.setQuestionMetadata(questionMetadata);
			 * 
			 * saveUserSettings(userSettings); }
			 * 
			 * @Override public PrintSettings getPrintSettings(String userid){
			 * 
			 * UserSettings userSettings = getUserSettings(userid); return
			 * userSettings.getPrintSettings();
			 * 
			 * }
			 * 
			 * @Override public void savePrintSettings(String userid,
			 * PrintSettings printSettings){
			 * 
			 * printSettings.validate();
			 * 
			 * if (!printSettings.isMultipleVersions()) {
			 * printSettings.setNumberOfVersions(1);
			 * printSettings.setScrambleOrder("Scramble question order"); }
			 * 
			 * UserSettings userSettings = getUserSettings(userid);
			 * userSettings.setPrintSettings(printSettings);
			 * 
			 * saveUserSettings(userSettings); }
			 */
}
