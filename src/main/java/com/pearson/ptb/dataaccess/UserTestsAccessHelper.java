package com.pearson.ptb.dataaccess;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Repository;

import com.pearson.ptb.bean.TestEnvelop;

@Repository
public class UserTestsAccessHelper extends GenericMongoRepository<TestEnvelop, String>{
	@Autowired
	public UserTestsAccessHelper(MongoOperations mongoOperations) {
		super(mongoOperations, TestEnvelop.class);
	}
}
