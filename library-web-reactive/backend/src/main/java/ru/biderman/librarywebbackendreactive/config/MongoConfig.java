package ru.biderman.librarywebbackendreactive.config;

import com.github.cloudyrock.mongock.SpringBootMongock;
import com.github.cloudyrock.mongock.SpringBootMongockBuilder;
import com.mongodb.MongoClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MongoConfig {
    @Bean
    public SpringBootMongock mongock(ApplicationContext applicationContext,
                                     MongoClient mongoClient,
                                     @Value("${spring.data.mongodb.database}") String databaseName) {
        return new SpringBootMongockBuilder(mongoClient, databaseName,
                "ru.biderman.librarywebbackendreactive.changelogs")
                .setApplicationContext(applicationContext)
                .setLockQuickConfig()
                .build();
    }
}
