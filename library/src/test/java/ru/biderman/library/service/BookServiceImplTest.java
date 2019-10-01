package ru.biderman.library.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.biderman.library.dao.BookDao;
import ru.biderman.library.domain.Book;

import java.util.Collections;
import java.util.List;

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
        final long BOOK_ID = 1;
        bookService.deleteBook(BOOK_ID);
        verify(bookDao).deleteBook(BOOK_ID);
    }



}