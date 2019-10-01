package ru.biderman.library.shell;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.shell.Shell;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.biderman.library.domain.Author;
import ru.biderman.library.domain.Book;
import ru.biderman.library.domain.Genre;
import ru.biderman.library.userinputoutput.UIUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DisplayName("Shell-компонент (при интеграционном тесте) ")
class LibraryShellIntegrationTest {
    private static final String EXISTING_AUTHOR_SURNAME1 = "Ivanov";
    private static final String EXISTING_AUTHOR_OTHER_NAMES1 = "Ivan Ivanovich";
    private static final String EXISTING_AUTHOR_SURNAME2 = "Smith";
    private static final String EXISTING_AUTHOR_OTHER_NAMES2 = "John";
    private static final String AUTHOR_FOR_DELETE_SURNAME = "ForDelete";
    private static final String AUTHOR_FOR_DELETE_OTHER_NAMES = "Author";
    private static final int EXISTING_AUTHOR_ID = 1;
    private static final int AUTHOR_FOR_DELETE_ID = 3;
    private static final String EXISTING_BOOK_NAME = "Book Name";
    private static final String EXISTING_GENRE = "Test-genre";
    private static final String GENRE_FOR_DELETE = "Genre for delete";
    private static final int EXISTING_GENRE_ID = 1;
    private static final int GENRE_FOR_DELETE_ID = 2;

    private static final String PRINT_AUTHORS_COMMAND = "print-authors";
    private static final String PRINT_BOOKS_COMMAND = "print-books";
    private static final String PRINT_GENRES_COMMAND = "print-genres";
    private static final String DELETE_GENRE_COMMAND = "delete-genre %d";
    private static final String ADD_GENRE_COMMAND = "add-genre %s";


    @Autowired
    Shell shell;

    @Autowired
    MessageSource messageSource;

    private static final Locale locale = new Locale("en_US");

    private String getMessage(String messageCode) {
        return messageSource.getMessage(messageCode, null, locale);
    }

    @DisplayName("должен возвращать список авторов")
    @Test
    void shouldGetAllAuthors() {
        String res = (String) shell.evaluate(() -> PRINT_AUTHORS_COMMAND);
        assertThat(res).isEqualTo(String.join("\n",
                UIUtils.getAuthorString(new Author(EXISTING_AUTHOR_ID, EXISTING_AUTHOR_SURNAME1, EXISTING_AUTHOR_OTHER_NAMES1)),
                UIUtils.getAuthorString(new Author(2, EXISTING_AUTHOR_SURNAME2, EXISTING_AUTHOR_OTHER_NAMES2)),
                UIUtils.getAuthorString(new Author(AUTHOR_FOR_DELETE_ID, AUTHOR_FOR_DELETE_SURNAME, AUTHOR_FOR_DELETE_OTHER_NAMES))));
    }

    @DisplayName("должен возвращать список книг")
    @Test
    void shouldGetAllBooks() {
        String res = (String) shell.evaluate(() -> PRINT_BOOKS_COMMAND);
        assertThat(res).isEqualTo(UIUtils.getBookString(
                new Book(1,
                        Arrays.asList(
                                new Author(EXISTING_AUTHOR_ID, EXISTING_AUTHOR_SURNAME1, EXISTING_AUTHOR_OTHER_NAMES1),
                                new Author(2, EXISTING_AUTHOR_SURNAME2, EXISTING_AUTHOR_OTHER_NAMES2)),
                        EXISTING_BOOK_NAME,
                        Collections.singleton(new Genre(EXISTING_GENRE_ID, EXISTING_GENRE)
                        ))));
    }

    @DisplayName("должен возвращать список жанров")
    @Test
    void shouldGetAllGenres() {
        String res = (String) shell.evaluate(() -> PRINT_GENRES_COMMAND);
        assertThat(res).isEqualTo(String.join("\n",
                UIUtils.getGenreString(new Genre(EXISTING_GENRE_ID, EXISTING_GENRE)),
                UIUtils.getGenreString(new Genre(GENRE_FOR_DELETE_ID, GENRE_FOR_DELETE))));
    }

    @DisplayName("должен удалять неиспользуемый жанр")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void shouldDeleteUnusedGenre() {
        String res = (String) shell.evaluate(() -> String.format(DELETE_GENRE_COMMAND, GENRE_FOR_DELETE_ID));
        assertThat(res).isEqualTo(getMessage("shell.genre-deleted"));

        res = (String) shell.evaluate(() -> PRINT_GENRES_COMMAND);
        assertThat(res).isEqualTo(UIUtils.getGenreString(new Genre(EXISTING_GENRE_ID, EXISTING_GENRE)));
    }

    @DisplayName("не должен удалять используемый жанр")
    @Test
    void shouldNotDeleteUsedGenre() {
        String res = (String) shell.evaluate(() -> String.format(DELETE_GENRE_COMMAND, EXISTING_GENRE_ID));
        assertThat(res).isEqualTo(getMessage("shell.error.delete-genre-error"));

        res = (String) shell.evaluate(() -> PRINT_GENRES_COMMAND);
        assertThat(res).isEqualTo(String.join("\n",
                UIUtils.getGenreString(new Genre(EXISTING_GENRE_ID, EXISTING_GENRE)),
                UIUtils.getGenreString(new Genre(GENRE_FOR_DELETE_ID, GENRE_FOR_DELETE))));
    }

    @DisplayName("не должен добавлять существующий жанр")
    @Test
    void shouldNotAddExistingGenre() {
        String res = (String) shell.evaluate(() -> String.format(ADD_GENRE_COMMAND, EXISTING_GENRE));
        assertThat(res).isEqualTo(getMessage("shell.error.add-genre-error"));
    }
}