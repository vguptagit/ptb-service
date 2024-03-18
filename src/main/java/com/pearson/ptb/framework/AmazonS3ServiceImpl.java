package com.pearson.ptb.framework;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.pearson.ptb.proxy.AmazonS3Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AmazonS3ServiceImpl implements AmazonS3Service {

	@Autowired
	private AmazonS3 amazonS3;

	@Value("${aws.s3.bucket.name}")
	private String bucketName;
	
//	private Logger log= LoggerFactory.getLogger(AmazonS3ServiceImpl.class);

	@Override
	public PutObjectResult upload(String fileId, String fileName, Optional<Map<String, String>> optionalMetaData,
			InputStream inputStream) {
		ObjectMetadata objectMetadata = new ObjectMetadata();
		optionalMetaData.ifPresent(map -> {
			if (!map.isEmpty()) {
				map.forEach(objectMetadata::addUserMetadata);
			}
		}); long contentLength = 0;
        try {
            contentLength = inputStream.available();
        } catch (IOException e) {
            log.error("Error determining content length of InputStream: {}", e.getMessage());
        }

        // Set the content length in the object metadata
        objectMetadata.setContentLength(contentLength);

        log.debug("FileId: {}, FileName: {}", fileId, fileName);

        // Upload the object to Amazon S3 with the specified content length
        
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, fileId, inputStream, objectMetadata);
        return amazonS3.putObject(putObjectRequest);
    }

	public S3Object download(String path, String fileName) {
		return amazonS3.getObject(path, fileName);
	}
}
