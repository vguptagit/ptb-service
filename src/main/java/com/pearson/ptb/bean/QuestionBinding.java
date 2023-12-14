package com.pearson.ptb.bean;

/**
 * The <code>QuestionBinding</code> class is responsible to hold the details of the Question Binding
 * such as questionId and sequence 
 *
 */
public class QuestionBinding {
	
	/**
	 * Indicates Id of the question
	 */
	private String questionId;
	
	/**
	 * Indicates sequence
	 */
	private double sequence;
	
	/** Get {@see #questionId}. @return {@link #questionId}. */
	public String getQuestionId() {
		return questionId;
	}
	
	/** Set {@see #questionId}. @param {@link #questionId}. */
	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}
	
	/** Get {@see #sequence}. @return {@link #sequence}. */
	public double getSequence() {
		return sequence;
	}
	
	/** Set {@see #sequence}. @param {@link #sequence}. */
	public void setSequence(double sequence) {
		this.sequence = sequence;
	}
}
