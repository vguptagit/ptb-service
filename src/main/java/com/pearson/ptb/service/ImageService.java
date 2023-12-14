package com.pearson.ptb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.pearson.ptb.proxy.eps.ImageRepo;

/**
 * Service which meant for image operation like accessing, uploading, etc..
 * @author prasadbn
 *
 */
@Service("imageService")
public class ImageService {
	
	@Autowired
	@Qualifier("imageRepo")
	private ImageRepo imageAccessor;

	/**
	 * uploading the image file to EPS repository
	 * @param file, Springframework's file
	 * @return
	 */
	public String uploadImage(MultipartFile file){
		return imageAccessor.uploadImage(file);
	}
}
