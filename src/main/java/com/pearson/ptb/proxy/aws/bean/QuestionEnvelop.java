package com.pearson.ptb.proxy.aws.bean;

import org.springframework.data.mongodb.core.mapping.Document;

import com.googlecode.jmapper.annotations.JMap;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * This <code>QuestionEnvelop</code> is entity of PAF to represents the question body.
 */
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "userQuestions")
public class QuestionEnvelop extends BaseEnvelop {

	private static final long serialVersionUID = 1L;
	
	
	/**
	 * Indicates the question body
	 */
	@JMap
	private String body;

	/** Get {@see #body}. @return {@link #body}. */
	public String getBody() {
		return body;
	}

	/** Set {@see #body}. @param {@link #body}. */
	public void setBody(String body) {
		this.body = body;
	}

}
