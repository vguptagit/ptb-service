package com.pearson.ptb.bean;

import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.googlecode.jmapper.annotations.JMap;
import com.pearson.ptb.framework.ConfigurationManager;
import com.pearson.ptb.framework.exception.ConfigException;
import com.pearson.ptb.framework.exception.InternalException;
import com.pearson.ptb.util.Common;

/**
 * The <code>TestResult</code> bean is responsible to hold the test save result
 * status
 * 
 * @author nithinjain
 *
 */
public class TestResult extends BaseLinkedDataEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * Indicates guid
	 */
	@JMap
	private String guid;

	/** Get {@see #guid}. @return {@link #guid}. */
	public String getGuid() {
		return guid;
	}

	/** Set {@see #guid}. @param {@link #guid}. */
	public void setGuid(String guid) {
		this.guid = guid;
	}

	/**
	 * To get the test Id
	 * 
	 * @return id
	 * @throws InternalException
	 */
	@JsonProperty("@id")
	public String getId() {
		try {
			/*return String.format(Common.TEST_END_POINT_FORMAT,
					ConfigurationManager.getInstance().getMyTestBaseUrl(),
					this.guid); */
			return StringUtils.EMPTY;
		} catch (ConfigException e) {
			throw new InternalException(
					"TestResultStatus.getId || Not able to read configuration - MyTestBaseUrl",
					e);
		}
	}

}
