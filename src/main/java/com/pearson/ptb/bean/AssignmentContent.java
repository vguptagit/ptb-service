package com.pearson.ptb.bean;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

/**
 * The PAF assignment entity
 * 
 * @author nithinjain
 *
 */
public class AssignmentContent extends BaseLinkedDataEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * The activity item bindings(Question details)
	 */
	private List<AssignmentBinding> binding;

	/**
	 * The activity content type
	 */
	@SerializedName("@contentType")
	@JsonProperty("@contentType")
	private String contentType;

	/** Get {@see #binding}. @return {@link #binding}. */
	public List<AssignmentBinding> getBinding() {
		return binding;
	}

	/** Set {@see #binding}. @param {@link #binding}. */
	public void setBinding(List<AssignmentBinding> binding) {
		this.binding = binding;
	}

	/** Get {@see #contentType}. @return {@link #contentType}. */
	public String getContentType() {
		return contentType;
	}

	/** Set {@see #contentType}. @param {@link #contentType}. */
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

}
