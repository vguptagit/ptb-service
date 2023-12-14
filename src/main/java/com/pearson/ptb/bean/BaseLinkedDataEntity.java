package com.pearson.ptb.bean;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.googlecode.jmapper.annotations.JMap;

public class BaseLinkedDataEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * The context of a entity and this will be in URL format
	 */
	@JMap
	@SerializedName("@context")
	@JsonProperty("@context")
	private String context;

	/**
	 * The type of a entity
	 */
	@JMap
	@SerializedName("@type")
	@JsonProperty("@type")
	private String type;

	/** Get {@see #context}. @return {@link #context}. */
	public String getContext() {
		return context;
	}

	/** Set {@see #context}. @param {@link #context}. */
	public void setContext(String context) {
		this.context = context;
	}

	/** Get {@see #type}. @return {@link #type}. */
	public String getType() {
		return type;
	}

	/** Set {@see #type}. @param {@link #type}. */
	public void setType(String type) {
		this.type = type;
	}
}
