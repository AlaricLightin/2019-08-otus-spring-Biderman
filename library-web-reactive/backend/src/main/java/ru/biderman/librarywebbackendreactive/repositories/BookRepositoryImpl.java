package ru.biderman.librarywebbackendreactive.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.biderman.librarywebbackendreactive.domain.Book;
import ru.biderman.librarywebbackendreactive.exceptions.BookNotFoundException;

public class BookRepositoryImpl implements BookRepositoryCustom {
    @Autowired
    BookRepository bookRepository;
    @Autowired
    ReactiveMongoTemplate mongoTemplate;

    @Override
    public Mono<Book> updateIfExists(Book book) {
        return bookRepository
                .existsById(book.getId())
                .flatMap(exists -> exists ? Mono.just(book) : Mono.empty())
                .flatMap(bookRepository::save)
                .switchIfEmpty(Mono.error(BookNotFoundException::new));
    }

    @Override
    public Mono<Void> deleteIfExists(String id) {
        return bookRepository
                .existsById(id)
                .flatMap(exists -> exists ? Mono.just(id) : Mono.empty())
                .switchIfEmpty(Mono.error(BookNotFoundException::new))
                .flatMap(bookRepository::deleteById);
    }

    @Override
    public Flux<String> findAllGenres() {
        return mongoTemplate
                .query(Book.class)
                .distinct("genres")
                .as(String.class)
                .all();
    }
}
