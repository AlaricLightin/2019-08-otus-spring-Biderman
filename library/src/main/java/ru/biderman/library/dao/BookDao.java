package ru.biderman.library.dao;


import ru.biderman.library.domain.Book;

import java.util.List;

public interface BookDao {
    void addBook(Book book);
    void updateBook(Book book);
    void deleteBook(Book book);
    List<Book> getAllBooks();
    Book getBookById(long id);
}
