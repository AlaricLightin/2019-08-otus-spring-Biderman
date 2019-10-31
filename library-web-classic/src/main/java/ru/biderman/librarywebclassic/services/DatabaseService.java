package ru.biderman.librarywebclassic.services;

import ru.biderman.librarywebclassic.domain.Author;
import ru.biderman.librarywebclassic.domain.Book;
import ru.biderman.librarywebclassic.domain.Genre;
import ru.biderman.librarywebclassic.services.exceptions.AuthorNotFoundException;
import ru.biderman.librarywebclassic.services.exceptions.DeleteAuthorException;

import java.util.List;

public interface DatabaseService {
    void saveBook(Book book);
    void deleteBookById(long id);
    List<Book> getAllBooks();
    Book getBookById(long id);

    void addAuthor(Author author);
    void updateAuthor(long id, Author author) throws AuthorNotFoundException;
    void deleteAuthor(long id) throws DeleteAuthorException;
    List<Author> getAllAuthors();
    Author findAuthorById(long id) throws AuthorNotFoundException;

    List<Genre> getAllGenres();
}
