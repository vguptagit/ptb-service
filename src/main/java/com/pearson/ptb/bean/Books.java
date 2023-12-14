package com.pearson.ptb.bean;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

/**
 * The <code>Books</code> class responsible to hold the Container of the book
 *
 */
public class Books extends Book {

	private static final long serialVersionUID = 1L;
	@JsonProperty("nodes")
	@SerializedName("nodes")
	private List<Containers> containers;
	
	/** Set {@see #container}. @param {@link #container}. */
	public void setContainers(List<Containers> container) {
		this.containers = container;
	}
		
	/** Get {@see #containers}. @return {@link #containers}. */
	public List<Containers> getContainers() {
		return containers;
	}
}
