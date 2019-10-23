package ru.biderman.librarymongo.config;

import com.github.cloudyrock.mongock.SpringBootMongock;
import com.github.cloudyrock.mongock.SpringBootMongockBuilder;
import com.mongodb.MongoClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class MongoConfig {
    @Bean
    public SpringBootMongock mongock(ApplicationContext applicationContext,
                                 MongoClient mongoClient,
                                 @Value("${spring.data.mongodb.database}") String databaseName) {
        return (SpringBootMongock) new SpringBootMongockBuilder(mongoClient, databaseName, "ru.biderman.librarymongo.changelogs")
                .setApplicationContext(applicationContext)
                .setLockQuickConfig()
                .build();
    }

    @Bean
    public MongoCustomConversions customConversions() {
        List<Converter<?, ?>> converterList = new ArrayList<>();
        converterList.add(new MongoZonedDateTimeReadConverter());
        converterList.add(new MongoZonedDateTimeWriteConverter());
        return new MongoCustomConversions(converterList);
    }
}
