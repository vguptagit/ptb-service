package com.pearson.ptb.proxy.repo;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.pearson.ptb.bean.ArchivedFolder;
import com.pearson.ptb.proxy.ArchiveDelegate;

@Repository
public class ArchiveRepo implements ArchiveDelegate {

	@Override
	public void archiveFolders(List<ArchivedFolder> folders) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<ArchivedFolder> getFolders(String userID, String parentFolderId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveFolder(ArchivedFolder folder) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ArchivedFolder getFolder(String folderId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArchivedFolder getFolder(String title, String parentId, String userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArchivedFolder getTestFolder(String testId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArchivedFolder getArchiveRoot(String userID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ArchivedFolder> getChildFolders(String parentFolderId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteFolders(List<String> ids) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteFolder(String id) {
		// TODO Auto-generated method stub
		
	}

	/*
	 * private DataAccessHelper<ArchivedFolder> accessor;
	 * 
	 *//**
		 * Constructor to access DataAccessHelper to perform Container operation.
		 * 
		 * @throws ConfigException
		 * @throws UnknownHostException
		 */
	/*
	 * public ArchiveRepo(){ // accessor = new
	 * DataAccessHelper<ArchivedFolder>(ArchivedFolder.class); }
	 * 
	 * @Override public void archiveFolders(List<ArchivedFolder> folders) {
	 * accessor.save(folders); }
	 * 
	 * @Override public void saveFolder(ArchivedFolder folder) {
	 * accessor.save(folder); }
	 * 
	 *//**
		 * This method will get the root folder of the archived folders from database.
		 * 
		 * @param userID ,represents the user.
		 * @return ArchivedFolder.
		 */
	/*
	 * @Override public ArchivedFolder getArchiveRoot(String userID){
	 * 
	 * Query<ArchivedFolder> query = accessor.getDataQuery(); ArchivedFolder
	 * rootFolder = query .filter(QueryFields.USERID, userID)
	 * .filter(QueryFields.PARENTID, userID) .get();
	 * 
	 * if(rootFolder == null) {
	 * 
	 * ArchivedFolder folder = new ArchivedFolder();
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
		 * This method will get the archived folders from database.
		 * 
		 * @param parentFolderId ,represents the parent folder.
		 * @param userID         ,represents the user.
		 * @return list of ArchivedFolders.
		 */
	/*
	 * @Override public List<ArchivedFolder> getFolders(String userID, String
	 * parentFolderId){
	 * 
	 * List<ArchivedFolder> folders; Query<ArchivedFolder> query =
	 * accessor.getDataQuery();
	 * 
	 * folders = query .filter(QueryFields.USERID, userID)
	 * .filter(QueryFields.PARENTID, parentFolderId)
	 * .order(QueryFields.SEQUENCE).asList();
	 * 
	 * return folders; }
	 * 
	 *//**
		 * This method will get the child folders of the folder from database.
		 * 
		 * @param parentFolderId ,represents the parent folder.
		 * @return list of ArchivedFolder.
		 */
	/*
	 * @Override public List<ArchivedFolder> getChildFolders(String parentFolderId){
	 * 
	 * Query<ArchivedFolder> query = accessor.getDataQuery();
	 * 
	 * return query .filter(QueryFields.PARENTID, parentFolderId)
	 * .order(QueryFields.SEQUENCE).asList(); }
	 * 
	 *//**
		 * This method will get the folder from database.
		 * 
		 * @param folderId ,represents the folder.
		 * @return ArchivedFolder.
		 */
	/*
	 * @Override public ArchivedFolder getFolder(String folderId){
	 * 
	 * Query<ArchivedFolder> query = accessor.getDataQuery(); return
	 * query.filter(QueryFields.GUID, folderId).get(); }
	 * 
	 *//**
		 * This method will get the folder from database.
		 * 
		 * @param parentId ,represents the parent of the folder.
		 * @param title    ,represents folder name.
		 * @param userId   ,represents the user.
		 * @return ArchivedFolder.
		 */
	/*
	 * @Override public ArchivedFolder getFolder(String title, String parentId,
	 * String userId){
	 * 
	 * ArchivedFolder folder = null; Query<ArchivedFolder> query =
	 * accessor.getDataQuery(); folder = query .filter(QueryFields.USERID, userId)
	 * .filter(QueryFields.PARENTID, parentId) .filter(QueryFields.TITLE, title)
	 * .get();
	 * 
	 * return folder; }
	 * 
	 *//**
		 * This method will get the folder of the test from database.
		 * 
		 * @param testId ,represents the test.
		 * @return ArchivedFolder.
		 */
	/*
	 * @Override public ArchivedFolder getTestFolder(String testId){
	 * 
	 * Query<ArchivedFolder> query = accessor.getDataQuery(); return
	 * query.filter("testBindings.testId", testId).get(); }
	 * 
	 *//**
		 * This method will delete the folders from database.
		 * 
		 * @param ids ,list of string represents folders.
		 */
	/*
	 * @Override public void deleteFolders(List<String> ids) { accessor.delete(ids);
	 * }
	 * 
	 *//**
		 * This method will delete the folder from database.
		 * 
		 * @param id ,represents folder.
		 *//*
			 * @Override public void deleteFolder(String id) { accessor.delete(id); }
			 */
}