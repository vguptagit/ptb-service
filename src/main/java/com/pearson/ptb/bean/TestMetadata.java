package com.pearson.ptb.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * This <code>TestMetadata</code> is responsible to hold the details of the test
 * meta data
 *
 */
public class TestMetadata extends Metadata {

	private static final long serialVersionUID = 1L;
	private static final String TEST_END_POINT_FORMAT = "%s/tests/%s";
	
	@JsonProperty("@id")
	public String getId() {
		return this.getGuid();
	}
}
