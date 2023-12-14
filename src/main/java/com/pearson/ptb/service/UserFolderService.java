package com.pearson.ptb.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.pearson.ptb.bean.QuestionBinding;
import com.pearson.ptb.bean.UserFolder;
import com.pearson.ptb.bean.UserQuestionsFolder;
import com.pearson.ptb.framework.CacheWrapper;
import com.pearson.ptb.framework.exception.BadDataException;
import com.pearson.ptb.framework.exception.DuplicateTitleException;
import com.pearson.ptb.framework.exception.NotFoundException;
import com.pearson.ptb.proxy.UserFoldersDelegate;
import com.pearson.ptb.util.CacheKey;

import org.springframework.stereotype.Service;

/**
 * This <code>UserFolderService</code> is responsible to create the folders and get the folders of tests and questions.
 */
@Service("userFolderService")
public class UserFolderService {

	/**
	 * @Qualifier annotation searched for the value userFolders in
	 *            appServlet-servlet.xml file created instance
	 */
	@Autowired
	@Qualifier("userFoldersRepo")
	private UserFoldersDelegate userFoldersRepo;	

	/**
	 * This method is responsible to get the user folders
	 * 
	 * @param userId
	 *            The instructor user id to get the folder
	 * @param parentFolderId
	 *            The parent folder id to get its children folders
	 * @return The list of user folders
	 */
	public List<UserFolder> getFolders(String userId, String parentFolderId){
		
		return userFoldersRepo.getChildFolders(userId, parentFolderId);
	}
	
	/**
	 * To get the My Test Root folder used to store root level test bindings
	 * 
	 * @param userId
	 *            The instructor user id to get the folder
	 * @return User Folder
	 */
	public UserFolder getMyTestRoot(String userID){
		
		return userFoldersRepo.getMyTestRoot(userID);
	}
	
	/**
	 * To get the Question Folders Root folder 
	 * 
	 * @param userId
	 *            The instructor user id to get the folder
	 * @return User Folder
	 */
	public UserQuestionsFolder getQuestionFoldersRoot(String userID){
		
		return userFoldersRepo.getQuestionFoldersRoot(userID);
	}
	
	/**
	 * To get User Questions Root folder used to store user created question bindings
	 * 
	 * @param userId
	 *            The instructor user id to get the folder
	 * @return User Questions Folder
	 */
	public UserQuestionsFolder getMyQuestionsFolder(String userID){
		
		return userFoldersRepo.getMyQuestionRoot(userID);
	}
	
	/**
	 * This method is responsible to get the folder of a user created questions
	 * @param folderId
	 * 				 ,folder id
	 * @param userID
	 * 				, user id
	 */
	public UserQuestionsFolder getMyQuestionsFolder(String userID, String folderId){
		
		return userFoldersRepo.getMyQuestionsFolder(userID, folderId);
	}
	
	public List<UserQuestionsFolder> getMyQuestionsFolders(String userID){
		
		return userFoldersRepo.getMyQuestionsFolders(userID);
	}
	
    public List<UserQuestionsFolder> getChildQuestionFolders(String folderId){
        
        return userFoldersRepo.getChildQuestionFolders(folderId);
    }
	
	/**
	 * To get the containing folder for test
	 * 
	 * @param testId
	 *            The PAF guid of the test stored in test bindings property of any user folder
	 * @return User Folder
	 */
	public UserFolder getTestFolder(String testId) {

		return userFoldersRepo.getTestFolder(testId);		 
	}	
	
	public double getUserFolderMinSeq(String userId) {

		return userFoldersRepo.getUserFolderMinSeq(userId);		 
	}
	
	public double getUserQuestionsFolderMinSeq(String userId) {

		return userFoldersRepo.getUserQuestionsFolderMinSeq(userId);		 
	}

	/**
	 * This method is responsible to get the user folder
	 * 
	 * @param userId
	 *            The instructor user id to get the folder
	 * @param folderId
	 *            The folder id to get its details
	 * @return The folder details
	 * @throws NotFoundException
	 */
	public UserFolder getFolder(String userId, String folderId) {
		
		UserFolder userFolder;		
		
        if(folderId == null || "null".equals(folderId) ) {
        	
        	userFolder = getMyTestRoot(userId);
		} else {
			
			userFolder = userFoldersRepo.getFolder(folderId);
		}        
		
		return userFolder; 
	}
	
	
	/**
	 * This method is responsible to update the folder on saving the test.
	 * @param folder
	 * 				, folder in which test is getting saved.
	 */
	public UserFolder updateFolder(UserFolder folder){
		List<UserFolder> folders = new ArrayList<UserFolder>();
		folders.add(folder);
		userFoldersRepo.saveFolders(folders);	
		return folder;
	}
	
	/**
	 * This method is responsible to save the folder
	 */
	public UserFolder saveFolder(UserFolder folder, String userId){
		
		// Check for userId and its value should not be null or empty
		if (userId == null) {
			throw new BadDataException("userId should not be null or empty");
		}
		if (folder.getGuid() == null) {
			folder.setGuid(UUID.randomUUID().toString());
		}
		folder.setUserID(userId);
		
		//Validate the user folder values
		folder.validateState();
		
		List<UserFolder> folders = new ArrayList<UserFolder>();
		if(!checkForUserFolderDuplicate(folder, userId)){
			folders.add(folder);
		}
		userFoldersRepo.saveFolders(folders);		
		
		return folder;
	}
	
	/**
	 * This method is responsible to update the folder
	 */
	public UserFolder updateFolder(UserFolder folder, String userId){
		
		// Check for userId and its value should not be null or empty
		if (userId == null) {
			throw new BadDataException("userId should not be null or empty");
		}
		if (folder.getGuid() == null) {
			throw new BadDataException("folderId should not be null or empty");
		}
		folder.setUserID(userId);
		
		//Validate the user folder values
		folder.validateState();
		
		List<UserFolder> folders = new ArrayList<UserFolder>();

		if(!checkForUserFolderDuplicate(folder, userId)){
			folders.add(folder);
		}
	
		userFoldersRepo.saveFolders(folders);		
		
		return folder;
	}
	
	
	/**
	 * This method will check for duplicate user folders. If duplicate found it
	 * it throws DuplicateTitleException else it returns false.
	 */
	private boolean checkForUserFolderDuplicate(UserFolder folder, String userId){
		List<UserFolder> folders = new ArrayList<UserFolder>();
		if(folder.getParentId() == null){
			folders = userFoldersRepo.getChildFolders(userId, null);
		}else{
			if(folder.getParentId() != null){
				folders = userFoldersRepo.getChildFolders(folder.getParentId());
			}
		}

		for(UserFolder userFolder : folders){
			if(!userFolder.getGuid().equals(folder.getGuid()) && userFolder.getTitle().equals(folder.getTitle())){
				throw new DuplicateTitleException("A folder with "+ folder.getTitle()+ " title already exists at this level");
			}
		}
		return false;
	}
	
	/**
	 * This method will check for duplicate UserQuestions folders. If duplicate found it
	 * it throws DuplicateTitleException else it returns false.
	 */
	private boolean checkForUserQuestionsFolderDuplicate(UserQuestionsFolder folder, String userId){
		List<UserQuestionsFolder> folders = new ArrayList<UserQuestionsFolder>();
		UserQuestionsFolder rootFolder = userFoldersRepo.getMyQuestionRoot(userId);
		if(folder.getParentId().equals(rootFolder.getGuid())){
			folders = userFoldersRepo.getMyQuestionsFolders(userId);
		}else{

			if(!folder.getParentId().equals(rootFolder.getGuid())){
				folders = userFoldersRepo.getChildQuestionFolders(folder.getParentId());
			}
		}
		for(UserQuestionsFolder userFolder : folders){
			if(!userFolder.getGuid().equals(folder.getGuid()) && userFolder.getTitle().equals(folder.getTitle())){
				throw new DuplicateTitleException("A folder with "+ folder.getTitle()+ " title already exists at this level");
			}
		}
		return false;
	}
	
	public UserQuestionsFolder saveUserQuestionFolder(UserQuestionsFolder folder, String userId){
		
		// Check for userId and its value should not be null or empty
		if (userId == null) {
			throw new BadDataException("userId should not be null or empty");
		}
		if (folder.getGuid() == null) {
			folder.setGuid(UUID.randomUUID().toString());
			folder.setQuestionBindings(new ArrayList<QuestionBinding>());
		}
		folder.setUserID(userId);		
		
		List<UserQuestionsFolder> folders = new ArrayList<UserQuestionsFolder>();
		if(!checkForUserQuestionsFolderDuplicate(folder, userId)){
			folders.add(folder);
		}
        String userQuestionsCacheKey = String.format(CacheKey.USER_QUESTIONS_FORMAT, folder.getGuid());    
        CacheWrapper.getInstance().delete(userQuestionsCacheKey);
        
		userFoldersRepo.saveUserQuestionFolders(folders);		
		
		return folder;
	}
	
	public UserQuestionsFolder updateUserQuestionFolder(UserQuestionsFolder folder, String userId){
		
		// Check for userId and its value should not be null or empty
		if (userId == null) {
			throw new BadDataException("userId should not be null or empty");
		}
		if (folder.getGuid() == null) {
			throw new BadDataException("folderId should not be null or empty");
		}
		folder.setUserID(userId);		
		
		List<UserQuestionsFolder> folders = new ArrayList<UserQuestionsFolder>();

		if(!checkForUserQuestionsFolderDuplicate(folder, userId)){
			folders.add(folder);
		}
		
        String userQuestionsCacheKey = String.format(CacheKey.USER_QUESTIONS_FORMAT, folder.getGuid());    
        CacheWrapper.getInstance().delete(userQuestionsCacheKey);
        
		userFoldersRepo.saveUserQuestionFolders(folders);		
		
		return folder;
	}
	
	/**
	 * This method is responsible to get the child folders of a folder
	 * @param parentFolderId
	 * 						, parent folder id
	 */
	public List<UserFolder> getFolders(String parentFolderId){

		List<UserFolder> folders;

		folders = userFoldersRepo.getChildFolders(parentFolderId);

		return folders;
	}
	
	/**
	 * Get the UserQuestions folder by title, parentid and userid
	 * @param title
	 * @param parentId
	 * @param userid
	 * @return
	 */
	public UserQuestionsFolder getFolderByTitle(String title,String parentId,String userid){
		return userFoldersRepo.getQuestionsFolderByTitle(title,parentId,userid);
	}
	
	/**
	 * Delete the UserQuestions folder by id
	 * @param id
	 */
	public void deleteQuestionFolder(String id){
		userFoldersRepo.deleteFolder(id);
	}
}
