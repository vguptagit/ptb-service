package com.pearson.ptb.proxy.aws.bean;

import com.googlecode.jmapper.annotations.JMap;
import com.pearson.ptb.framework.ConfigurationManager;
import com.pearson.ptb.framework.exception.ConfigException;
import com.pearson.ptb.framework.exception.InternalException;
/**
 *  This <code>AssignmentBinding</code> is entity of PAF to represents the details of the activity such as
 *  guid, activityFormat and bindingIndex.
 *
 */
public class AssignmentBinding {

	/**
	 * The guid of a question
	 */
	@JMap
	private String guid;

	/**
	 * The activity format
	 */
	@JMap
	private String activityFormat;

	/**
	 * The activity binding index
	 */
	@JMap
	private int bindingIndex;

	private String boundActivity;

	/** Get {@see #guid}. @return {@link #guid}. */
	public String getGuid() {
		return guid;
	}

	/** Get {@see #guid}. @return {@link #guid}. */
	public void setGuid(String guid){
		this.guid = guid;
		try {
			StringBuilder boundActivityUrl = new StringBuilder();
			boundActivityUrl
					.append(ConfigurationManager.getInstance().getPAFBaseUrl())
					.append(ConfigurationManager.getInstance()
							.getPAFActivitiesEndPoint()).append("/")
					.append(this.getGuid());
			this.boundActivity = boundActivityUrl.toString();
		} catch (ConfigException e) {
			throw new InternalException(
					"Not able to read configuration - AssignmentBinding.getBoundActivity",
					e);
		}
	}

	/**
	 * Get {@see #activityFormat}. @return {@link #activityFormat}.
	 * 
	 * @throws InternalException
	 */
	public String getActivityFormat() {

		return activityFormat;
	}

	/**
	 * Set {@see #activityFormat}. @param {@link #activityFormat}.
	 * 
	 * @throws InternalException
	 */
	public void setActivityFormat(String activityFormat){

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.pearson.mytest.bean.AssignmentBinding#getBoundActivity()
	 */
	public String getBoundActivity() {
		return this.boundActivity;
	}

}
