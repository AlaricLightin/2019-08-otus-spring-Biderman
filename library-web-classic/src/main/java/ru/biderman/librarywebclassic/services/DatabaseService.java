package ru.biderman.librarywebclassic.services;

import ru.biderman.librarywebclassic.domain.Author;
import ru.biderman.librarywebclassic.domain.Book;
import ru.biderman.librarywebclassic.domain.Genre;
import ru.biderman.librarywebclassic.services.exceptions.AuthorNotFoundException;
import ru.biderman.librarywebclassic.services.exceptions.BookNotFoundException;
import ru.biderman.librarywebclassic.services.exceptions.DeleteAuthorException;

import java.util.List;

public interface DatabaseService {
    void saveBook(Book book, boolean adultOnly);
    void deleteBookById(long id);
    List<Book> getAllBooks();
    Book getBookById(long id) throws BookNotFoundException;

    void addAuthor(Author author);
    void updateAuthor(long id, Author author) throws AuthorNotFoundException;
    void deleteAuthor(long id) throws DeleteAuthorException;
    List<Author> getAllAuthors();
    Author findAuthorById(long id) throws AuthorNotFoundException;

    List<Genre> getAllGenres();

    long getBookCount();
    long getAuthorCount();
}
