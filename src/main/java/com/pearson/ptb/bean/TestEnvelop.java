package com.pearson.ptb.bean;

/**
 * The <code>TestEnvelop</code> class is responsible to hold the details of the Test Envelop
 * such as metadata and content body 
 *
 */
public class TestEnvelop {
	
	/**
	 * The Metadata of the test
	 */
	private Metadata metadata;
	
	/**
	 * The content body of the test
	 */
	private Test body;
	
	/** Get {@see #metadata}. @return {@link #metadata}. */
	public Metadata getmetadata() {
		return metadata;
	}
	
	/** Set {@see #metadata}. @param {@link #metadata}. */
	public void setmetadata(Metadata metadata) {
		this.metadata = metadata;
	}
	
	/** Get {@see #body}. @return {@link #body}. */
	public Test getBody() {
		return body;
	}
	
	/** Set {@see #body}. @param {@link #body}. */
	public void setBody(Test body) {
		this.body = body;
	}
}
