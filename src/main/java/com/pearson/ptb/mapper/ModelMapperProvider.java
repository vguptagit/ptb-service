package com.pearson.ptb.mapper;

import org.modelmapper.ModelMapper;

public class ModelMapperProvider {

	private static ModelMapper modelMapper = new ModelMapper();
	
	public static <S, D> D getDestinationBean(S entity, Class<D> destClass) {
		return modelMapper.map(entity,destClass);
	}
}
