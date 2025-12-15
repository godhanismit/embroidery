package com.example.demo.config;

import com.mongodb.client.MongoClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
public class MongoConfig {

    @Bean
    public MongoTemplate mongoTemplate() {
        return new MongoTemplate(
                MongoClients.create("mongodb+srv://Smit:26062005@cluster0.jcsxoog.mongodb.net/ChalanApp?retryWrites=true&w=majority"),
                "ChalanApp"
        );
    }
}
