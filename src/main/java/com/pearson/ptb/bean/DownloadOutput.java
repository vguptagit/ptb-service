package com.pearson.ptb.bean;

/**
 *  The <code>DownloadOutput</code> class is responsible to hold the details of the download document 
 *  such as content type and filename
 *
 */
public class DownloadOutput {
	
	/**
	 *  Indicates content Type 
	 */
	private String contentType;
	
	/**
	 *  Indicates name of the file
	 */
	private String fileName;
	
	/** Get {@see #contentType}. @return {@link #contentType}. */
	public String getContentType() {
		return contentType;
	}
	
	/** Set {@see #contentType}. @param {@link #contentType}. */
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	
	/** Get {@see #fileName}. @return {@link #fileName}. */
	public String getFileName() {
		return fileName;
	}
	
	/** Set {@see #fileName}. @param {@link #fileName}. */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

}
