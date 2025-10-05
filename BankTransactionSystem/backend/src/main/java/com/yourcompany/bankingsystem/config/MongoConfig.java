package com.yourcompany.bankingsystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

@Configuration
public class MongoConfig extends AbstractMongoClientConfiguration {
    
    @Override
    protected String getDatabaseName() {
        return "banking_system";
    }
    
    @Bean
    public MongoTemplate mongoTemplate() {
        MongoTemplate template = new MongoTemplate(mongoClient(), getDatabaseName());
        
        // Remove _class field from documents
        MappingMongoConverter converter = (MappingMongoConverter) template.getConverter();
        converter.setTypeMapper(new DefaultMongoTypeMapper(null));
        
        return template;
    }
    
    @Bean
    public MongoMappingContext mongoMappingContext() {
        return new MongoMappingContext();
    }
}
