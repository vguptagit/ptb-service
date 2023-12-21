package com.pearson.ptb.bean;

import com.google.gson.annotations.SerializedName;
import com.googlecode.jmapper.annotations.JMap;

/**
 * This <code>Assignment</code> is entity of PAF to represents the details of
 * the activity such as title guid, assignmentContents.
 *
 */
public class Assignment extends com.pearson.ptb.bean.BaseLinkedDataEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * The title of a activity
	 */
	@JMap
	private String title;

	@JMap
	private String guid;

	/**
	 * The paf activity contents of type AssignmentContent
	 */
	@JMap
	private AssignmentContent assignmentContents;

	@SerializedName("@id")
	private String id;

	/** Get {@see #id}. @return {@link #id}. */
	public String getId() {
		return id;
	}

	/** Set {@see #id}. @param {@link #id}. */
	public void setId(String id) {
		this.id = id;
	}

	/** Get {@see #guid}. @return {@link #guid}. */
	public String getGuid() {
		return guid;
	}

	/** Set {@see #guid}. @param {@link #guid}. */
	public void setGuid(String guid) {
		this.guid = guid;
	}

	/** Get {@see #title}. @return {@link #title}. */
	public String getTitle() {
		return title;
	}

	/** Set {@see #title}. @param {@link #title}. */
	public void setTitle(String title) {
		this.title = title;
	}

	/** Get {@see #assignmentContents}. @return {@link #assignmentContents}. */
	public AssignmentContent getAssignmentContents() {
		return assignmentContents;
	}

	/** Set {@see #assignmentContents}. @param {@link #assignmentContents}. */
	public void setAssignmentContents(AssignmentContent assignmentContents) {
		this.assignmentContents = assignmentContents;
	}

}
