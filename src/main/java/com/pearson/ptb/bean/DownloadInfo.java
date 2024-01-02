package com.pearson.ptb.bean;

import java.util.LinkedList;
import java.util.Queue;

import com.pearson.ptb.framework.exception.InternalException;

/**
 * The <code>DownloadInfo</code> class is responsible to hold the details of the
 * Download document
 *
 */
public class DownloadInfo implements Cloneable {

	/**
	 * The Title of the test
	 */
	private String testTitle;

	/**
	 * The questions of the test
	 */
	private Queue<String> questions;

	/**
	 * The printSettings of the document
	 */
	private PrintSettings printSettings;

	/** Get {@see #testTitle}. @return {@link #testTitle}. */
	public String getTestTitle() {
		return testTitle;
	}

	/** Set {@see #testTitle}. @param {@link #testTitle}. */
	public void setTestTitle(String testTitle) {
		this.testTitle = testTitle;
	}

	/** Get {@see #questions}. @return {@link #questions}. */
	public Queue<String> getQuestions() {
		return questions;
	}

	/** Set {@see #questions}. @param {@link #questions}. */
	public void setQuestions(Queue<String> questions) {
		this.questions = questions;
	}

	/** Get {@see #printSettings}. @return {@link #printSettings}. */
	public PrintSettings getPrintSettings() {
		return printSettings;
	}

	/** Set {@see #printSettings}. @param {@link #printSettings}. */
	public void setPrintSettings(PrintSettings printSettings) {
		this.printSettings = printSettings;
	}

	/**
	 * The details for download document
	 * 
	 * @return DownloadInfo
	 * @throws InternalException
	 */

	@SuppressWarnings("unchecked")
	@Override
	public DownloadInfo clone() {
		try {

			DownloadInfo downloadInfo;
			downloadInfo = (DownloadInfo) super.clone();
			downloadInfo.setQuestions(
					(Queue<String>) ((LinkedList<String>) this.questions)
							.clone());
			downloadInfo.setPrintSettings(this.printSettings.clone());
			return downloadInfo;
		} catch (CloneNotSupportedException e) {
			throw new InternalException("Exception while cloning  DownloadInfo",
					e);
		}
	}
}
