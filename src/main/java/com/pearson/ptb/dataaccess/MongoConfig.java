
package com.pearson.ptb.dataaccess;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

@Configuration
@EnableMongoRepositories(basePackages = "com.pearson.mytest.dataaccess")
public class MongoConfig extends AbstractMongoClientConfiguration {

	@Value("${spring.data.mongodb.database}")
	private String dbName;

	@Value("${spring.data.mongodb.host}")
	private String host;

	@Value("${spring.data.mongodb.username}")
	private String username;

	@Value("${spring.data.mongodb.password}")
	private String password;

	@Value("${spring.data.mongodb.port}")
	private int port;

	@Override
	protected String getDatabaseName() {
		return dbName;
	}

	@Override
	public MongoClient mongoClient() {
		return MongoClients.create(mongoClientSettings());
	}

	@Override
	protected MongoClientSettings mongoClientSettings() {
		ConnectionString connectionString;
		if (host.equals("localhost")) {
			connectionString = new ConnectionString("mongodb://localhost:27017/dbName");
		} else {
			connectionString = new ConnectionString(
					"mongodb://" + username + ":" + password + "@" + host + ":" + port + "/" + dbName+"?retryWrites=false");

		}
		MongoClientSettings.Builder builder = MongoClientSettings.builder().applyConnectionString(connectionString);
		return builder.build();
	}

	@Bean
	public MongoOperations mongoOperations() {
		return new MongoTemplate(new SimpleMongoClientDatabaseFactory(mongoClient(), getDatabaseName()));
	}
}
