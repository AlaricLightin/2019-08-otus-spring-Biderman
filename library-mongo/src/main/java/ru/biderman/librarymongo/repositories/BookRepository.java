package ru.biderman.librarymongo.repositories;


import org.springframework.data.mongodb.repository.MongoRepository;
import ru.biderman.librarymongo.domain.Book;

public interface BookRepository extends MongoRepository<Book, String>, BookRepositoryCustom {
}