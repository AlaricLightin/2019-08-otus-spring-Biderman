package ru.biderman.librarywebclassic.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.biderman.librarywebclassic.domain.Book;
import ru.biderman.librarywebclassic.repositories.BookRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@DisplayName("Сервис по работе с книгами ")
class BookServiceImplTest {
    private BookRepository bookRepository;
    private BookService bookService;

    @BeforeEach
    void initBookDao() {
        bookRepository = mock(BookRepository.class);
        bookService = new BookServiceImpl(bookRepository);
    }

    @DisplayName("должен возвращать всех")
    @Test
    void shouldGetAll() {
        final List<Book> books = Collections.singletonList(mock(Book.class));
        when(bookRepository.findAll()).thenReturn(books);
        assertEquals(books, bookService.getAllBooks());
    }

    @DisplayName("должен возвращать книгу по id")
    @Test
    void shouldGetBookById() {
        Book book = mock(Book.class);
        long BOOK_ID = 1;
        when(bookRepository.findById(BOOK_ID)).thenReturn(Optional.of(book));
        assertThat(bookService.getBookById(BOOK_ID)).isEqualTo(book);
    }

    @DisplayName("должен сохранять книгу")
    @Test
    void shouldSaveBook() {
        Book book = mock(Book.class);
        bookService.save(book);
        verify(bookRepository).save(book);
    }

    @DisplayName("должен удалять книгу")
    @Test
    void shouldDeleteBook() {
        final long BOOK_ID = 1;
        bookService.deleteById(BOOK_ID);
        verify(bookRepository).deleteById(BOOK_ID);
    }
}