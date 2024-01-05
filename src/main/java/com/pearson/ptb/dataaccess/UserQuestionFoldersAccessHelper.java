package com.pearson.ptb.dataaccess;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Repository;
import com.pearson.ptb.bean.UserQuestionsFolder;

@Repository
public class UserQuestionFoldersAccessHelper extends GenericMongoRepository<UserQuestionsFolder, String>{
	@Autowired
	public UserQuestionFoldersAccessHelper(MongoOperations mongoOperations) {
		super(mongoOperations, UserQuestionsFolder.class);
	}
}
