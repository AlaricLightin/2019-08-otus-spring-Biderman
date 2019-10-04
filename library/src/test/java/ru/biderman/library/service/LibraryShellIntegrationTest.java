package ru.biderman.library.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.shell.Input;
import org.springframework.shell.InputProvider;
import org.springframework.shell.ResultHandler;
import org.springframework.shell.Shell;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.biderman.library.domain.Author;
import ru.biderman.library.domain.Book;
import ru.biderman.library.domain.Genre;
import ru.biderman.library.userinputoutput.UIUtils;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
    private static final String EXISTING_GENRE = "Test genre 0";
    private static final String GENRE_FOR_DELETE = "Genre for delete";
    private static final int EXISTING_GENRE_ID = 1;
    private static final int GENRE_FOR_DELETE_ID = 2;


    @Autowired
    LibraryShell libraryShell;

    @DisplayName("должен возвращать список жанров")
    @Test
    void shouldGetAllGenres() {
        assertEquals(String.join("\n",
                UIUtils.getGenreString(new Genre(EXISTING_GENRE_ID, EXISTING_GENRE)),
                UIUtils.getGenreString(new Genre(GENRE_FOR_DELETE_ID, GENRE_FOR_DELETE))
        ), libraryShell.printAllGenres());
    }

    @DisplayName("должен возвращать список авторов")
    @Test
    void shouldGetAllAuthors() {
        assertEquals(String.join("\n",
                UIUtils.getAuthorString(new Author(EXISTING_AUTHOR_ID, EXISTING_AUTHOR_SURNAME1, EXISTING_AUTHOR_OTHER_NAMES1)),
                UIUtils.getAuthorString(new Author(2, EXISTING_AUTHOR_SURNAME2, EXISTING_AUTHOR_OTHER_NAMES2)),
                UIUtils.getAuthorString(new Author(AUTHOR_FOR_DELETE_ID, AUTHOR_FOR_DELETE_SURNAME, AUTHOR_FOR_DELETE_OTHER_NAMES))
                ), libraryShell.printAllAuthors());
    }

    @DisplayName("должен возвращать список книг")
    @Test
    void shouldGetAllBooks() {
        assertEquals(UIUtils.getBookString(
                new Book(1,
                        Arrays.asList(
                                new Author(EXISTING_AUTHOR_ID, EXISTING_AUTHOR_SURNAME1, EXISTING_AUTHOR_OTHER_NAMES1),
                                new Author(2, EXISTING_AUTHOR_SURNAME2, EXISTING_AUTHOR_OTHER_NAMES2)),
                        EXISTING_BOOK_NAME,
                        Collections.singletonList(new Genre(EXISTING_GENRE_ID, EXISTING_GENRE)
                        ))
        ), libraryShell.printAllBooks());
    }
}