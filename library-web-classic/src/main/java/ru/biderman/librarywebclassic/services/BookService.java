package ru.biderman.librarywebclassic.services;

import ru.biderman.librarywebclassic.domain.Book;

import java.util.List;

public interface BookService {
    void save(Book book);
    void deleteById(long id);
    List<Book> getAllBooks();
    Book getBookById(long id);
}
