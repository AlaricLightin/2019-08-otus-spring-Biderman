package ru.biderman.librarywebbackendreactive.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import ru.biderman.librarywebbackendreactive.domain.Book;

public interface BookRepository extends ReactiveMongoRepository<Book, String>, BookRepositoryCustom {
}