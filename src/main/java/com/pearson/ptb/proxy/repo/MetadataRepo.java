package com.pearson.ptb.proxy.repo;

import org.springframework.stereotype.Repository;

import com.pearson.ptb.bean.Metadata;
import com.pearson.ptb.proxy.MetadataDelegate;

@Repository
public class MetadataRepo implements MetadataDelegate {

	@Override
	public Metadata getMetadata(String metadataId) {
		// TODO Auto-generated method stub
		return null;
	}

}
