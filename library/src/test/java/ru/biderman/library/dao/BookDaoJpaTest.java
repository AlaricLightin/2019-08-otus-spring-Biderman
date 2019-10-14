package ru.biderman.library.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.biderman.library.domain.Author;
import ru.biderman.library.domain.Book;
import ru.biderman.library.domain.Comment;
import ru.biderman.library.domain.Genre;
import ru.biderman.library.testutils.TestData;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;
import static ru.biderman.library.testutils.TestData.EXISTING_BOOK_ID;
import static ru.biderman.library.testutils.TestData.EXISTING_BOOK_TITLE;

@DisplayName("Dao для работы с книгами ")
@DataJpaTest
@ExtendWith(SpringExtension.class)
@EntityScan(basePackages = "ru.biderman.library.domain")
@Import({BookDaoJpa.class, CommentDaoJpa.class})
class BookDaoJpaTest {
    private static final long BOOK_AUTHOR1_ID = 1;
    private static final long BOOK_AUTHOR2_ID = 2;
    private static final long BOOK_GENRE = 1;

    @Autowired
    BookDaoJpa bookDaoJpa;

    @Autowired
    TestEntityManager testEntityManager;

    @DisplayName("должен получать книги по id")
    @Test
    void shouldGetBookById() {
        Book book = bookDaoJpa.getBookById(EXISTING_BOOK_ID);
        assertThat(book)
                .hasFieldOrPropertyWithValue("id", EXISTING_BOOK_ID)
                .hasFieldOrPropertyWithValue("title", EXISTING_BOOK_TITLE)
                .satisfies(book1 ->
                        assertThat(book1.getAuthorList())
                                .extracting("id")
                                .containsOnly(BOOK_AUTHOR1_ID, BOOK_AUTHOR2_ID))
                .satisfies(book1 -> assertThat(book1.getGenres())
                        .extracting("id")
                        .containsOnly(BOOK_GENRE));
    }

    @DisplayName("должен возвращать null, если книги нет")
    @Test
    void shouldGetNullIfBookAbsent() {
        final long NON_EXISTING_BOOK_ID = 100;
        assertNull(bookDaoJpa.getBookById(NON_EXISTING_BOOK_ID));
    }

    @DisplayName("должен возвращать все книги")
    @Test
    void shouldGetAllBooks() {
        List<Book> books = bookDaoJpa.getAllBooks();
        assertThat(books).hasSize(1);
        Book book = books.get(0);
        assertThat(book)
                .hasFieldOrPropertyWithValue("id", EXISTING_BOOK_ID)
                .hasFieldOrPropertyWithValue("title", EXISTING_BOOK_TITLE)
                .satisfies(book1 ->
                        assertThat(book1.getAuthorList())
                                .extracting("id")
                                .containsOnly(BOOK_AUTHOR1_ID, BOOK_AUTHOR2_ID))
                .satisfies(book1 -> assertThat(book1.getGenres())
                        .extracting("id")
                        .containsOnly(BOOK_GENRE));
    }

    @DisplayName("должен удалять книгу по id")
    @Test
    void shouldDeleteBookById() {
        bookDaoJpa.deleteById(EXISTING_BOOK_ID);
        Book deletedBook = testEntityManager.find(Book.class, EXISTING_BOOK_ID);
        assertNull(deletedBook);

        Comment comment = testEntityManager.find(Comment.class, TestData.EXISTING_COMMENT_ID);
        assertNull(comment);
    }

    @DisplayName("должен добавлять книгу")
    @Test
    void shouldAddBook() {
        final String NEW_BOOK_TITLE = "New Book Title";
        Book book = Book.createNewBook(
                Collections.singletonList(new Author(BOOK_AUTHOR1_ID, "X", "X")),
                NEW_BOOK_TITLE,
                Collections.singleton(new Genre(BOOK_GENRE, "X")));
        bookDaoJpa.addBook(book);

        assertThat(book.getId()).isGreaterThan(0);
        Book newBook = testEntityManager.find(Book.class, book.getId());
        assertThat(newBook).isEqualToComparingFieldByField(book);
    }
}