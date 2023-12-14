package com.pearson.ptb.bean;

import com.googlecode.jmapper.annotations.JMap;
import com.pearson.ptb.bean.Assignment;

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
