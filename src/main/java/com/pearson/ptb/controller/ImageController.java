package com.pearson.ptb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.pearson.ptb.service.ImageService;

import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * To upload the image to eps
 *
 */
@Controller
@Tag(name = "Image", description = "Image APIs")
public class ImageController extends BaseController {

	@Autowired()
	@Qualifier("imageService")
	private ImageService imageService;

	/**
	 * To upload the image
	 * 
	 * @param file imagefile
	 * @return image stored path, url
	 */
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Success") })
	@Operation(summary = "returns the path of saved the image", description = "returns the path of saved the image")

	@RequestMapping(value = "/image/upload", method = RequestMethod.POST)
	@ResponseBody
	public String upload(@RequestParam("file") MultipartFile file) {
		return imageService.uploadImage(file);
	}
}
