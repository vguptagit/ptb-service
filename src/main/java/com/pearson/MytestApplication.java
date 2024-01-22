package com.pearson;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.tags.Tag;

@SpringBootApplication
//@EnableMongoRepositories(basePackages = "com.pearson.ptb.dataaccess")
// @ComponentScan({"com.pearson.ptb.dataaccess","com.pearson.ptb.controller"})
public class MytestApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(MytestApplication.class, args);
	}

	
	@Bean
	public OpenAPI eazyShopOpenAPI() {
		List<Tag> tags = new ArrayList<>();
		Tag tag = new Tag();
		tag.setName("Archive");
		tags.add(tag);
		
		Tag tag2 = new Tag();
		tag2.setName("Authentication");
		tags.add(tag2);
		
		Tag tag3 = new Tag();
		tag3.setName("Books");
		tags.add(tag3);
		
		Tag tag4 = new Tag();
		tag4.setName("Containers");
		tags.add(tag4);
		
		Tag tag5 = new Tag();
		tag5.setName("Dashboard");
		tags.add(tag5);
		
		Tag tag6 = new Tag();
		tag6.setName("Health");
		tags.add(tag6);
		
		Tag tag7 = new Tag();
		tag7.setName("Image");
		tags.add(tag7);
		
		Tag tag8 = new Tag();
		tag8.setName("Books");
		tags.add(tag8);
		
		Tag tag9 = new Tag();
		tag9.setName("Questions");
		tags.add(tag9);
		
		Tag tag10 = new Tag();
		tag10.setName("Tests");
		tags.add(tag10);
		
		Tag tag11 = new Tag();
		tag11.setName("MyTests");
		tags.add(tag11);
		
		Tag tag12 = new Tag();
		tag12.setName("User Books");
		tags.add(tag12);
		
		Tag tag13 = new Tag();
		tag13.setName("User folders");
		tags.add(tag13);
		
		Tag tag14 = new Tag();
		tag14.setName("User Test Preferences");
		tags.add(tag14);
				
		return new OpenAPI()
				.info(new Info().title("Pearson Test Builder API")
						.description("Create Instructor tests using custom questions or published book questions")
						.version("v1.0")
						.contact(new Contact().name("Pearson").email("ptb-dev@pearson.com")
								.url("www.eazyshop.com"))
						.license(new License().name("Copyright © 2015 Pearson Education, All Rights Reserved").url("https://int-piapi.stg-openclass.com/v1/piapi-int/login/static/html/REVEL-EULA.html")))
				.externalDocs(new ExternalDocumentation()
						.description("TestBuilder")
						.url("https://testbuilder.pearsoned.com/api"))
				.tags(tags);


	}
}
