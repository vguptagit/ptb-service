package com.pearson.ptb.proxy;

import com.pearson.ptb.bean.Metadata;
import com.pearson.ptb.framework.exception.InternalException;
import com.pearson.ptb.framework.exception.NotFoundException;

/**
 * This interface defines the contract for accessing test and question metadata
 *
 */

public interface MetadataDelegate {

	/**
	 * Gets the test metadata for both test and question
	 * 
	 * @param metadataId
	 * @return
	 * @throws InternalException
	 * @throws NotFoundException
	 */
	Metadata getMetadata(String metadataId);

}
