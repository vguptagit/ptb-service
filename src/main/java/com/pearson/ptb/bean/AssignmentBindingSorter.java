package com.pearson.ptb.bean;

import java.util.Comparator;
/**
 * This <code>AssignmentBindingSorter</code> is responsible for sorting the
 * assignment bindings by passing Comparators to a sort method, which imposes a
 * total ordering on AssignmentBindings
 * 
 */
public class AssignmentBindingSorter implements Comparator<AssignmentBinding> {

	@Override
	public int compare(AssignmentBinding arg0, AssignmentBinding arg1) {
		return arg0.getBindingIndex() - arg1.getBindingIndex();
	}
}
