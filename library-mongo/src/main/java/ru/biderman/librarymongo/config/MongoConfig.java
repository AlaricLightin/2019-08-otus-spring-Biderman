package ru.biderman.librarymongo.config;

import com.github.cloudyrock.mongock.Mongock;
import com.github.cloudyrock.mongock.SpringBootMongockBuilder;
import com.mongodb.MongoClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class MongoConfig {
    private final ApplicationContext applicationContext;
    private final MongoClient mongoClient;
    private final String databaseName;

    public MongoConfig(ApplicationContext applicationContext,
                       MongoClient mongoClient,
                       @Value("${spring.data.mongodb.database}")String databaseName) {
        this.applicationContext = applicationContext;
        this.mongoClient = mongoClient;
        this.databaseName = databaseName;
    }

    @PostConstruct
    public void initDBWithMongock() {
        Mongock mongock = new SpringBootMongockBuilder(mongoClient, databaseName, "ru.biderman.librarymongo.changelogs")
                .setApplicationContext(applicationContext)
                .setLockQuickConfig()
                .build();
        mongock.execute();
    }

    @Bean
    public MongoCustomConversions customConversions() {
        List<Converter<?, ?>> converterList = new ArrayList<>();
        converterList.add(new MongoZonedDateTimeReadConverter());
        converterList.add(new MongoZonedDateTimeWriteConverter());
        return new MongoCustomConversions(converterList);
    }
}
