package com.pearson.ptb.bean;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "file_meta")
public class FileMeta {

	@Id
	private String id;

	private String fileName;
	private String filePath;
	private String version;

	// Constructors, getters, and setters

}
