package com.pearson.ptb.proxy;

import java.util.List;

import com.pearson.ptb.bean.UserFolder;
import com.pearson.ptb.bean.UserQuestionsFolder;

/**
 * This interface defines the contract for accessing the user folders from
 * repository
 * 
 * @author nithinjain
 *
 */
public interface UserFoldersDelegate {

	/**
	 * This method is responsible for saving the user folder to repository
	 * 
	 * @param folders
	 *            The list of user folder to save in repository
	 */
	void saveFolders(List<UserFolder> folders);
	
	void updateFolder(UserFolder folder);

	/**
	 * This method is responsible for saving the User Question to repository
	 * 
	 * @param folders,
	 *            The list of user folder to save in repository
	 */
	void saveUserQuestionFolders(List<UserQuestionsFolder> folders);

	/**
	 * Get the Folder for the given folderId
	 * 
	 * @param folderId
	 * @return UserFolder
	 */
	UserFolder getFolder(String folderId);

	/**
	 * To delete the folders of given list of ids
	 * 
	 * @param ids
	 */
	void deleteFolders(List<String> ids);

	/**
	 * To save user created folder
	 * 
	 * @param folder
	 */
	void saveFolder(UserFolder folder);

	/**
	 * To delete single user created folder for given folder id
	 * 
	 * @param id
	 */
	void deleteFolder(String id);

	/**
	 * To get folder used for storing root level tests
	 * 
	 * @param userID
	 * @return UserFolder
	 */
	UserFolder getMyTestRoot(String userID);

	/**
	 * To get question folder for specific user
	 * 
	 * @param userID
	 * @return UserQuestionsFolder
	 */
	UserQuestionsFolder getQuestionFoldersRoot(String userID);

	/**
	 * To get Test Folder for given test Id
	 * 
	 * @param testId
	 * @return UserFolder
	 */
	UserFolder getTestFolder(String testId);

	/**
	 * To get Folder for given title
	 * 
	 * @param title
	 * @param parentId
	 * @param userId
	 * @return UserFolder
	 */
	UserFolder getFolderByTitle(String title, String parentId, String userId);

	/**
	 * To get Folder for given title
	 * 
	 * @param title
	 * @param parentId
	 * @param userId
	 * @return UserFolder
	 */
	UserQuestionsFolder getQuestionsFolderByTitle(String title, String parentId,
			String userId);

	/**
	 * To get My Questions Folder
	 * 
	 * @param userID
	 * @return UserQuestionsFolder
	 */
	UserQuestionsFolder getMyQuestionRoot(String userID);

	/**
	 * To get the Child Folders
	 * 
	 * @param userID
	 * @param parentFolderId
	 * @return list of UserFolder
	 */
	List<UserFolder> getChildFolders(String userID, String parentFolderId);

	/**
	 * To get the Child Folders
	 * 
	 * @param parentFolderId
	 * @return list of UserFolder
	 */
	List<UserFolder> getChildFolders(String parentFolderId);

	/**
	 * To get My Questions Folder
	 * 
	 * @param userID
	 * @param folderid
	 * @return UserQuestionsFolder
	 */
	UserQuestionsFolder getMyQuestionsFolder(String userID, String folderid);

	/**
	 * To get My Questions Folders
	 * 
	 * @param userID
	 * @return list of UserQuestionsFolder
	 */
	List<UserQuestionsFolder> getMyQuestionsFolders(String userID);

	/**
	 * To get My Questions Folders
	 * 
	 * @param userID
	 * @return list of UserQuestionsFolder
	 */
	List<UserQuestionsFolder> getChildQuestionFolders(String folderid);

	/**
	 * To get User Folder Min Sequence
	 * 
	 * @param userID
	 * @return Sequence value in double
	 */
	double getUserFolderMinSeq(String userID);

	/**
	 * To get User Questions Folder Min Sequence
	 * 
	 * @param userId
	 * @return Sequence value in double
	 */
	double getUserQuestionsFolderMinSeq(String userId);
}
