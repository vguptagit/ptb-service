package com.pearson.ptb.bean;

import java.io.Serializable;

/**
 *  The <code>ExtMetadata</code> class is responsible to hold the metadata details
 *
 */
public class ExtMetadata implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * The name of the metadata
	 */
	private String name;
	
	/**
	 * The value of the metadata
	 */
	private String value;
	
	/** Get {@see #name}. @return {@link #name}. */
	public String getName() {
		return name;
	}
	/** Set {@see #name}. @param {@link #name}. */
	public void setName(String name) {
		this.name = name;
	}
	
	/** Get {@see #value}. @return {@link #value}. */
	public String getValue() {
		return value;
	}
	
	/** Set {@see #value}. @param {@link #value}. */
	public void setValue(String value) {
		this.value = value;
	}
}
