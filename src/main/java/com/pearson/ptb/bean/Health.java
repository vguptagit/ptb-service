package com.pearson.ptb.bean;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The <code>Health</code> class is responsible to hold the details of the
 * application
 *
 */
public class Health {

	/**
	 * The Name of the application
	 */
	private String applicationName;

	/**
	 * The Owner of the application
	 */
	private String applicationOwner;

	/**
	 * The Domain of the application
	 */
	private String applicationDomain;

	/** Get {@see #applicationName}. @return {@link #applicationName}. */
	@JsonProperty("name")
	public String getApplicationName() {
		return applicationName;
	}

	/** Set {@see #applicationName}. @param {@link #applicationName}. */
	@JsonProperty("name")
	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

	/** Get {@see #applicationOwner}. @return {@link #applicationOwner}. */
	@JsonProperty("owner")
	public String getApplicationOwner() {
		return applicationOwner;
	}

	/** Set {@see #applicationOwner}. @param {@link #applicationOwner}. */
	@JsonProperty("owner")
	public void setApplicationOwner(String applicationOwner) {
		this.applicationOwner = applicationOwner;
	}

	/** Get {@see #applicationDomain}. @return {@link #applicationDomain}. */
	@JsonProperty("domain")
	public String getApplicationDomain() {
		return applicationDomain;
	}

	/** Set {@see #applicationDomain}. @param {@link #applicationDomain}. */
	@JsonProperty("domain")
	public void setApplicationDomain(String applicationDomain) {
		this.applicationDomain = applicationDomain;
	}

}
