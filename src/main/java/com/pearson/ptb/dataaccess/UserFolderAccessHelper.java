package com.pearson.ptb.dataaccess;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Repository;
import com.pearson.ptb.bean.UserFolder;

@Repository
public class UserFolderAccessHelper extends GenericMongoRepository<UserFolder, String>{

	@Autowired
	public UserFolderAccessHelper(MongoOperations mongoOperations) {
		super(mongoOperations, UserFolder.class);
	}
}
