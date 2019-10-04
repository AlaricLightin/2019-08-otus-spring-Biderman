package ru.biderman.library.service;

import ru.biderman.library.domain.Book;
import ru.biderman.library.domain.Comment;

import java.util.List;

public interface BookService {
    void addBook(Book book);
    void deleteBook(Book book);
    List<Book> getAllBooks();
    Book getBookById(long id);

    void addComment(Book book, Comment comment);
    void deleteComment(Book book, long commentId);
}
