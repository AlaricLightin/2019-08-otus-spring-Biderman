package ru.biderman.librarywebclassic.services;

import ru.biderman.librarywebclassic.domain.Book;
import ru.biderman.librarywebclassic.services.exceptions.BookNotFoundException;

import java.util.List;

public interface BookService {
    void save(Book book, boolean adultOnly);
    void deleteById(long id);
    List<Book> getAllBooks();
    Book getBookById(long id) throws BookNotFoundException;
    long getCount();
}
