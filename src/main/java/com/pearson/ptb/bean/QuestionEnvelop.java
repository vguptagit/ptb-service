package com.pearson.ptb.bean;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The <code>QuestionEnvelop</code> class is responsible to hold the details of
 * the Question Envelop such as metadata and content body
 *
 */

public class QuestionEnvelop {

	/**
	 * The Metadata of the Question
	 */
	private Metadata metadata;

	/**
	 * The Question body
	 */
	private String body;

	/** Get {@see #metadata}. @return {@link #metadata}. */
	public Metadata getmetadata() {
		return metadata;
	}

	/** Set {@see #metadata}. @param {@link #metadata}. */
	public void setmetadata(Metadata metadata) {
		this.metadata = metadata;
	}

	/** Get {@see #body}. @return {@link #body}. */
	public String getBody() {
		return body;
	}

	/** Set {@see #body}. @param {@link #body}. */
	public void setBody(String body) {
		this.body = body;
	}
}
