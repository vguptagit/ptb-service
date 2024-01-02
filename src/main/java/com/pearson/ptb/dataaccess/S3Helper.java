package com.pearson.ptb.dataaccess;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Repository;

import com.pearson.ptb.bean.FileMeta;

@Repository
public class S3Helper extends GenericMongoRepository<FileMeta, String> {

	@Autowired
	public S3Helper(MongoOperations mongoOperations) {
		super(mongoOperations, FileMeta.class);
	}
}
