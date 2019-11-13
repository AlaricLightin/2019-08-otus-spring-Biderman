package ru.biderman.librarywebbackendreactive.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.test.annotation.DirtiesContext;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.biderman.librarywebbackendreactive.domain.Book;
import ru.biderman.librarywebbackendreactive.exceptions.BookNotFoundException;

import java.util.ArrayList;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@ComponentScan({"ru.biderman.librarywebbackendreactive.repositories",
        "ru.biderman.librarywebbackendreactive.config"})
@DisplayName("Репозиторий по работе с книгами ")
class BookRepositoryTest {
    @Autowired
    BookRepository bookRepository;

    @Autowired
    MongoOperations mongoOperations;

    @Nested
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    class DoIfExists{
        private String existingBookId;

        @BeforeEach
        void initExistingBookId() {
            existingBookId = mongoOperations.findAll(Book.class).get(0).getId();
        }

        @DisplayName("должен удалять существующую книгу")
        @Test
        void shouldDeleteIfExists() {
            int startBookCount = 2;
            bookRepository.deleteIfExists(existingBookId).block();

            assertThat(mongoOperations.findAll(Book.class)).hasSize(startBookCount - 1);
        }

        @DisplayName("должен редактировать существующую книгу")
        @Test
        void shouldUpdateIfExists() {
            String title = "New title";
            Book newBook = new Book(existingBookId, Collections.emptyList(), title, Collections.emptySet());
            Mono<Book> bookMono = bookRepository.updateIfExists(newBook);

            StepVerifier.create(bookMono)
                    .assertNext(b -> assertThat(b).isEqualToComparingFieldByField(newBook))
                    .expectComplete()
                    .verify();
        }
    }

    @DisplayName("должен выдавать ошибку при удалении несуществующей книги")
    @Test
    void shouldThrowExceptionIfDeleteNonExisting() {
        Mono<Void> voidMono = bookRepository.deleteIfExists("absentId");

        StepVerifier.create(voidMono)
                .expectError(BookNotFoundException.class)
                .verify();
    }

    @DisplayName("должен выдавать ошибку при редактировании несуществующей книги")
    @Test
    void shouldThrowExceptionIfUpdateNonExisting() {
        Book newBook = new Book("absentId", Collections.emptyList(), "Title", Collections.emptySet());
        Mono<Book> bookMono = bookRepository.updateIfExists(newBook);

        StepVerifier.create(bookMono)
                .expectError(BookNotFoundException.class)
                .verify();
    }

    @DisplayName("должен возвращать все жанры")
    @Test
    void shouldGetAllGenres() {
        String[] existingGenres = {"Приключения", "Поэзия", "Роман"};
        Flux<String> genresFlux = bookRepository.findAllGenres();

        StepVerifier.create(genresFlux)
                .recordWith(ArrayList::new)
                .thenConsumeWhile(x -> true)
                .consumeRecordedWith(strings -> assertThat(strings).containsOnly(existingGenres))
                .verifyComplete();
    }
}