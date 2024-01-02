package com.pearson.ptb.proxy;

import java.io.IOException;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface MetadataService {
    public void upload(MultipartFile file) throws IOException;
    public ResponseEntity<InputStreamResource> download(String id);

    //   public S3Object download(String id);
    //public List<FileMeta> list();

}
