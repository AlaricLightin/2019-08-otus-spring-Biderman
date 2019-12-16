package ru.biderman.librarywebclassic.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.biderman.librarywebclassic.domain.Book;
import ru.biderman.librarywebclassic.repositories.BookRepository;
import ru.biderman.librarywebclassic.services.exceptions.BookNotFoundException;
import ru.biderman.librarywebclassic.services.exceptions.ServiceException;

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
    private AvailabilityForMinorsService availabilityForMinorsService;

    @BeforeEach
    void initBookDao() {
        bookRepository = mock(BookRepository.class);
        availabilityForMinorsService = mock(AvailabilityForMinorsService.class);
        bookService = new BookServiceImpl(bookRepository, availabilityForMinorsService);
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
    void shouldGetBookById() throws ServiceException {
        Book book = mock(Book.class);
        long BOOK_ID = 1;
        when(bookRepository.findById(BOOK_ID)).thenReturn(Optional.of(book));
        assertThat(bookService.getBookById(BOOK_ID)).isEqualTo(book);
    }

    @DisplayName("должен выбрасывать исключение, если книги нет")
    @Test
    void shouldThrowExceptionIfBookAbsent() {
        long BOOK_ID = 1;
        when(bookRepository.findById(BOOK_ID)).thenReturn(Optional.empty());
        assertThrows(BookNotFoundException.class, () -> bookService.getBookById(BOOK_ID));
    }

    @DisplayName("должен сохранять книгу")
    @Test
    void shouldSaveBook() {
        Book book = mock(Book.class);
        bookService.save(book, true);
        verify(bookRepository).save(book);
        verify(availabilityForMinorsService).setRights(book, true);
    }

    @DisplayName("должен удалять книгу")
    @Test
    void shouldDeleteBook() {
        final long BOOK_ID = 1;
        bookService.deleteById(BOOK_ID);
        verify(bookRepository).deleteById(BOOK_ID);
    }

    @DisplayName("должен возвращать количество книг")
    @Test
    void shouldGetCount() {
        final long bookCount = 20;
        when(bookRepository.count()).thenReturn(bookCount);
        assertThat(bookService.getCount()).isEqualTo(bookCount);
    }
}