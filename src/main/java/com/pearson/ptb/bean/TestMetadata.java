package com.pearson.ptb.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pearson.ptb.framework.ConfigurationManager;
import com.pearson.ptb.framework.exception.ConfigException;
import com.pearson.ptb.framework.exception.InternalException;
/**
 * This <code>TestMetadata</code> is responsible to hold the details of the test meta data
 *
 */
public class TestMetadata extends Metadata {

	private static final long serialVersionUID = 1L;
	private static final String TEST_END_POINT_FORMAT = "%s/tests/%s";
	
	@JsonProperty("@id")
	public String getId(){
		try {
			return String.format(TEST_END_POINT_FORMAT, ConfigurationManager
					.getInstance().getMyTestBaseUrl(), this.getGuid());
		} catch (ConfigException e) {
			throw new InternalException(
					"Not able to read configuration - Test.getId", e);
		}
	}
}
