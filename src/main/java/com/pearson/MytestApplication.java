package com.pearson;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
//@EnableMongoRepositories(basePackages = "com.pearson.ptb.dataaccess")
// @ComponentScan({"com.pearson.ptb.dataaccess","com.pearson.ptb.controller"})
public class MytestApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(MytestApplication.class, args);
	}

}
