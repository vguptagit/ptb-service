package com.pearson.ptb.bean;

/**
 * The <code>ArchiveItem</code> class is responsible to hold the details of the
 * Archive folder or test
 *
 */
public class ArchiveItem {

	/**
	 * Indicates the id
	 */
	private String id;

	/**
	 * Indicates the Folder Id
	 */
	private String folderId;

	/** Get {@see #id}. @return {@link #id}. */
	public String getId() {
		return id;
	}

	/** Set {@see #id}. @param {@link #id}. */
	public void setId(String id) {
		this.id = id;
	}

	/** Get {@see #folderId}. @return {@link #folderId}. */
	public String getFolderId() {
		return folderId;
	}

	/** Set {@see #folderId}. @param {@link #folderId}. */
	public void setFolderId(String folderId) {
		this.folderId = folderId;
	}
}
