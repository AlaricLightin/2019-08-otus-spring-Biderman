package ru.biderman.librarymongo.shell;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.shell.Shell;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.biderman.librarymongo.domain.Book;
import ru.biderman.librarymongo.domain.Comment;

import java.util.List;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.biderman.librarymongo.testutils.TestData.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DisplayName("Shell-компонент (при интеграционном тесте) ")
class LibraryShellIntegrationTest {
    private static final String PRINT_AUTHORS_COMMAND = "print-authors";
    private static final String PRINT_BOOKS_COMMAND = "print-books";
    private static final String PRINT_GENRES_COMMAND = "print-genres";
    private static final String ADD_COMMENT_COMMAND = "add-comment %s %s";
    private static final String PRINT_COMMENT_COMMAND = "print-comments %s";
    private static final String DELETE_COMMENT_COMMAND = "delete-comment %s";
    private static final String DELETE_BOOK_COMMAND = "delete-book %s";
    private static final String LOGIN_COMMAND = "login %s";

    private static final String USER = "User";

    @Autowired
    Shell shell;

    @Autowired
    MessageSource messageSource;

    @Autowired
    MongoOperations mongoOperations;

    private static final Locale locale = new Locale("en_US");

    private String getMessage(String messageCode) {
        return messageSource.getMessage(messageCode, null, locale);
    }

    private String getExistingBookId() {
        Query query = new Query();
        query.addCriteria(Criteria.where("title").is(BOOK_WITH_COMMENT_TITLE));
        Book book = mongoOperations.findOne(query, Book.class);
        assertThat(book).isNotNull();
        return book.getId();
    }

    @DisplayName("должен возвращать список авторов")
    @Test
    void shouldGetAllAuthors() {
        String res = (String) shell.evaluate(() -> PRINT_AUTHORS_COMMAND);
        assertThat(res)
                .contains(ILF_SURNAME + " " + ILF_NAME)
                .contains(PETROV_SURNAME + " " + PETROV_NAME)
                .contains(PUSHKIN_SURNAME + " " + PUSHKIN_NAME);
    }

    @DisplayName("должен возвращать список книг")
    @Test
    void shouldGetAllBooks() {
        String res = (String) shell.evaluate(() -> PRINT_BOOKS_COMMAND);
        assertThat(res)
                .contains(BOOK_WITH_COMMENT_TITLE)
                .contains(ILF_SURNAME + " " + ILF_NAME)
                .contains(PETROV_SURNAME + " " + PETROV_NAME)
                .contains(PUSHKIN_SURNAME + " " + PUSHKIN_NAME);
    }

    @DisplayName("должен добавлять комментарий")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void shouldAddComment() {
        String bookId = getExistingBookId();
        shell.evaluate(() -> String.format(LOGIN_COMMAND, USER));
        final String commentText = "123456789";
        String res = (String) shell.evaluate(() -> String.format(ADD_COMMENT_COMMAND, bookId, commentText));
        assertThat(res).isEqualTo(getMessage("shell.comment-added"));

        res = (String) shell.evaluate(() -> String.format(PRINT_COMMENT_COMMAND, bookId));
        assertThat(res)
                .contains(commentText)
                .contains(USER);
    }

    @DisplayName("должен удалять комментарий")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void shouldDeleteComment() {
        List<Comment> comment = mongoOperations.findAll(Comment.class);
        assertThat(comment).hasSize(1);

        shell.evaluate(() -> String.format(LOGIN_COMMAND, USER));
        String res = (String) shell.evaluate(() -> String.format(DELETE_COMMENT_COMMAND, comment.get(0).getId()));
        assertThat(res).isEqualTo(getMessage("shell.comment-deleted"));

        res = (String) shell.evaluate(() -> String.format(PRINT_COMMENT_COMMAND, getExistingBookId()));
        assertThat(res).doesNotContain(EXISTING_COMMENT_TEXT);
    }

    @DisplayName("должен удалять книгу")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void shouldDeleteBook() {
        shell.evaluate(() -> String.format(LOGIN_COMMAND, USER));
        String res = (String) shell.evaluate(() -> String.format(DELETE_BOOK_COMMAND, getExistingBookId()));
        assertThat(res).isEqualTo(getMessage("shell.book-deleted"));

        res = (String) shell.evaluate(() -> PRINT_BOOKS_COMMAND);
        assertThat(res)
                .contains(PUSHKIN_SURNAME + " " + PUSHKIN_NAME)
                .doesNotContain(BOOK_WITH_COMMENT_TITLE);
    }
}