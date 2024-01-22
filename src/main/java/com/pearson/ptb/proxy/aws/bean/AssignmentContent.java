package com.pearson.ptb.proxy.aws.bean;

import java.util.List;

import com.googlecode.jmapper.annotations.JMap;
/**
 *  This <code>AssignmentContent</code> is entity of PAF to represents the detail of the activity item such as
 *  assignment binding.
 *
 */
public class AssignmentContent {

	/**
	 * The activity item bindings(Question details)
	 */
	@JMap
	private List<AssignmentBinding> binding;

	/** Get {@see #binding}. @return {@link #binding}. */
	public List<AssignmentBinding> getBinding() {
		return binding;
	}

	/** Set {@see #binding}. @param {@link #binding}. */
	public void setBinding(List<AssignmentBinding> binding) {
		this.binding = binding;
	}

}
