package ru.biderman.librarywebclassic.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import ru.biderman.librarywebclassic.domain.Author;
import ru.biderman.librarywebclassic.domain.Book;
import ru.biderman.librarywebclassic.domain.Genre;
import ru.biderman.librarywebclassic.services.exceptions.AuthorNotFoundException;
import ru.biderman.librarywebclassic.services.exceptions.BookNotFoundException;
import ru.biderman.librarywebclassic.services.exceptions.DeleteAuthorException;
import ru.biderman.librarywebclassic.services.exceptions.ServiceException;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@DisplayName("Общий сервис работы с БД ")
class DatabaseServiceImplTest {
    private BookService bookService;
    private AuthorService authorService;
    private GenreService genreService;
    private DatabaseServiceImpl databaseService;

    @BeforeEach
    void initServices() {
        bookService = mock(BookService.class);
        authorService = mock(AuthorService.class);
        genreService = mock(GenreService.class);

        databaseService = new DatabaseServiceImpl(bookService, authorService, genreService);
    }

    @Nested
    @DisplayName("при работе с книгами ")
    class Books {
        @DisplayName("должен сохранять книгу")
        @Test
        void shouldAdd() {
            Book book = mock(Book.class);
            boolean adultOnly = true;
            databaseService.saveBook(book, adultOnly);
            verify(bookService).save(book, adultOnly);
        }

        @DisplayName("должен удалять книгу по id")
        @Test
        void shouldDeleteById() {
            final long bookId = 100;
            databaseService.deleteBookById(bookId);
            verify(bookService).deleteById(bookId);
        }

        @DisplayName("должен возвращать все книги")
        @Test
        void shouldGetAll() {
            Book book1 = mock(Book.class);
            Book book2 = mock(Book.class);
            List<Book> books = Arrays.asList(book1, book2);
            when(bookService.getAllBooks()).thenReturn(books);
            assertThat(databaseService.getAllBooks())
                    .containsOnly(book1, book2);
        }

        @DisplayName("должен возвращать книгу по id")
        @Test
        void shouldGetById() throws BookNotFoundException {
            final long bookId = 100;
            Book book = mock(Book.class);
            when(bookService.getBookById(bookId)).thenReturn(book);
            assertThat(databaseService.getBookById(bookId)).isEqualTo(book);
        }
    }

    @Nested
    @DisplayName("при работе с авторами")
    class Authors {
        @DisplayName("должен добавлять автора")
        @Test
        void shouldAdd() {
            Author author = mock(Author.class);
            databaseService.addAuthor(author);
            verify(authorService).addAuthor(author);
        }

        @DisplayName("должен обновлять автора")
        @Test
        void shouldUpdate() throws ServiceException {
            final long id = 100;
            Author author = mock(Author.class);
            databaseService.updateAuthor(id, author);
            verify(authorService).updateAuthor(id, author);
        }

        @DisplayName("должен возвращать исключение, если не удаётся обновить автора")
        @Test
        void shouldThrowExceptionIfCouldNotUpdate() throws ServiceException {
            final long id = 100;
            Author author = mock(Author.class);
            doThrow(AuthorNotFoundException.class).when(authorService).updateAuthor(id, author);
            assertThrows(AuthorNotFoundException.class, () -> databaseService.updateAuthor(id, author));
        }

        @DisplayName("должен возвращать всех авторов")
        @Test
        void shouldGetAll() {
            Author author1 = mock(Author.class);
            Author author2 = mock(Author.class);
            List<Author> authors = Arrays.asList(author1, author2);
            when(authorService.getAllAuthors()).thenReturn(authors);
            assertThat(databaseService.getAllAuthors())
                    .containsOnly(author1, author2);
        }

        @DisplayName("должен удалять автора по id")
        @Test
        void shouldDeleteById() throws ServiceException {
            final long id = 100;
            databaseService.deleteAuthor(id);
            verify(authorService).deleteById(id);
        }

        @DisplayName("должен бросать исключение, если не удалось удалить автора")
        @Test
        void shouldThrowExceptionIfCouldNotDelete() throws ServiceException{
            final long id = 100;
            doThrow(DeleteAuthorException.class).when(authorService).deleteById(id);
            assertThrows(DeleteAuthorException.class, () -> databaseService.deleteAuthor(id));
        }

        @DisplayName("должен возвращать автора по id")
        @Test
        void shouldGetById() throws ServiceException{
            final long id = 100;
            Author author = mock(Author.class);
            when(authorService.findById(id)).thenReturn(author);
            assertThat(databaseService.findAuthorById(id)).isEqualTo(author);
        }

        @DisplayName("должен возвращать исключение, если не удаётся найти автора")
        @Test
        void shouldThrowExceptionIfCouldNotFind() throws Exception{
            final long id = 100;
            doThrow(AuthorNotFoundException.class).when(authorService).findById(id);
            assertThrows(AuthorNotFoundException.class, () -> databaseService.findAuthorById(id));
        }
    }

    @Nested
    @DisplayName("при работе с жанрами ")
    class Genres{
        @DisplayName("должен возвращать все жанры")
        @Test
        void shouldGetAll() {
            Genre genre1 = mock(Genre.class);
            Genre genre2 = mock(Genre.class);
            List<Genre> genres = Arrays.asList(genre1, genre2);
            when(genreService.getAllGenres()).thenReturn(genres);
            assertThat(databaseService.getAllGenres()).containsOnly(genre1, genre2);
        }
    }
}