package ru.biderman.librarymongo.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.biderman.librarymongo.domain.Author;

public interface AuthorRepository extends MongoRepository<Author, String> {
}
