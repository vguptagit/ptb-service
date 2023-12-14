package com.pearson.ptb.proxy.eps;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.entity.ContentType;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import com.pearson.ptb.framework.ConfigurationManager;
import com.pearson.ptb.framework.exception.InternalException;
import com.pearson.ptb.proxy.ImageDelegate;
import com.pearson.ptb.util.HttpResponse;
import com.pearson.ptb.util.HttpUtility;

/**
 * Image repository accessor which holds responsibility of image operations like
 * getting and uploading
 * @author prasadbn
 *
 */
@Repository("imageRepo")
public class ImageRepo implements ImageDelegate {
	
	private static final  String AUTHORISATION = "Authorization";
	private static final  String BEARER = "Bearer ";
	private static final  String CONTENT_TYPE = "content-type";
	private static final  String APPLICATION_JSON = "application/json";
	private static final  String EPS_PAYLOAD = "{\"collection\":{\"uuid\":\"b28f1ffe-2008-4f5e-d559-83c8acd79316\"},\"metadata\": \"<xml><item><name>This one has a file</name><description>Created while following the tutorials</description></item></xml>\"}";
	private static final  String FILE = "/file";
	

	/**
	 * Uploading the image to EPS converter given Spring framework's Multipart file.
	 * 1. Creating staging area in EPS
	 * 2. Updating the image into staging area by sending the byte array of file.
	 * 3. Creating the item from staging area
	 */
	@Override
	public String uploadImage(MultipartFile file) {
		HttpUtility utility = new HttpUtility();
		Map<String, String> headers = new HashMap<String, String>();
		Map<String,String> queryString = new HashMap<String, String>();
		
		headers.put(AUTHORISATION, BEARER + ConfigurationManager.getInstance().getEPSAuthorisationToken());
		
		HttpResponse response;
		
		response = createStagingArea(utility, headers);
		
		String location = response.getResponseHeader("Location");
		String stagingID = response.getResponseHeader("x-eps-stagingid");
		String imageName = file.getOriginalFilename().replace(" ", "");
		
		response = insertImageToStagingArea(file, utility, headers, location);
		
		headers.put(CONTENT_TYPE, APPLICATION_JSON);
		queryString.put(file.getName(), stagingID);
		
		response = createItemFromStagingArea(utility, headers, queryString);
		
		location = response.getResponseHeader("Location");
		
		return location + FILE + "/" + imageName;
	}

	/**
	 * creating the item in EPS repository from created Staging area
	 * @param utility
	 * @param headers
	 * @param queryString
	 * @return
	 */
	private HttpResponse createItemFromStagingArea(HttpUtility utility,
			Map<String, String> headers, Map<String, String> queryString) {
		HttpResponse response;
		response = utility.makePost(ConfigurationManager.getInstance().getEPSItemURL(), headers, ContentType.APPLICATION_JSON , queryString, EPS_PAYLOAD);
		return response;
	}

	/**
	 * updating the file data to created staging area of the EPS repository.
	 * @param file
	 * @param utility
	 * @param headers
	 * @param location
	 * @return
	 */
	private HttpResponse insertImageToStagingArea(MultipartFile file,
			HttpUtility utility, Map<String, String> headers, String location) {
		HttpResponse response;
		try {
			response = utility.updateFile(location + "/" + file.getOriginalFilename().replace(" ", ""), file.getBytes(), headers);
		} catch (IOException e) {
			throw new InternalException("Unable to upload image", e);
		}
		return response;
	}

	/**
	 * creating the staging area in EPS repository
	 * @param utility
	 * @param headers
	 * @return
	 */
	private HttpResponse createStagingArea(HttpUtility utility,
			Map<String, String> headers) {
		HttpResponse response;
		response = utility.makePost(ConfigurationManager.getInstance().getEPSStagingURL(), headers, ContentType.WILDCARD, null,"");
		return response;
	}

}
