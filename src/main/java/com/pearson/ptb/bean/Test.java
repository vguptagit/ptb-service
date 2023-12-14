package com.pearson.ptb.bean;

import java.io.Serializable;

/**
 * PAF activity bean
 * 
 * @author nithinjain
 *
 */
public class Test extends LinkedDataEntity implements Serializable {

	/**
	 * Indicate the version id of serialization
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * The activity contents of type AssignmentContent
	 */
	private AssignmentContent assignmentContents;

	/** Get {@see #assignmentContents}. @return {@link #assignmentContents}. */
	public AssignmentContent getAssignmentContents() {
		return assignmentContents;
	}

	/** Set {@see #assignmentContents}. @param {@link #assignmentContents}. */
	public void setAssignmentContents(AssignmentContent assignmentContents) {
		this.assignmentContents = assignmentContents;
	}

}
