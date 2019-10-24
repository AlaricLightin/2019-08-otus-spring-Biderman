package ru.biderman.librarymongo.service;

import ru.biderman.librarymongo.domain.Book;

import java.util.List;

public interface BookService {
    void addBook(Book book);
    void deleteById(String id);
    List<Book> getAllBooks();
    Book getBookById(String id);
    void updateGenre(String oldGenresText, String newGenresText);
}
