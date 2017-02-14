package com.prototype.config;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

//http://stackoverflow.com/questions/23972002/java-8-date-time-jsr-310-types-mapping-with-spring-data-mongodb
@Configuration
@ComponentScan(basePackages = {"com.prototype.repository.event"})
public class MongoDbConfig extends AbstractMongoConfiguration {

    @Autowired
    private MongoDbFactory mongoDbFactory;

    @Override
    protected String getDatabaseName() {
        return "vadabait";
    }

    @Override
    public Mongo mongo() throws Exception {
        return new MongoClient("127.0.0.1");
    }

    @Bean
    public MongoTemplate mongoTemplate() throws Exception {

        MongoTemplate mongoTemplate = new MongoTemplate(mongoDbFactory, getDefaultMongoConverter());
        return mongoTemplate;

    }

    @Bean
    public MappingMongoConverter getDefaultMongoConverter() throws Exception {

        MappingMongoConverter converter = new MappingMongoConverter(
                new DefaultDbRefResolver(mongoDbFactory), new MongoMappingContext());
        converter.afterPropertiesSet();

        return converter;
    }
}
