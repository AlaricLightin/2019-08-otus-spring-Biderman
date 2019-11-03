package ru.biderman.librarywebclassic.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.biderman.librarywebclassic.domain.Author;
import ru.biderman.librarywebclassic.domain.Book;
import ru.biderman.librarywebclassic.domain.Genre;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий для работы с книгами ")
@DataJpaTest
class BookRepositoryTest {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @DisplayName("должен добавлять книги")
    @Test
    void shouldAddBooks() {
        final long BOOK_AUTHOR_ID = 1;
        final long BOOK_GENRE = 1;
        final String NEW_BOOK_TITLE = "New Book Title";
        Book book = Book.createNewBook(
                Collections.singletonList(new Author(BOOK_AUTHOR_ID, "X", "X")),
                NEW_BOOK_TITLE,
                Collections.singleton(new Genre(BOOK_GENRE, "X")));
        bookRepository.save(book);

        assertThat(book.getId()).isGreaterThan(0);
        Book newBook = testEntityManager.find(Book.class, book.getId());
        assertThat(newBook).isEqualToComparingFieldByField(book);
    }
}