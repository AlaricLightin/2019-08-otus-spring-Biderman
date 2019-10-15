package ru.biderman.library.service;

import ru.biderman.library.domain.Book;

import java.util.List;

public interface BookService {
    void addBook(Book book);
    void deleteById(long id);
    List<Book> getAllBooks();
    Book getBookById(long id);
}
