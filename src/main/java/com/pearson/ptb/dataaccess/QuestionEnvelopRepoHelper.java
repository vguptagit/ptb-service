package com.pearson.ptb.dataaccess;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Repository;

import com.pearson.ptb.proxy.aws.bean.QuestionEnvelop;

@Repository
public class QuestionEnvelopRepoHelper extends GenericMongoRepository<QuestionEnvelop, String>{

	@Autowired
	public QuestionEnvelopRepoHelper(MongoOperations mongoOperations) {
		super(mongoOperations, QuestionEnvelop.class);
	}
}
