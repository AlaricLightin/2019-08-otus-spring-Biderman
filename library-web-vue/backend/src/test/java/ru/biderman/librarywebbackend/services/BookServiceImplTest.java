package ru.biderman.librarywebbackend.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.biderman.librarywebbackend.domain.Book;
import ru.biderman.librarywebbackend.repositories.BookRepository;
import ru.biderman.librarywebbackend.services.exceptions.BookNotFoundException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

    @DisplayName("должен сохранять новую книгу")
    @Test
    void shouldSaveBook() {
        Book bookBeforeSave = mock(Book.class);
        Book bookAfterSave = mock(Book.class);
        when(bookRepository.save(bookBeforeSave)).thenReturn(bookAfterSave);
        assertThat(bookService.createBook(bookBeforeSave)).isEqualTo(bookAfterSave);
    }

    @DisplayName("должен обновлять книгу, если она существует")
    @Test
    void shouldUpdateBookIfExists() throws BookNotFoundException {
        final long id = 1000;
        Book bookBeforeSave = mock(Book.class);
        when(bookBeforeSave.getId()).thenReturn(id);
        Book bookAfterSave = mock(Book.class);
        when(bookRepository.existsById(id)).thenReturn(true);
        when(bookRepository.save(bookBeforeSave)).thenReturn(bookAfterSave);
        assertThat(bookService.updateBook(bookBeforeSave)).isEqualTo(bookAfterSave);
    }

    @DisplayName("должен бросать исключение при обновлении, если обновлять нечего")
    @Test
    void shouldThrowExceptionWhenUpdateIfAbsent() {
        final long id = 1000;
        Book bookBeforeSave = mock(Book.class);
        when(bookBeforeSave.getId()).thenReturn(id);
        when(bookRepository.existsById(id)).thenReturn(false);
        assertThrows(BookNotFoundException.class, () -> bookService.updateBook(bookBeforeSave));
    }

    @DisplayName("должен удалять книгу")
    @Test
    void shouldDeleteBook() throws BookNotFoundException{
        final long BOOK_ID = 100;
        when(bookRepository.existsById(BOOK_ID)).thenReturn(true);
        bookService.deleteById(BOOK_ID);
        verify(bookRepository).deleteById(BOOK_ID);
    }

    @DisplayName("должен бросать исключение при удалении, если удалять нечего")
    @Test
    void shouldThrowExceptionWhenDeleteIfAbsent() {
        final long BOOK_ID = 100;
        when(bookRepository.existsById(BOOK_ID)).thenReturn(false);
        assertThrows(BookNotFoundException.class, () -> bookService.deleteById(BOOK_ID));
    }
}