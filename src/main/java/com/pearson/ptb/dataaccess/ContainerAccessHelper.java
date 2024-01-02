package com.pearson.ptb.dataaccess;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Repository;

import com.pearson.ptb.bean.Container;

@Repository
public class ContainerAccessHelper extends GenericMongoRepository<Container, String>{
	@Autowired
	public ContainerAccessHelper(MongoOperations mongoOperations) {
		super(mongoOperations, Container.class);
	}

}
