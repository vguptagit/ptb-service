package com.pearson.ptb.bean;

/**
 * The <code>TestBinding</code> class is responsible to hold the details of the Test Binding
 * such as testId and sequence 
 *
 */
public class TestBinding {
	
	/**
	 * Indicates Id of the test
	 */
	private String testId;
	
	/**
	 * Indicates sequence
	 */
	private double sequence;
	
	/** Get {@see #testId}. @return {@link #testId}. */
	public String getTestId() {
		return testId;
	}
	
	/** Set {@see #testId}. @param {@link #testId}. */
	public void setTestId(String testId) {
		this.testId = testId;
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
