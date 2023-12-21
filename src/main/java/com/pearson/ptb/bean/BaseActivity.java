package com.pearson.ptb.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.googlecode.jmapper.annotations.JMap;
import com.pearson.ptb.framework.ConfigurationManager;

/**
 * This <code>BaseActivity</code> is entity of PAF to represents the details
 * such as title and guid.
 * 
 * @author nithinjain
 */
public class BaseActivity {

	/**
	 * The title of a activity
	 */
	@JMap
	private String title;

	/**
	 * The guid of a activity
	 */
	@JMap
	private String guid;

	/**
	 * The context of a activity
	 */
	@SerializedName("@context")
	@JsonProperty("@context")
	private String context = "http://purl.org/pearson/content/v1/ctx/metadata/MetadataId";

	/**
	 * The id of a activity containse link data(The other/same project reset ful
	 * api end point
	 */
	@SerializedName("@id")
	@JsonProperty("@id")
	private String id;

	/**
	 * The default constructor
	 */
	public BaseActivity() {
		super();
	}

	/** Get {@see #title}. @return {@link #title}. */
	public String getTitle() {
		return title;
	}

	/** Set {@see #title}. @param {@link #title}. */
	public void setTitle(String title) {
		this.title = title;
	}

	/** Get {@see #guid}. @return {@link #guid}. */
	public String getGuid() {
		return guid;
	}

	/** Set {@see #guid}. @param {@link #guid}. */
	public void setGuid(String guid) {
		this.guid = guid;
		StringBuilder url = new StringBuilder();
		url.append(ConfigurationManager.getInstance().getPAFBaseUrl()).append(
				ConfigurationManager.getInstance().getPAFActivitiesEndPoint())
				.append("/").append(guid);
		this.setId(url.toString());
	}

	/** Get {@see #context}. @return {@link #context}. */
	public String getContext() {
		return context;
	}

	/** Set {@see #context}. @param {@link #context}. */
	public void setContext(String context) {
		this.context = context;
	}

	/** Get {@see #id}. @return {@link #id}. */
	public String getId() {
		return id;
	}

	/** Set {@see #id}. @param {@link #id}. */
	public void setId(String id) {
		this.id = id;
	}

}