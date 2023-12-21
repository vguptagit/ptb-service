package com.pearson.ptb.bean;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pearson.ptb.framework.ConfigurationManager;
import com.pearson.ptb.framework.exception.ConfigException;
import com.pearson.ptb.framework.exception.InternalException;
import com.pearson.ptb.util.Common;
/**
 * This <code>QuestionMetadata</code> is responsible to hold the meta data of
 * the questions
 *
 */
public class QuestionMetadata extends Metadata implements Serializable {

	/**
	 * Indicate the version id of serialization
	 */
	private static final long serialVersionUID = 1L;

	private List<String> questionHierarchy;

	/*
	 * to get the question hierarchy
	 */
	public List<String> getQuestionHierarchy() {
		return questionHierarchy;
	}

	/*
	 * to set the question hierarchy
	 */
	public void setQuestionHierarchy(List<String> questionHierarchy) {
		this.questionHierarchy = questionHierarchy;
	}

	@JsonProperty("@id")
	public String getId() {
		try {
			return String.format(Common.QUESTION_END_POINT_FORMAT,
					ConfigurationManager.getInstance().getMyTestBaseUrl(),
					this.getGuid());
		} catch (ConfigException e) {
			throw new InternalException(
					"Not able to read configuration - Question.getId", e);
		}
	}
}
