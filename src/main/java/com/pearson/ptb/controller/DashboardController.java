

package com.pearson.ptb.controller;

import java.io.IOException;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.pearson.ptb.proxy.MetadataService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;

@Tag(name = "Dashboard")
@RestController

@AllArgsConstructor
public class DashboardController {

	private final MetadataService metadataService;

	

	@PostMapping("/upload")
	public String upload(@RequestParam("file") MultipartFile file) throws IOException {
		metadataService.upload(file);
		return "File uploaded successfully!";

	}

	@GetMapping("/download/{id}")
	public ResponseEntity<InputStreamResource> viewFile(@PathVariable String id) throws IOException {
		return metadataService.download(id);
	}
}
