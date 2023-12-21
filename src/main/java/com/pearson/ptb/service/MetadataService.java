package com.pearson.ptb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.pearson.ptb.bean.Metadata;
import com.pearson.ptb.framework.CacheWrapper;
import com.pearson.ptb.proxy.MetadataDelegate;
import com.pearson.ptb.util.CacheKey;

import org.springframework.stereotype.Service;

/**
 * This <code>MetadataService</code> is responsible to get the mete data for
 * tests.
 */
@Service("metadataService")
public class MetadataService {

	@Autowired
	@Qualifier("metadataRepo")
	private MetadataDelegate metadataRepo;

	private static CacheWrapper CACHE;
	/**
	 * This constructor initializes the instance of the cache wrapper object for
	 * caching operation.
	 */
	public MetadataService() {
		// CACHE = CacheWrapper.getInstance();
	}

	/**
	 * Gets the meta data details of the test
	 * 
	 * @param testId
	 * @return metadata object
	 */
	public Metadata getMetadata(String metadataId) {

		String metadataCacheKey = String.format(CacheKey.METADATA_FORMAT,
				metadataId);
		Metadata metadata = CACHE.get(metadataCacheKey);
		if (metadata == null) {
			metadata = metadataRepo.getMetadata(metadataId);
			CACHE.set(metadataCacheKey, metadata);
		}
		return metadata;
	}
}
