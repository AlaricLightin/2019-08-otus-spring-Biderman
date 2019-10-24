package ru.biderman.librarymongo.shell;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import ru.biderman.librarymongo.domain.Author;
import ru.biderman.librarymongo.domain.Book;
import ru.biderman.librarymongo.domain.Comment;
import ru.biderman.librarymongo.service.AuthorService;
import ru.biderman.librarymongo.service.BookService;
import ru.biderman.librarymongo.service.CommentService;
import ru.biderman.librarymongo.service.DatabaseService;
import ru.biderman.librarymongo.service.exceptions.DeleteAuthorException;
import ru.biderman.librarymongo.service.exceptions.ServiceException;
import ru.biderman.librarymongo.service.exceptions.UpdateAuthorException;
import ru.biderman.librarymongo.userinputoutput.BookReader;
import ru.biderman.librarymongo.userinputoutput.UIUtils;
import ru.biderman.librarymongo.userinputoutput.UserInterface;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@DisplayName("Shell-компонент ")
class LibraryShellTest {
    private static final String RIGHT_RESULT_STRING = "Success";
    private UserInterface userInterface;
    private DatabaseService databaseService;
    private BookReader bookReader;
    private LibraryShell libraryShell;

    @BeforeEach
    void initShell() {
        userInterface = mock(UserInterface.class);
        databaseService = mock(DatabaseService.class);
        bookReader = mock(BookReader.class);
        libraryShell = new LibraryShell(databaseService, bookReader, userInterface);
    }

    void setRightResultMessageCode(String messageCode) {
        when(userInterface.getText(messageCode)).thenReturn(RIGHT_RESULT_STRING);
    }

    @Nested
    @DisplayName("при работе с авторами")
    class Authors {
        private AuthorService authorService;
        private static final String SURNAME = "Surname";
        private static final String NAME = "Name";
        private static final String AUTHOR_ID = "1";

        @BeforeEach
        void init() {
            authorService = mock(AuthorService.class);
            when(databaseService.getAuthorService()).thenReturn(authorService);
        }

        @DisplayName("должен печатать их перечень, если он не пуст")
        @Test
        void shouldPrintAll() {
            final String AUTHOR_ID = "1";
            when(authorService.getAllAuthors()).thenReturn(Collections.singletonList(new Author(AUTHOR_ID, SURNAME, NAME)));
            String result = libraryShell.printAllAuthors();
            assertThat(result).isEqualTo(String.format("(id: %s) %s %s", AUTHOR_ID, SURNAME, NAME));
        }

        @DisplayName("должен сообщать, если список авторов пуст")
        @Test
        void shouldPrintAllIfEmpty() {
            when(authorService.getAllAuthors()).thenReturn(Collections.emptyList());
            setRightResultMessageCode("shell.error.no-authors-found");
            assertThat(libraryShell.printAllAuthors()).isEqualTo(RIGHT_RESULT_STRING);
        }

        @DisplayName("должен добавлять автора")
        @Test
        void shouldAdd() {
            setRightResultMessageCode("shell.author-added");
            String resultString = libraryShell.addAuthor(SURNAME, NAME);
            ArgumentCaptor<Author> argument = ArgumentCaptor.forClass(Author.class);
            verify(authorService).addAuthor(argument.capture());
            assertThat(argument.getValue()).isEqualToComparingFieldByField(Author.createNewAuthor(SURNAME, NAME));
            assertThat(resultString).isEqualTo(RIGHT_RESULT_STRING);
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
        private AuthorService authorService;
        private static final String BOOK_ID = "1";
        private static final String BOOK_TITLE = "Book title";
        private static final String GENRE_TITLE =  "Some genre";
        private static final String SURNAME = "Surname";
        private static final String NAME = "Name";
        private static final String AUTHOR_ID = "1";

        @BeforeEach
        void init() {
            bookService = mock(BookService.class);
            authorService = mock(AuthorService.class);
            when(databaseService.getAuthorService()).thenReturn(authorService);
            when(databaseService.getBookService()).thenReturn(bookService);
        }

        @DisplayName("должен печатать их перечень, если он не пуст")
        @Test
        void shouldPrintAll() {
            Book book = new Book(BOOK_ID,
                    Collections.singletonList(new Author(AUTHOR_ID, SURNAME, NAME)),
                    BOOK_TITLE,
                    Collections.singleton(GENRE_TITLE));

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
            List<Author> authors = Collections.singletonList(author);
            when(authorService.getAllAuthors()).thenReturn(authors);

            Book book = Book.createNewBook(Collections.singletonList(author), BOOK_TITLE, Collections.singleton(GENRE_TITLE));
            when(bookReader.getBook(authors)).thenReturn(book);
            setRightResultMessageCode("shell.book-added");

            String resultString = libraryShell.addBook();
            verify(bookService).addBook(book);
            assertThat(resultString).isEqualTo(RIGHT_RESULT_STRING);
        }

        @DisplayName("должен удалять книгу")
        @Test
        void shouldDelete() {
            setRightResultMessageCode("shell.book-deleted");
            Book book = mock(Book.class);
            when(bookService.getBookById(BOOK_ID)).thenReturn(book);
            String resultString = libraryShell.deleteBook(BOOK_ID);
            verify(bookService).deleteById(BOOK_ID);
            assertThat(resultString).isEqualTo(RIGHT_RESULT_STRING);
        }

        @DisplayName("должен обновлять жанры")
        @Test
        void shouldUpdateGenre() {
            setRightResultMessageCode("shell.genre-updated");
            String genre1 = "Genre 1";
            String genre2 = "Genre 2";
            String resultString = libraryShell.updateGenre(genre1, genre2);
            verify(bookService).updateGenre(genre1, genre2);
            assertThat(resultString).isEqualTo(RIGHT_RESULT_STRING);
        }

        @Nested
        @DisplayName("при работе с комментариями")
        class Comments {
            private CommentService commentService;

            private static final String BOOK_ID = "1";

            @BeforeEach
            void init() {
                commentService = mock(CommentService.class);
                when(databaseService.getCommentService()).thenReturn(commentService);
            }

            @DisplayName("должен добавлять комментарий")
            @Test
            void shouldAddComment() {
                setRightResultMessageCode("shell.comment-added");
                Book book = mock(Book.class);
                when(bookService.getBookById(BOOK_ID)).thenReturn(book);
                final String COMMENT_TEXT = "Comment Text";

                String result = libraryShell.addComment(BOOK_ID, COMMENT_TEXT);
                ArgumentCaptor<Comment> argument = ArgumentCaptor.forClass(Comment.class);
                verify(commentService).addComment(argument.capture());

                assertThat(argument.getValue())
                        .hasFieldOrPropertyWithValue("text", COMMENT_TEXT)
                        .hasFieldOrPropertyWithValue("book", book);
                assertThat(result).isEqualTo(RIGHT_RESULT_STRING);
            }

            @DisplayName("должен возвращать ошибку при попытке добавить комментарий к отсутствующей книге")
            @Test
            void shouldReturnErrorIfAddCommentToAbsentBook() {
                setRightResultMessageCode("shell.error.no-such-book");
                when(bookService.getBookById(BOOK_ID)).thenReturn(null);
                assertThat(libraryShell.addComment(BOOK_ID, "")).isEqualTo(RIGHT_RESULT_STRING);
            }

            @DisplayName("должен удалять комментарий")
            @Test
            void shouldDeleteComment() {
                setRightResultMessageCode("shell.comment-deleted");
                final String COMMENT_ID = "1";
                String result = libraryShell.deleteComment(COMMENT_ID);
                verify(commentService).deleteCommentById(COMMENT_ID);
                assertThat(result).isEqualTo(RIGHT_RESULT_STRING);
            }

            @DisplayName("должен печатать комментарии по книгам")
            @Test
            void shouldPrintComments() {
                Author author = new Author(AUTHOR_ID, SURNAME, NAME);
                Book book = Book.createNewBook(Collections.singletonList(author), BOOK_TITLE, Collections.singleton(GENRE_TITLE));

                final String user = "User";
                final String commentText = "Comment text";
                Comment comment = new Comment(user, ZonedDateTime.now(), commentText, book);

                when(bookService.getBookById(BOOK_ID)).thenReturn(book);
                when(commentService.getCommentsByBook(book)).thenReturn(Collections.singletonList(comment));
                String result = libraryShell.printComments(BOOK_ID);
                assertThat(result)
                        .contains(SURNAME)
                        .contains(NAME)
                        .contains(BOOK_TITLE)
                        .contains(user)
                        .contains(commentText);
            }
        }
    }

    @DisplayName("должен проверять залогиненность")
    @Test
    void shouldTestLoginAvailability() {
        assertFalse(libraryShell.isLoggedIn().isAvailable());
        libraryShell.login("User");
        assertTrue(libraryShell.isLoggedIn().isAvailable());
        libraryShell.logout();
        assertFalse(libraryShell.isLoggedIn().isAvailable());
    }
}