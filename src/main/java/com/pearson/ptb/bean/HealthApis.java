package com.pearson.ptb.bean;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The <code>HealthApis</code> bean is responsible to hold the Health
 * information of the application
 *
 */
public class HealthApis {

	/**
	 * Indicates healthInfo
	 */
	private List<Health> healthInfo;

	/** Get {@see #healthInfo}. @return {@link #healthInfo}. */
	@JsonProperty("apis")
	public List<Health> getHealthApi() {
		return this.healthInfo;
	}

	/** Set {@see #healthInfo}. @param {@link #healthInfo}. */
	public void setHealthApi(List<Health> healthInfo) {
		this.healthInfo = healthInfo;
	}

}
