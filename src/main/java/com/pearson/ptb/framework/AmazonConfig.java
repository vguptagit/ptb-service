package com.pearson.ptb.framework;

import org.springframework.beans.factory.annotation.Value;
import com.amazonaws.auth.InstanceProfileCredentialsProvider;
import com.amazonaws.client.builder.AwsClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@Configuration
public class AmazonConfig {

//	@Value("${aws.access.key.id}")
//	private String accessKey;
//
//	@Value("${aws.secret.access.key}")
//	private String secretKey;

	@Value("${cloud.aws.region.static}")
	private String region;
	
	@Value("$cloud.aws.s3.endpoint")
	private String endpoint;

//	@Bean
//	public AmazonS3 s3() {
//	    AWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
//
//	    ClientConfiguration clientConfig = new ClientConfiguration();
//	    clientConfig.setSignerOverride("AWSS3V4SignerType");
//
//	    return AmazonS3ClientBuilder.standard()
//	            .withRegion(region)
//	            .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
//	            .withClientConfiguration(clientConfig)
//	            .build();
//	}
	
	@Bean
	public AmazonS3 s3() {
	    return AmazonS3ClientBuilder.standard()
	            .withCredentials(InstanceProfileCredentialsProvider.getInstance())
	            .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endpoint, region))
	            .build();
	}
}