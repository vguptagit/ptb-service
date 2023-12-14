package com.pearson.ptb.bean;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pearson.ptb.framework.ConfigurationManager;
import com.pearson.ptb.framework.exception.ConfigException;
import com.pearson.ptb.framework.exception.InternalException;
import com.pearson.ptb.util.Common;

/**
 * The PAF assignment question binding entity
 * 
 * @author nithinjain
 *
 */
public class AssignmentBinding implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * The guid of a question
	 */
	private String guid;

	/**
	 * The activity format
	 */
	private String activityFormat;

	/**
	 * The activity binding index
	 */
	private int bindingIndex;

	/** Get {@see #guid}. @return {@link #guid}. */
	public String getGuid() {
		return guid;
	}

	/** Get {@see #guid}. @return {@link #guid}. */
	public void setGuid(String guid) {
		this.guid = guid;
	}

	/** Get {@see #activityFormat}. @return {@link #activityFormat}. */
	public String getActivityFormat() {
		return activityFormat;
	}

	/** Set {@see #activityFormat}. @param {@link #activityFormat}. */
	public void setActivityFormat(String activityFormat) {
		this.activityFormat = activityFormat;
	}

	/** Get {@see #bindingIndex}. @return {@link #bindingIndex}. */
	public int getBindingIndex() {
		return bindingIndex;
	}

	/** Set {@see #bindingIndex}. @param {@link #bindingIndex}. */
	public void setBindingIndex(int bindingIndex) {
		this.bindingIndex = bindingIndex;
	}

	/**
	 * Get {@see #boundActivity}. @return {@link #boundActivity}.
	 * 
	 * @throws InternalException
	 */
	@JsonProperty("boundActivity")
	public String getBoundActivity(){
		try {
			return String.format(Common.QUESTION_END_POINT_FORMAT,
					ConfigurationManager.getInstance().getMyTestBaseUrl(),
					this.getGuid());
		} catch (ConfigException e) {
			throw new InternalException(
					"Not able to read configuration - AssignmentBinding.getBoundActivity",
					e);
		}
	}

}
