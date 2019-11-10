package ru.biderman.librarywebbackend.services;

import ru.biderman.librarywebbackend.domain.Book;
import ru.biderman.librarywebbackend.services.exceptions.BookNotFoundException;

import java.util.List;

public interface BookService {
    Book createBook(Book book);
    Book updateBook(Book book) throws BookNotFoundException;
    void deleteById(long id) throws BookNotFoundException;
    List<Book> getAllBooks();
    Book getBookById(long id);
}
