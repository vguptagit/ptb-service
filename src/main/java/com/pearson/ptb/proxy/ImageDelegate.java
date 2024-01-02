package com.pearson.ptb.proxy;

import org.springframework.web.multipart.MultipartFile;

/**
 * This interface defines the contract for uploading an image to repository
 */
public interface ImageDelegate {
	/**
	 * saving image to repository
	 * 
	 * @param file,
	 *            Springframework's file which contains byte array
	 * @return
	 */
	public String uploadImage(MultipartFile file);
}
