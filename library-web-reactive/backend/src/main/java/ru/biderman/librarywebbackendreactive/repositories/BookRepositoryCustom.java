package ru.biderman.librarywebbackendreactive.repositories;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.biderman.librarywebbackendreactive.domain.Book;

public interface BookRepositoryCustom {
    Mono<Book> updateIfExists(Book book);
    Mono<Void> deleteIfExists(String id);
    Flux<String> findAllGenres();
}
