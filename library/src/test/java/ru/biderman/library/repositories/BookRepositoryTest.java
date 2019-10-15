package ru.biderman.library.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.biderman.library.domain.Author;
import ru.biderman.library.domain.Book;
import ru.biderman.library.domain.Comment;
import ru.biderman.library.domain.Genre;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;
import static ru.biderman.library.testutils.TestData.EXISTING_BOOK_ID;
import static ru.biderman.library.testutils.TestData.EXISTING_COMMENT_ID;

@DisplayName("Репозиторий для работы с книгами ")
@DataJpaTest
@ExtendWith(SpringExtension.class)
@EntityScan(basePackages = "ru.biderman.library.domain")
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

    @DisplayName("должен удалять книги с комментариями")
    @Test
    void shouldDeleteWithComments() {
        bookRepository.deleteByIdWithComments(EXISTING_BOOK_ID);
        Book deletedBook = testEntityManager.find(Book.class, EXISTING_BOOK_ID);
        assertNull(deletedBook);

        Comment comment = testEntityManager.find(Comment.class, EXISTING_COMMENT_ID);
        assertNull(comment);
    }
}