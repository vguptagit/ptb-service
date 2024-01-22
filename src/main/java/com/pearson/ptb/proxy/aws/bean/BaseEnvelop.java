package com.pearson.ptb.proxy.aws.bean;

import com.google.gson.annotations.SerializedName;
import com.googlecode.jmapper.annotations.JMap;
import com.pearson.ptb.bean.BaseEntity;
import com.pearson.ptb.bean.Metadata;

/**
 *  This <code>BaseEnvelop</code> is entity of PAF to represents the detail of the activity such as
 *  metadata, context and type.
 *
 */
public class BaseEnvelop extends BaseEntity {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	@SerializedName("@context")
	private String context = "http://purl.org/pearson/content/v1/ctx/metadata/envelope";
	@SerializedName("@type")
	private String type = "Envelope";

	@JMap(value = "metadata", classes = { Metadata.class })
	private Activity metadata;
	
	
	/** Get {@see #context}. @return {@link #context}. */	
	public String getContext() {
		return context;
	}
	
	/** Get {@see #type}. @return {@link #type}. */	
	public String getType() {
		return type;
	}
	
	/** Get {@see #metadata}. @return {@link #metadata}. */
	public Activity getMetadata() {
		return metadata;
	}

	/** Set {@see #metadata}. @param {@link #metadata}. */
	public void setMetadata(Activity metadata) {
		this.metadata = metadata;
	}
}
