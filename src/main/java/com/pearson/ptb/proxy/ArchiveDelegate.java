package com.pearson.ptb.proxy;

import java.util.List;

import com.pearson.ptb.bean.ArchivedFolder;

/**
 * This interface defines the contract for accessing the Archive folders from
 * repository
 *
 */
public interface ArchiveDelegate {

	/**
	 * To archive the given folders
	 * 
	 * @param folders,
	 *            list of folders
	 *
	 */

	void archiveFolders(List<ArchivedFolder> folders);

	/**
	 * To get the archived folders
	 * 
	 * @param userID
	 * @param parentFolderId
	 * @return ArchivedFolder
	 *
	 */
	List<ArchivedFolder> getFolders(String userID, String parentFolderId);

	/**
	 * To save the archived folders
	 * 
	 * @param folder
	 */
	void saveFolder(ArchivedFolder folder);

	/**
	 * To get the archived folder by folder id
	 * 
	 * @param folderId
	 * @return ArchivedFolder
	 */
	ArchivedFolder getFolder(String folderId);

	/**
	 * To get the archived folder by title
	 * 
	 * @param title
	 * @param parentId
	 * @param userId
	 * @return ArchivedFolder
	 */
	ArchivedFolder getFolder(String title, String parentId, String userId);

	/**
	 * To get the Test Folder by test id
	 * 
	 * @param testId
	 * @return ArchivedFolder
	 */
	ArchivedFolder getTestFolder(String testId);

	/**
	 * To get the Archive Root by userID
	 * 
	 * @param userID
	 * @return ArchivedFolder
	 */
	ArchivedFolder getArchiveRoot(String userID);

	/**
	 * To get the Child Folders by parentFolderId
	 * 
	 * @param parentFolderId
	 * @return ArchivedFolder ,list of ArchivedFolder
	 */
	List<ArchivedFolder> getChildFolders(String parentFolderId);

	/**
	 * To delete the multiple folder by list of ids
	 * 
	 * @param ids
	 */
	void deleteFolders(List<String> ids);

	/**
	 * To delete the single folder by id
	 * 
	 * @param id
	 */
	void deleteFolder(String id);

}
