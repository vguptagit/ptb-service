/*
 * package com.cb.controller;
 * 
 * import java.io.IOException;
 * 
 * import org.springframework.core.io.InputStreamResource; import
 * org.springframework.http.HttpHeaders; import
 * org.springframework.http.HttpStatus; import
 * org.springframework.http.MediaType; import
 * org.springframework.http.ResponseEntity; import
 * org.springframework.stereotype.Controller; import
 * org.springframework.ui.Model; import
 * org.springframework.web.bind.annotation.GetMapping; import
 * org.springframework.web.bind.annotation.PathVariable; import
 * org.springframework.web.bind.annotation.PostMapping; import
 * org.springframework.web.bind.annotation.RequestParam; import
 * org.springframework.web.bind.annotation.RestController; import
 * org.springframework.web.multipart.MultipartFile;
 * 
 * import com.amazonaws.services.s3.model.S3Object; import
 * com.cb.service.MetadataService;
 * 
 * import lombok.AllArgsConstructor;
 * 
 * 
 * 
 * @Controller
 * 
 * @AllArgsConstructor public class DashboardController {
 * 
 * private MetadataService metadataService;
 * 
 * @GetMapping("dashboard") public String dashboard(Model model) {
 * 
 * var files = metadataService.list(); model.addAttribute("files", files);
 * return "dashboard"; }
 * 
 * @PostMapping("upload") public String upload(
 * 
 * @RequestParam("file") MultipartFile file) throws IOException {
 * metadataService.upload(file); return "redirect:/dashboard"; }
 * 
 * @GetMapping("download/{id}") public ResponseEntity<InputStreamResource>
 * viewFile(@PathVariable int id) throws IOException {
 * System.out.println(id+"++++++++++++++++++++"); S3Object s3Object =
 * metadataService.download(id);
 * 
 * System.out.println(s3Object+"sssssssssssssssssssss");
 * 
 * String contentType = s3Object.getObjectMetadata().getContentType(); var
 * inputStream = s3Object.getObjectContent();
 * 
 * HttpHeaders headers = new HttpHeaders();
 * headers.setContentType(MediaType.valueOf(contentType));
 * headers.setContentLength(s3Object.getObjectMetadata().getContentLength());
 * 
 * return new ResponseEntity<>(new InputStreamResource(inputStream), headers,
 * HttpStatus.OK); } }
 */

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

import lombok.AllArgsConstructor;

@RestController
// @RequestMapping("/api")
@AllArgsConstructor
public class DashboardController {

	private final MetadataService metadataService;

	/*
	 * @GetMapping("/dashboard") public String dashboard() { var files =
	 * metadataService.list(); // Assuming you want to return some JSON response,
	 * you can use @ResponseBody // // annotation
	 * 
	 * return files.toString(); }
	 */

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
