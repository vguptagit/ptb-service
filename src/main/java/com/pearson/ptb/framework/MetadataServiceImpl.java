package com.pearson.ptb.framework;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.pearson.ptb.bean.FileMeta;
import com.pearson.ptb.proxy.AmazonS3Service;
import com.pearson.ptb.proxy.MetadataService;
import com.pearson.ptb.proxy.repo.S3Repo;

@Service
public class MetadataServiceImpl implements MetadataService {

    @Autowired
    private AmazonS3Service amazonS3Service;

    @Autowired
    private S3Repo fileMetaRepository;

    @Value("${aws.s3.bucket.name}")
    private String bucketName;

    @Override
    public void upload(MultipartFile file) throws IOException {

        if (file.isEmpty())
            throw new IllegalStateException("Cannot upload empty file");

        Map<String, String> metadata = new HashMap<>();
        metadata.put("Content-Type", file.getContentType());
        metadata.put("Content-Length", String.valueOf(file.getSize()));

        String path = String.format("%s/%s", bucketName, UUID.randomUUID());
        String fileName = String.format("%s", file.getOriginalFilename());

     
        PutObjectResult putObjectResult = amazonS3Service.upload(
                path, fileName, Optional.of(metadata), file.getInputStream());
        String id= UUID.randomUUID().toString();
       
        fileMetaRepository.insert(new FileMeta(id,fileName, path, putObjectResult.getMetadata().getVersionId()));

    }

    @Override
    public ResponseEntity<InputStreamResource> download(String id) {
        FileMeta fileMeta = fileMetaRepository.findById(id);
                

        S3Object s3Object = amazonS3Service.download(fileMeta.getFilePath(), fileMeta.getFileName());

        String contentType = s3Object.getObjectMetadata().getContentType();
        var inputStream = s3Object.getObjectContent();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf(contentType));
        headers.setContentLength(s3Object.getObjectMetadata().getContentLength());

        return new ResponseEntity<>(new InputStreamResource(inputStream), headers, HttpStatus.OK);
    }
}
