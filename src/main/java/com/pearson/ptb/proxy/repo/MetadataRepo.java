package com.pearson.ptb.proxy.repo;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.pearson.ptb.bean.Metadata;
import com.pearson.ptb.dataaccess.GenericMongoRepository;
import com.pearson.ptb.proxy.MetadataDelegate;
import com.pearson.ptb.proxy.aws.bean.Activity;
import com.pearson.ptb.proxy.aws.bean.QuestionEnvelop;

@Repository
public class MetadataRepo implements MetadataDelegate {

	@Autowired
	private ModelMapper mapper;

	@Autowired
	private GenericMongoRepository<QuestionEnvelop, String> genericMongoRepository;

	@Override
	public Metadata getMetadata(String metadataId) {
		QuestionEnvelop questionEnvelop = genericMongoRepository.findById(metadataId);
		Activity activityMetadata = questionEnvelop.getMetadata();
		return mapper.map(activityMetadata, Metadata.class);
	}

}
