package com.pearson.ptb.dataaccess;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Repository;

import com.pearson.ptb.bean.UserSettings;

@Repository
public class UserSettingsHelper extends GenericMongoRepository<UserSettings, String> {
	
	@Autowired
	public UserSettingsHelper(MongoOperations mongoOperations) {
		super(mongoOperations, UserSettings.class);
	}

}
