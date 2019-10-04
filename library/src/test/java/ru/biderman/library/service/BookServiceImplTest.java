package ru.biderman.library.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.biderman.library.dao.BookDao;
import ru.biderman.library.domain.Book;
import ru.biderman.library.domain.Comment;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@DisplayName("Сервис по работе с книгами ")
class BookServiceImplTest {
    private BookDao bookDao;
    private BookService bookService;

    @BeforeEach
    void initBookDao() {
        bookDao = mock(BookDao.class);
        bookService = new BookServiceImpl(bookDao);
    }

    @DisplayName("должен возвращать всех")
    @Test
    void shouldGetAll() {
        final List<Book> resultMap = Collections.singletonList(mock(Book.class));
        when(bookDao.getAllBooks()).thenReturn(resultMap);
        assertEquals(resultMap, bookService.getAllBooks());
    }

    @DisplayName("должен возвращать книгу по id")
    @Test
    void shouldGetBookById() {
        Book book = mock(Book.class);
        long BOOK_ID = 1;
        when(bookDao.getBookById(BOOK_ID)).thenReturn(book);
        assertThat(bookService.getBookById(BOOK_ID)).isEqualTo(book);
    }

    @DisplayName("должен добавлять книгу")
    @Test
    void shouldAddAuthor() {
        Book book = mock(Book.class);
        bookService.addBook(book);
        verify(bookDao).addBook(book);
    }

    @DisplayName("должен удалять книгу")
    @Test
    void shouldDeleteAuthor() {
        Book book = mock(Book.class);
        bookService.deleteBook(book);
        verify(bookDao).deleteBook(book);
    }

    @DisplayName("должен добавлять комментарий")
    @Test
    void shouldAddComment() {
        Book book = new Book(0, Collections.emptyList(), "Title", Collections.emptySet());
        Comment comment = new Comment("User", ZonedDateTime.now(), "Text");

        bookService.addComment(book, comment);

        verify(bookDao).updateBook(book);
        assertThat(book.getCommentList()).containsOnly(comment);
    }

    @DisplayName("должен удалять комментарий")
    @Test
    void shouldDeleteComment() {
        Book book = new Book(0, Collections.emptyList(), "Title", Collections.emptySet());
        Comment comment = new Comment("User", ZonedDateTime.now(), "Text");

        Comment spyComment = spy(comment);
        final long commentId = 1;
        when(spyComment.getId()).thenReturn(commentId);
        book.getCommentList().add(spyComment);

        bookService.deleteComment(book, commentId);

        verify(bookDao).updateBook(book);
        assertThat(book.getCommentList()).hasSize(0);
    }
}