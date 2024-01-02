package com.pearson.ptb.dataaccess;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
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

	/*
	 * @Autowired private ApplicationContext applicationContext;
	 */

    @Override
    protected String getDatabaseName() {
        return dbName;
    }

    @Override
    public MongoClient mongoClient() {
        return MongoClients.create(mongoClientSettings());
    }

    @Override  // Add this annotation to explicitly indicate you are overriding the method
    protected MongoClientSettings mongoClientSettings() {
        ConnectionString connectionString = new ConnectionString("mongodb://localhost:27017/dbName");
        MongoClientSettings.Builder builder = MongoClientSettings.builder()
                .applyConnectionString(connectionString);
        // Add additional settings if needed
        return builder.build();
    }

    @Bean
    public MongoOperations mongoOperations() {
        return new MongoTemplate(new SimpleMongoClientDatabaseFactory(mongoClient(), getDatabaseName()));
    }
	/*
	 * 
	 * @Value("${spring.data.mongodb.database}") private String dbName;
	 * 
	 * 
	 * @Value("${spring.data.mongodb.uri}") private String mongoUri;
	 * 
	 * 
	 * @Autowired private ApplicationContext applicationContext;
	 * 
	 * @Override protected String getDatabaseName() { return dbName; }
	 * 
	 * @Override public MongoClient mongoClient() { return
	 * MongoClients.create(); }
	 * 
	 * 
	 * 
	 * 
	 * @Override
	 * 
	 * @Bean public MongoClientSettings mongoClientSettings() { ConnectionString
	 * connectionString = new ConnectionString(mongoUri); return
	 * MongoClientSettings.builder() .applyConnectionString(connectionString) //
	 * Add other settings as needed .build(); }
	 * 
	 * 
	 */}
