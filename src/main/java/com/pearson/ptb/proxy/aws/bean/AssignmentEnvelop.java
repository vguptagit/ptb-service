package com.pearson.ptb.proxy.aws.bean;

import com.googlecode.jmapper.annotations.JMap;

/**
 * This <code>AssignmentEnvelop</code> is entity of PAF to represents the assignment body.
 */

public class AssignmentEnvelop extends BaseEnvelop {
	
	/**
	 * Indicates the Assignment body
	 */
	@JMap
	private Assignment body;

	/** Get {@see #body}. @return {@link #body}. */
	public Assignment getBody() {
		return body;
	}

	/** Set {@see #body}. @param {@link #body}. */
	public void setBody(Assignment body) {
		this.body = body;
	}
}
