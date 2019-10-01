package ru.biderman.library.shell;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import ru.biderman.library.domain.Author;
import ru.biderman.library.domain.Book;
import ru.biderman.library.domain.Genre;
import ru.biderman.library.service.AuthorService;
import ru.biderman.library.service.BookService;
import ru.biderman.library.service.GenreService;
import ru.biderman.library.service.exceptions.*;
import ru.biderman.library.userinputoutput.BookReader;
import ru.biderman.library.userinputoutput.UIUtils;
import ru.biderman.library.userinputoutput.UserInterface;

import java.util.Collections;
import java.util.Map;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.*;

@DisplayName("Shell-компонент ")
class LibraryShellTest {
    private static final String RIGHT_RESULT_STRING = "Success";
    private UserInterface userInterface;
    private LibraryShell libraryShell;

    @BeforeEach
    void setUserInterface() {
        userInterface = mock(UserInterface.class);
    }

    void setRightResultMessageCode(String messageCode) {
        when(userInterface.getText(messageCode)).thenReturn(RIGHT_RESULT_STRING);
    }

    @Nested
    @DisplayName("при работе с жанрами ")
    class Genres {
        private GenreService genreService;
        private static final String GENRE_TITLE =  "Some genre";
        private static final long GENRE_ID = 1;

        @BeforeEach
        void init() {
            genreService = mock(GenreService.class);
            libraryShell = new LibraryShell(null, genreService, null, null, userInterface);
        }

        @DisplayName("должен печатать их перечень, если он не пуст")
        @Test
        void shouldPrintAll() {
            when(genreService.getAllGenres()).thenReturn(Collections.singletonMap(GENRE_ID, new Genre(GENRE_ID, GENRE_TITLE)));
            String result = libraryShell.printAllGenres();
            assertThat(result).isEqualTo(String.format("%d. %s", GENRE_ID, GENRE_TITLE));
        }

        @DisplayName("должен сообщать, если список жанров пуст")
        @Test
        void shouldPrintAllIfEmpty() {
            when(genreService.getAllGenres()).thenReturn(Collections.emptyMap());
            setRightResultMessageCode("shell.error.no-genres-found");
            assertThat(libraryShell.printAllGenres()).isEqualTo(RIGHT_RESULT_STRING);
        }

        @DisplayName("должен добавлять жанр")
        @Test
        void shouldAdd() throws ServiceException {
            setRightResultMessageCode("shell.genre-added");
            String resultString = libraryShell.addGenre(GENRE_TITLE);
            verify(genreService).addGenre(GENRE_TITLE);
            assertThat(resultString).isEqualTo(RIGHT_RESULT_STRING);
        }

        @DisplayName("должен обрабатывать исключение, что жанр нельзя добавить")
        @Test
        void shouldCatchAddException() throws ServiceException{
            setRightResultMessageCode("shell.error.add-genre-error");
            doThrow(AddGenreException.class).when(genreService).addGenre(GENRE_TITLE);
            assertThat(libraryShell.addGenre(GENRE_TITLE)).isEqualTo(RIGHT_RESULT_STRING);
        }

        @DisplayName("должен их удалять")
        @Test
        void shouldDelete() throws ServiceException {
            setRightResultMessageCode("shell.genre-deleted");
            String resultString = libraryShell.deleteGenre(GENRE_ID);
            verify(genreService).deleteGenre(GENRE_ID);
            assertThat(resultString).isEqualTo(RIGHT_RESULT_STRING);
        }

        @DisplayName("должен обрабатывать исключения, если не удалось удалить")
        @Test
        void shouldCatchDeleteException() throws ServiceException {
            setRightResultMessageCode("shell.error.delete-genre-error");
            doThrow(DeleteGenreException.class).when(genreService).deleteGenre(GENRE_ID);
            assertThat(libraryShell.deleteGenre(GENRE_ID)).isEqualTo(RIGHT_RESULT_STRING);
        }

        @DisplayName("должен обновлять жанр")
        @Test
        void shouldUpdate() throws ServiceException {
            final String NEW_TITLE = "New title";
            setRightResultMessageCode("shell.genre-updated");
            String resultString = libraryShell.updateGenre(GENRE_ID, NEW_TITLE);
            ArgumentCaptor<Genre> argumentCaptor = ArgumentCaptor.forClass(Genre.class);
            verify(genreService).updateGenre(GENRE_ID, NEW_TITLE);
            assertThat(resultString).isEqualTo(RIGHT_RESULT_STRING);
        }

        @DisplayName("должен обрабатывать исключение, если не удалось обновить")
        @Test
        void shouldCatchUpdateException() throws ServiceException {
            final String NEW_TITLE = "New title";
            setRightResultMessageCode("shell.error.update-genre-error");
            doThrow(UpdateGenreException.class).when(genreService).updateGenre(GENRE_ID, NEW_TITLE);
            assertThat(libraryShell.updateGenre(GENRE_ID, NEW_TITLE)).isEqualTo(RIGHT_RESULT_STRING);
        }
    }

    @Nested
    @DisplayName("при работе с авторами")
    class Authors {
        private AuthorService authorService;
        private static final String SURNAME = "Surname";
        private static final String NAME = "Name";
        private static final long AUTHOR_ID = 1;

        @BeforeEach
        void init() {
            authorService = mock(AuthorService.class);
            libraryShell = new LibraryShell(null, null, authorService, null, userInterface);
        }

        @DisplayName("должен печатать их перечень, если он не пуст")
        @Test
        void shouldPrintAll() {
            final long AUTHOR_ID = 1;
            when(authorService.getAllAuthors()).thenReturn(Collections.singletonMap(AUTHOR_ID, new Author(AUTHOR_ID, SURNAME, NAME)));
            String result = libraryShell.printAllAuthors();
            assertThat(result).isEqualTo(String.format("%d. %s %s", AUTHOR_ID, SURNAME, NAME));
        }

        @DisplayName("должен сообщать, если список авторов пуст")
        @Test
        void shouldPrintAllIfEmpty() {
            when(authorService.getAllAuthors()).thenReturn(Collections.emptyMap());
            setRightResultMessageCode("shell.error.no-authors-found");
            assertThat(libraryShell.printAllAuthors()).isEqualTo(RIGHT_RESULT_STRING);
        }

        @DisplayName("должен добавлять автора")
        @Test
        void shouldAdd() throws ServiceException {
            setRightResultMessageCode("shell.author-added");
            String resultString = libraryShell.addAuthor(SURNAME, NAME);
            ArgumentCaptor<Author> argument = ArgumentCaptor.forClass(Author.class);
            verify(authorService).addAuthor(argument.capture());
            assertThat(argument.getValue()).isEqualToComparingFieldByField(Author.createNewAuthor(SURNAME, NAME));
            assertThat(resultString).isEqualTo(RIGHT_RESULT_STRING);
        }

        @DisplayName("должен обрабатывать исключение, если нельзя добавить")
        @Test
        void shouldCatchCouldNotAdd() throws ServiceException{
            setRightResultMessageCode("shell.error.add-author-error");
            doThrow(AddAuthorException.class).when(authorService).addAuthor(any());
            assertThat(libraryShell.addAuthor(SURNAME, NAME)).isEqualTo(RIGHT_RESULT_STRING);
        }

        @DisplayName("должен удалять автора")
        @Test
        void shouldDelete() throws ServiceException {
            setRightResultMessageCode("shell.author-deleted");
            String resultString = libraryShell.deleteAuthor(AUTHOR_ID);
            verify(authorService).deleteAuthor(AUTHOR_ID);
            assertThat(resultString).isEqualTo(RIGHT_RESULT_STRING);
        }

        @DisplayName("должен обрабатывать исключение, если нельзя удалить")
        @Test
        void shouldCatchDeleteException() throws ServiceException {
            setRightResultMessageCode("shell.error.delete-author-error");
            doThrow(DeleteAuthorException.class).when(authorService).deleteAuthor(AUTHOR_ID);
            assertThat(libraryShell.deleteAuthor(AUTHOR_ID)).isEqualTo(RIGHT_RESULT_STRING);
        }

        @DisplayName("должен обновлять жанр")
        @Test
        void shouldUpdate() throws ServiceException {
            final String surname = "New-surname";
            final String name = "New-name";
            setRightResultMessageCode("shell.author-updated");
            String resultString = libraryShell.updateAuthor(AUTHOR_ID, surname, name);
            ArgumentCaptor<Author> argument = ArgumentCaptor.forClass(Author.class);
            verify(authorService).updateAuthor(eq(AUTHOR_ID), argument.capture());
            assertThat(argument.getValue()).isEqualToComparingFieldByField(Author.createNewAuthor(surname, name));
            assertThat(resultString).isEqualTo(RIGHT_RESULT_STRING);
        }

        @DisplayName("должен обрабатывать исключение, если не удалось обновить")
        @Test
        void shouldCatchUpdateException() throws ServiceException {
            final String surname = "New-surname";
            final String name = "New-name";
            setRightResultMessageCode("shell.error.update-author-error");
            doThrow(UpdateAuthorException.class).when(authorService).updateAuthor(eq(AUTHOR_ID), any());
            assertThat(libraryShell.updateAuthor(AUTHOR_ID, surname, name)).isEqualTo(RIGHT_RESULT_STRING);
        }
    }

    @Nested
    @DisplayName("при работе с книгами ")
    class Books {
        private BookService bookService;
        private GenreService genreService;
        private AuthorService authorService;
        private BookReader bookReader;
        private static final long BOOK_ID = 1;
        private static final String BOOK_TITLE = "Book title";
        private static final String GENRE_TITLE =  "Some genre";
        private static final long GENRE_ID = 1;
        private static final String SURNAME = "Surname";
        private static final String NAME = "Name";
        private static final long AUTHOR_ID = 1;

        @BeforeEach
        void init() {
            bookService = mock(BookService.class);
            genreService = mock(GenreService.class);
            authorService = mock(AuthorService.class);
            bookReader = mock(BookReader.class);
            libraryShell = new LibraryShell(bookService, genreService, authorService, bookReader, userInterface);
        }

        @DisplayName("должен печатать их перечень, если он не пуст")
        @Test
        void shouldPrintAll() {
            Book book = new Book(BOOK_ID,
                    Collections.singletonList(new Author(AUTHOR_ID, SURNAME, NAME)),
                    BOOK_TITLE,
                    Collections.singleton(new Genre(GENRE_ID, GENRE_TITLE)));

            when(bookService.getAllBooks()).thenReturn(Collections.singletonList(book));
            String result = libraryShell.printAllBooks();
            assertThat(result).isEqualTo(UIUtils.getBookString(book));
        }

        @DisplayName("должен сообщать, если список авторов пуст")
        @Test
        void shouldPrintAllIfEmpty() {
            when(bookService.getAllBooks()).thenReturn(Collections.emptyList());
            setRightResultMessageCode("shell.error.no-books-found");
            assertThat(libraryShell.printAllBooks()).isEqualTo(RIGHT_RESULT_STRING);
        }

        @DisplayName("должен добавлять книгу")
        @Test
        void shouldAdd() {
            Author author = new Author(AUTHOR_ID, SURNAME, NAME);
            Genre genre = new Genre(GENRE_ID, GENRE_TITLE);
            Map<Long, Genre> genreMap = Collections.singletonMap(GENRE_ID, genre);
            Map<Long, Author> authorMap = Collections.singletonMap(AUTHOR_ID, author);
            when(genreService.getAllGenres()).thenReturn(genreMap);
            when(authorService.getAllAuthors()).thenReturn(authorMap);

            Book book = Book.createNewBook(Collections.singletonList(author), BOOK_TITLE, Collections.singleton(genre));
            when(bookReader.getBook(authorMap, genreMap)).thenReturn(book);
            setRightResultMessageCode("shell.book-added");

            String resultString = libraryShell.addBook();
            verify(bookService).addBook(book);
            assertThat(resultString).isEqualTo(RIGHT_RESULT_STRING);
        }

        @DisplayName("должен удалять книгу")
        @Test
        void shouldDelete() {
            setRightResultMessageCode("shell.book-deleted");
            String resultString = libraryShell.deleteBook(BOOK_ID);
            verify(bookService).deleteBook(BOOK_ID);
            assertThat(resultString).isEqualTo(RIGHT_RESULT_STRING);
        }
    }
}