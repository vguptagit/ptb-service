package com.pearson.ptb.dataaccess;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Repository;

import com.pearson.ptb.bean.Login;

@Repository
public class LoginAccessHelper  extends GenericMongoRepository<Login, String> {
	
	@Autowired
	public LoginAccessHelper(MongoOperations mongoOperations) {
		super(mongoOperations, Login.class);
	}



}
