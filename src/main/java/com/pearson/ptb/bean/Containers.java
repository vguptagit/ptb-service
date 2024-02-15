package com.pearson.ptb.bean;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The <code>Containers</code> bean responsible to hold the Containers list
 *
 */
public class Containers extends Container implements Serializable {
	@JsonProperty("nodes")
	private List<Containers> containers;

	/**
	 * Indicate the version id of serialization
	 */
	private static final long serialVersionUID = 1L;

	/** Set {@see #containerList}. @param {@link #containerList}. */
	public void setContainers(List<Containers> containerList) {
		this.containers = containerList;
	}

	/** Get {@see #containerList}. @return {@link #containerList}. */
	public List<Containers> getContainers() {
		return containers;
	}
}
