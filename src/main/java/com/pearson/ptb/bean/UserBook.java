package com.pearson.ptb.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * The <code>UserBook</code> class is responsible to hold the user books
 * migrated from Pegasus
 *
 */
public class UserBook extends BaseEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * The instructor user id
	 */
	private String userId;

	/**
	 * The indicates testBindings
	 */
	private List<String> testBindings;

	/**
	 * The indicates questionBindings
	 */
	private List<String> questionBindings;

	/**
	 * To check if book is already imported, default set to false
	 */
	private Boolean isImported = false;

	/**
	 * The indicates imported date of the book
	 */
	private Date importedDate;

	/** Get {@see #userId}. @return {@link #userId}. */
	public String getUserId() {
		return userId;
	}

	/** Set {@see #userId}. @param {@link #userId}. */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/** Get {@see #testBindings}. @return {@link #testBindings}. */
	public List<String> getTestBindings() {
		if (testBindings == null) {
			testBindings = new ArrayList<String>();
		}
		return testBindings;
	}

	/** Set {@see #testBindings}. @param {@link #testBindings}. */
	public void setTestBindings(List<String> testBindings) {
		this.testBindings = testBindings;
	}

	/** Get {@see #questionBindings}. @return {@link #questionBindings}. */
	public List<String> getQuestionBindings() {
		if (questionBindings == null) {
			questionBindings = new ArrayList<String>();
		}
		return questionBindings;
	}

	/** Set {@see #questionBindings}. @param {@link #questionBindings}. */
	public void setQuestionBindings(List<String> questionBindings) {
		this.questionBindings = questionBindings;
	}

	/** Get {@see #isImported}. @return {@link #isImported}. */
	public Boolean getIsImported() {
		return isImported;
	}

	/** Set {@see #isImported}. @param {@link #isImported}. */
	public void setIsImported(Boolean isImported) {
		this.isImported = isImported;
	}

	/** Get {@see #importedDate}. @return {@link #importedDate}. */
	public Date getImportedDate() {
		return importedDate;
	}

	/** Set {@see #importedDate}. @param {@link #importedDate}. */
	public void setImportedDate(Date importedDate) {
		this.importedDate = importedDate;
	}

}
