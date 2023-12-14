package com.pearson.ptb.proxy.repo;

import java.net.UnknownHostException;
import java.util.List;
import java.util.UUID;

//import org.mongodb.morphia.query.Query;
import org.springframework.stereotype.Repository;

import com.pearson.ptb.bean.UserFolder;
import com.pearson.ptb.bean.UserQuestionsFolder;
import com.pearson.ptb.framework.exception.ConfigException;
import com.pearson.ptb.proxy.UserFoldersDelegate;

/**
 * Implementation class which got implemented from interface UserFoldersDelegate
 * 
 * @see com.pearson.ptb.proxy.UserFoldersDelegate
 *
 */
@Repository("userFoldersRepo")
public class UserFoldersRepo implements UserFoldersDelegate {

	@Override
	public void saveFolders(List<UserFolder> folders) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void saveUserQuestionFolders(List<UserQuestionsFolder> folders) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public UserFolder getFolder(String folderId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteFolders(List<String> ids) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void saveFolder(UserFolder folder) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteFolder(String id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public UserFolder getMyTestRoot(String userID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserQuestionsFolder getQuestionFoldersRoot(String userID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserFolder getTestFolder(String testId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserFolder getFolderByTitle(String title, String parentId, String userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserQuestionsFolder getQuestionsFolderByTitle(String title, String parentId, String userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserQuestionsFolder getMyQuestionRoot(String userID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UserFolder> getChildFolders(String userID, String parentFolderId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UserFolder> getChildFolders(String parentFolderId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserQuestionsFolder getMyQuestionsFolder(String userID, String folderid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UserQuestionsFolder> getMyQuestionsFolders(String userID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UserQuestionsFolder> getChildQuestionFolders(String folderid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double getUserFolderMinSeq(String userID) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getUserQuestionsFolderMinSeq(String userId) {
		// TODO Auto-generated method stub
		return 0;
	}
	/*
	 * 
	 * private DataAccessHelper<UserFolder> accessor;
	 * 
	 * private DataAccessHelper<UserQuestionsFolder> accessorUserQuestionsFolder;
	 * 
	 * 
	 * 
	 *//**
		 * Constructor to access DataAccessHelper to perform Container operation.
		 * 
		 * @throws ConfigException
		 * @throws UnknownHostException
		 */
	/*
	 * public UserFoldersRepo(){ // accessor = new
	 * DataAccessHelper<UserFolder>(UserFolder.class); //accessorUserQuestionsFolder
	 * = new DataAccessHelper<UserQuestionsFolder>(UserQuestionsFolder.class); }
	 * 
	 *//**
		 * This method will get the root folder of the test.
		 * 
		 * @param userId represents the user.
		 * @return UserFolder.
		 */
	/*
	 * @Override public UserFolder getMyTestRoot(String userID){
	 * 
	 * Query<UserFolder> query = accessor.getDataQuery(); UserFolder rootFolder =
	 * query .filter(QueryFields.USERID, userID) .filter(QueryFields.PARENTID,
	 * userID) .get();
	 * 
	 * if(rootFolder == null) {
	 * 
	 * UserFolder folder = new UserFolder();
	 * 
	 * folder.setGuid(UUID.randomUUID().toString()); folder.setUserID(userID);
	 * folder.setParentId(userID); folder.setSequence(1.0); saveFolder(folder);
	 * 
	 * rootFolder = query .filter(QueryFields.USERID, userID)
	 * .filter(QueryFields.PARENTID, userID) .get(); }
	 * 
	 * return rootFolder; }
	 * 
	 *//**
		 * This method will get root folder of the user created questions.
		 * 
		 * @param userId represents the user.
		 * @return UserQuestionsFolder.
		 */
	/*
	 * @Override public UserQuestionsFolder getMyQuestionRoot(String userID){
	 * 
	 * Query<UserQuestionsFolder> query =
	 * accessorUserQuestionsFolder.getDataQuery(); UserQuestionsFolder rootFolder =
	 * query .filter(QueryFields.USERID, userID) .filter(QueryFields.PARENTID,
	 * userID) .get();
	 * 
	 * if(rootFolder == null) {
	 * 
	 * UserQuestionsFolder folder = new UserQuestionsFolder();
	 * 
	 * folder.setGuid(UUID.randomUUID().toString()); folder.setUserID(userID);
	 * folder.setParentId(userID); folder.setSequence(1.0); saveFolder(folder);
	 * 
	 * rootFolder = query .filter(QueryFields.USERID, userID)
	 * .filter(QueryFields.PARENTID, userID) .get(); }
	 * 
	 * return rootFolder; }
	 * 
	 *//**
		 * This method will get the folder of the user created questions based on folder
		 * id.
		 * 
		 * @param userId   , represents the user.
		 * @param folderid , represents the folder which user questions folder belongs
		 *                 to.
		 * @return UserQuestionsFolder.
		 */
	/*
	 * @Override public UserQuestionsFolder getMyQuestionsFolder(String userID,
	 * String folderid){
	 * 
	 * Query<UserQuestionsFolder> query =
	 * accessorUserQuestionsFolder.getDataQuery(); return query
	 * .filter(QueryFields.GUID, folderid) .get();
	 * 
	 * }
	 * 
	 * @Override public List<UserQuestionsFolder> getMyQuestionsFolders(String
	 * userID) { Query<UserQuestionsFolder> query =
	 * accessorUserQuestionsFolder.getDataQuery();
	 * 
	 * return query .filter(QueryFields.PARENTID,
	 * getMyQuestionRoot(userID).getGuid()) .order(QueryFields.SEQUENCE) .asList();
	 * }
	 * 
	 * @Override public List<UserQuestionsFolder> getChildQuestionFolders(String
	 * folderid) { Query<UserQuestionsFolder> query =
	 * accessorUserQuestionsFolder.getDataQuery();
	 * 
	 * return query .filter(QueryFields.PARENTID, folderid)
	 * .order(QueryFields.SEQUENCE) .asList(); }
	 * 
	 * @Override public List<UserFolder> getChildFolders(String userID, String
	 * parentFolderId){
	 * 
	 * Query<UserFolder> query = accessor.getDataQuery(); return query
	 * .filter(QueryFields.USERID, userID) .filter(QueryFields.PARENTID,
	 * parentFolderId) .order(QueryFields.SEQUENCE).asList(); }
	 * 
	 * @Override public List<UserFolder> getChildFolders(String parentFolderId){
	 * 
	 * Query<UserFolder> query = accessor.getDataQuery(); return query
	 * .filter(QueryFields.PARENTID, parentFolderId)
	 * .order(QueryFields.SEQUENCE).asList(); }
	 * 
	 * @Override public UserFolder getFolderByTitle(String title, String parentId,
	 * String userId){
	 * 
	 * UserFolder folder = null; Query<UserFolder> query = accessor.getDataQuery();
	 * folder = query .filter(QueryFields.USERID, userId)
	 * .filter(QueryFields.PARENTID, parentId) .filter(QueryFields.TITLE, title)
	 * .get();
	 * 
	 * return folder; }
	 * 
	 * @Override public double getUserFolderMinSeq(String userId){
	 * 
	 * Query<UserFolder> query = accessor.getDataQuery(); UserFolder folder = query
	 * .filter(QueryFields.USERID, userId) .filter(QueryFields.PARENTID, null)
	 * .order(QueryFields.SEQUENCE).limit(1).get(); if(folder == null) { return 1.0;
	 * } else { return folder.getSequence(); } }
	 * 
	 * @Override public double getUserQuestionsFolderMinSeq(String userId){
	 * 
	 * UserQuestionsFolder rootFolder = getMyQuestionRoot(userId);
	 * 
	 * Query<UserQuestionsFolder> query =
	 * accessorUserQuestionsFolder.getDataQuery(); UserQuestionsFolder folder =
	 * query .filter(QueryFields.USERID, userId) .filter(QueryFields.PARENTID,
	 * rootFolder.getGuid()) .order(QueryFields.SEQUENCE).limit(1).get(); if(folder
	 * == null) { return 1.0; } else { return folder.getSequence(); } }
	 * 
	 * @Override public UserFolder getFolder(String folderId){
	 * 
	 * Query<UserFolder> query = accessor.getDataQuery(); return
	 * query.filter(QueryFields.GUID, folderId).get(); }
	 * 
	 * @Override public UserFolder getTestFolder(String testId){
	 * 
	 * Query<UserFolder> query = accessor.getDataQuery(); return
	 * query.filter("testBindings.testId", testId).get(); }
	 * 
	 * @Override public void saveFolders(List<UserFolder> folders) {
	 * accessor.save(folders); }
	 * 
	 * @Override public void saveFolder(UserFolder folder) { accessor.save(folder);
	 * }
	 * 
	 * @Override public void deleteFolders(List<String> ids) { accessor.delete(ids);
	 * }
	 * 
	 * @Override public void deleteFolder(String id) { accessor.delete(id); }
	 * 
	 * @Override public UserQuestionsFolder getQuestionFoldersRoot(String userID) {
	 * Query<UserQuestionsFolder> query =
	 * accessorUserQuestionsFolder.getDataQuery(); return query
	 * .filter(QueryFields.USERID, userID) .filter(QueryFields.PARENTID, userID)
	 * .get(); }
	 * 
	 *//**
		 * This method will save the user questions folder to the database.
		 * 
		 * @param folders , represents the user question folders.
		 */
	/*
	 * @Override public void saveUserQuestionFolders(List<UserQuestionsFolder>
	 * folders) { accessorUserQuestionsFolder.save(folders);
	 * 
	 * }
	 * 
	 *//**
		 * Getting the questions folder by title parentid and userid
		 *//*
			 * @Override public UserQuestionsFolder getQuestionsFolderByTitle(String title,
			 * String parentId, String userId){
			 * 
			 * UserQuestionsFolder folder = null; Query<UserQuestionsFolder> query =
			 * accessorUserQuestionsFolder.getDataQuery(); folder = query
			 * .filter(QueryFields.USERID, userId) .filter(QueryFields.PARENTID, parentId)
			 * .filter(QueryFields.TITLE, title) .get();
			 * 
			 * return folder; }
			 */
}
