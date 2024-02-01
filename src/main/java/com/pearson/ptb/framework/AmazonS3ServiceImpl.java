package com.pearson.ptb.framework;

import java.io.InputStream;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
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

	@Override
	public PutObjectResult upload(String fileId, String fileName, Optional<Map<String, String>> optionalMetaData,
			InputStream inputStream) {
		ObjectMetadata objectMetadata = new ObjectMetadata();
		optionalMetaData.ifPresent(map -> {
			if (!map.isEmpty()) {
				map.forEach(objectMetadata::addUserMetadata);
			}
		});
		log.debug("FileId: " + fileId + ", FileName:" + fileName);
		return amazonS3.putObject(bucketName, fileId, inputStream, objectMetadata);
	}

	public S3Object download(String path, String fileName) {
		return amazonS3.getObject(path, fileName);
	}
}
