package com.pearson.ptb.bean;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * The <code>TestEnvelop</code> class is responsible to hold the details of the
 * Test Envelop such as metadata and content body
 *
 */
@Document(collection = "userTests")
public class TestEnvelop extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

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
