package com.pearson.ptb.proxy.repo;

import org.springframework.stereotype.Repository;

import com.pearson.ptb.bean.FileMeta;
import com.pearson.ptb.dataaccess.GenericMongoRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class S3Repo {

	private final GenericMongoRepository<FileMeta,String> genericMongoRepository;
	
	
	public void insert(FileMeta fileMeta) {
		genericMongoRepository.save(fileMeta);
	}
	
	public FileMeta findById(String id) {
		return genericMongoRepository.findById(id);
	}
}
