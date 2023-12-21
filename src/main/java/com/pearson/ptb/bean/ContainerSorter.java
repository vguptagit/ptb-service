package com.pearson.ptb.bean;

import java.util.Comparator;
/**
 * This <code>ContainerSorter</code> is responsible for sorting the containers
 * by passing Comparators to a sort method, which imposes a total ordering on
 * containers.
 * 
 */
public class ContainerSorter implements Comparator<Container> {
	@Override
	public int compare(Container arg0, Container arg1) {
		return Double.compare(arg0.getSequence(), arg1.getSequence());
	}

}
