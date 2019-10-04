package ru.biderman.library.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.biderman.library.domain.Author;
import ru.biderman.library.domain.Book;
import ru.biderman.library.domain.Genre;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Dao для работы с книгами ")
@JdbcTest
@ExtendWith(SpringExtension.class)
@Import({BookDaoJdbc.class, AuthorDaoJdbc.class, GenreDaoJdbc.class})
class BookDaoJdbcTest {
    private static final long EXISTING_BOOK_ID = 1;
    private static final String EXISTING_BOOK_NAME = "Book Name";
    private static final long BOOK_AUTHOR1_ID = 1;
    private static final long BOOK_AUTHOR2_ID = 2;
    private static final long BOOK_GENRE = 1;

    @Autowired
    BookDaoJdbc bookDaoJdbc;

    @DisplayName("должен получать книги по id")
    @Test
    void shouldGetBookById() {
        Book book = bookDaoJdbc.getBookById(EXISTING_BOOK_ID);
        assertThat(book)
                .hasFieldOrPropertyWithValue("id", EXISTING_BOOK_ID)
                .hasFieldOrPropertyWithValue("title", EXISTING_BOOK_NAME)
                .satisfies(book1 ->
                        assertThat(book1.getAuthorList())
                                .extracting("id")
                                .containsOnly(BOOK_AUTHOR1_ID, BOOK_AUTHOR2_ID))
                .satisfies(book1 -> assertThat(book1.getGenreList())
                        .extracting("id")
                        .containsOnly(BOOK_GENRE));
    }

    @DisplayName("должен возвращать все книги")
    @Test
    void shouldGetAllBooks() {
        List<Book> books = bookDaoJdbc.getAllBooks();
        assertThat(books).hasSize(1);
        Book book = books.get(0);
        assertThat(book)
                .hasFieldOrPropertyWithValue("id", EXISTING_BOOK_ID)
                .hasFieldOrPropertyWithValue("title", EXISTING_BOOK_NAME)
                .satisfies(book1 ->
                        assertThat(book1.getAuthorList())
                                .extracting("id")
                                .containsOnly(BOOK_AUTHOR1_ID, BOOK_AUTHOR2_ID))
                .satisfies(book1 -> assertThat(book1.getGenreList())
                        .extracting("id")
                        .containsOnly(BOOK_GENRE));
    }

    @DisplayName("должен удалять книгу по id")
    @Test
    void shouldDeleteBookById() {
        bookDaoJdbc.deleteBook(EXISTING_BOOK_ID);
        List<Book> books = bookDaoJdbc.getAllBooks();
        assertThat(books).hasSize(0);
    }

    @DisplayName("должен добавлять книгу")
    @Test
    void shouldAddBook() {
        final String NEW_BOOK_TITLE = "New Book Title";
        Book book = Book.createNewBook(
                Collections.singletonList(new Author(BOOK_AUTHOR1_ID, "X", "X")),
                NEW_BOOK_TITLE,
                Collections.singletonList(new Genre(BOOK_GENRE, "X")));
        bookDaoJdbc.addBook(book);

        List<Book> books = bookDaoJdbc.getAllBooks();
        assertThat(books).hasSize(2);

        Book newBook = books.stream().filter(b -> EXISTING_BOOK_ID != b.getId()).findFirst().orElse(null);
        assertThat(newBook)
                .hasFieldOrPropertyWithValue("title", NEW_BOOK_TITLE)
                .satisfies(book1 ->
                        assertThat(book1.getAuthorList())
                                .extracting("id")
                                .containsOnly(BOOK_AUTHOR1_ID))
                .satisfies(book1 -> assertThat(book1.getGenreList())
                        .extracting("id")
                        .containsOnly(BOOK_GENRE));
    }
}