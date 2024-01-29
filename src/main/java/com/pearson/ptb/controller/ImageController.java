package com.pearson.ptb.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.pearson.ptb.proxy.MetadataService;
import com.pearson.ptb.service.ImageService;

import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

/**
 * To upload the image to eps
 *
 */
@RestController
@RequiredArgsConstructor
@Tag(name = "Image", description = "Image APIs")
public class ImageController extends BaseController {

	/*
	 * @Autowired()
	 * 
	 * @Qualifier("imageService") private ImageService imageService;
	 */

	private final MetadataService metadataService;
	/**
	 * To upload the image
	 * 
	 * @param file imagefile
	 * @return image stored path, url
	 * @throws IOException 
	 */
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Success") })
	@Operation(summary = "returns the path of saved the image", description = "returns the path of saved the image")

	@RequestMapping(value = "/image/upload", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<com.pearson.ptb.response.ApiResponse> upload(@RequestParam("file") MultipartFile file) throws IOException {
		metadataService.upload(file);
		com.pearson.ptb.response.ApiResponse build = com.pearson.ptb.response.ApiResponse.builder().message("file saved successfully").status(HttpStatus.CREATED).code(201).build();
		return new ResponseEntity<>(build, HttpStatus.CREATED);
		
	}
}
