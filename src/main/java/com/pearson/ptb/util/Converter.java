/**
 * 
 */
package com.pearson.ptb.util;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;

import com.googlecode.jmapper.JMapper;

/**
 * Converter class designed to convert activities and test from PAF to Mytest
 * and vice versa
 *
 */
public final class Converter {

	private static final ModelMapper modelMapper = new ModelMapper();

	/**
	 * private constructor to avoid creation of this class instance
	 */
	private Converter() {

	}

	/**
	 * Generic method to convert bean.
	 * 
	 * @param source      , source bean which needs to convert
	 * @param destClass   , Class literal of the destination class. This will be
	 *                    like instance of the class to be converted.
	 * @param sourceClass , Class literal of the source class. This will be like
	 *                    instance of the class which need to be converted @return,
	 *                    Destination class with value of the source class
	 * 
	 *                    To work this all properties of the destination class
	 *                    should be annotated with JMap. If the property name of the
	 *                    destination class is different than source class, then
	 *                    property name of the destination class should be annotated
	 *                    with @JMap("{property name of the source class}") For ref
	 *                    : https://code.google.
	 *                    com/p/jmapper-framework/wiki/Introduction
	 * 
	 * @Review : Whether this need to be placed in seperate class?
	 */
	/*
	 * public static <D, S> D getDestinationBean(S source, Class<D> destClass,
	 * Class<S> sourceClass) { JMapper<D, S> mapper = new JMapper<D, S>(destClass,
	 * sourceClass); return mapper.getDestination(source); }
	 */
	 public static <D, S> D getDestinationBean(S source, Class<D> destinationType, Class<S> sourceType) {
	        return modelMapper.map(source, destinationType);
	    }
	/**
	 * Generic method to convert list of bean.
	 * 
	 * @param source      , list of source bean which needs to convert
	 * @param destClass   , Class literal of the destination class. This will be
	 *                    like instance of the class to be converted.
	 * @param sourceClass , Class literal of the source class. This will be like
	 *                    instance of the class which need to be converted @return,
	 *                    list of Destination bean with value of the source class
	 * 
	 *                    To work this all properties of the destination class
	 *                    should be annotated with JMap. If the property name of the
	 *                    destination class is different than source class, then
	 *                    property name of the destination class should be annotated
	 *                    with @JMap("{property name of the source class}") For ref
	 *                    : https://code.google.
	 *                    com/p/jmapper-framework/wiki/Introduction
	 * 
	 * @Review : Whether this need to be placed in separate class?
	 */
	public static <D, S> List<D> getDestinationBeanList(List<S> source, Class<D> destClass, Class<S> sourceClass) {
		JMapper<D, S> mapper = new JMapper<D, S>(destClass, sourceClass);
		List<D> beanList = new ArrayList<D>();
		for (S s : source) {
			beanList.add(mapper.getDestination(s));
		}
		return beanList;
	}
}
