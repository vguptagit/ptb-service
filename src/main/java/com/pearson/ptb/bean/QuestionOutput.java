package com.pearson.ptb.bean;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.googlecode.jmapper.annotations.JMap;

/**
 * The <code>QuestionOutput</code> class holds the details of the Question such as guid, qtixml,
 * id and metadata 
 *
 */
public class QuestionOutput implements Serializable {

	/**
	 * Indicate the version id of serialization
	 */
	private static final long serialVersionUID = 1L;
		
	/**
	 * Indicate the guid
	 */
	@JMap
	private String guid;
	
	/**
	 * Indicate the context
	 */
	@JMap
	private String context;
	
	/**
	 * Indicate the id
	 */
	@JMap
	private String id;
	
	/**
	 * Indicate the type
	 */
	@JMap
	private String type;
	
	/**
	 * Indicate the qtixml
	 */	
	private String qtixml;
	
	/**
	 * Indicate the metadata
	 */	
	private Metadata metadata;
	
	/** Get {@see #guid}. @return {@link #guid}. */
	public String getGuid() {
		return guid;
	}
	
	/** Set {@see #guid}. @param {@link #guid}. */
	public void setGuid(String guid) {
		this.guid = guid;
	}
	
	/** Get {@see #qtixml}. @return {@link #qtixml}. */
	public String getQtixml() {
		return qtixml;
	}
	
	/** Set {@see #qtixml}. @param {@link #qtixml}. */
	public void setQtixml(String qtixml) {
		this.qtixml = qtixml;
	}
	
	/** Get {@see #metadata}. @return {@link #metadata}. */
	public Metadata getMetadata() {
		return metadata;
	}
	
	/** Set {@see #metadata}. @param {@link #metadata}. */
	public void setMetadata(Metadata metadata) {
		this.metadata = metadata;
	}
	
	/** Get {@see #context}. @return {@link #context}. */
	@JsonProperty("@context")
	public String getContext() {
		return context;
	}
	
	/** Set {@see #context}. @param {@link #context}. */
	@JsonProperty("@context")
	public void setContext(String context) {
		this.context = context;
	}
	
	/** Get {@see #id}. @return {@link #id}. */
	@JsonProperty("@id")
	public String getId() {
		return id;
	}
	
	/** Set {@see #id}. @param {@link #id}. */
	@JsonProperty("@id")
	public void setId(String id) {
		this.id = id;
	}
	
	/** Get {@see #type}. @return {@link #type}. */
	@JsonProperty("@type")
	public String getType() {
		return type;
	}
	
	/** Set {@see #type}. @param {@link #type}. */
	@JsonProperty("@type")
	public void setType(String type) {
		this.type = type;
	}

	
	
}
