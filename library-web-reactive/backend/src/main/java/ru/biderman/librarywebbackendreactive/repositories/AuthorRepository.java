package ru.biderman.librarywebbackendreactive.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import ru.biderman.librarywebbackendreactive.domain.Author;

public interface AuthorRepository extends ReactiveMongoRepository<Author, String> {
}
