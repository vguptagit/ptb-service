package com.pearson.ptb.bean;

import java.io.Serializable;

import com.googlecode.jmapper.annotations.JMap;

/**
 * The <code>BaseEntity</code> class is responsible to hold the guid and title
 * of a specific entity
 *
 */
public class BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * The guid entity
	 */
	@JMap
	@org.springframework.data.annotation.Id
	private String guid;

	/**
	 * The title of a specific entity
	 */
	@JMap
	private String title;

	/** Get {@see #guid}. @return {@link #guid}. */
	public String getGuid() {
		return guid;
	}

	/** Set {@see #guid}. @param {@link #guid}. */
	public void setGuid(String guid) {
		this.guid = guid;
	}

	/** Get {@see #title}. @return {@link #title}. */
	public String getTitle() {
		return title;
	}

	/** Set {@see #title}. @param {@link #title}. */
	public void setTitle(String title) {
		this.title = title;
	}

}
