package ru.biderman.librarymongo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories(basePackages = "ru.biderman.librarymongo.repositories")
public class LibraryMongoApplication {

	public static void main(String[] args) {
		SpringApplication.run(LibraryMongoApplication.class, args);
	}

}
