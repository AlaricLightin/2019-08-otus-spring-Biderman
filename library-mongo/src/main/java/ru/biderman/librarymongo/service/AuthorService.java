package ru.biderman.librarymongo.service;

import ru.biderman.librarymongo.domain.Author;
import ru.biderman.librarymongo.service.exceptions.DeleteAuthorException;
import ru.biderman.librarymongo.service.exceptions.UpdateAuthorException;

import java.util.List;

public interface AuthorService {
    void addAuthor(Author author);
    void updateAuthor(String id, Author author) throws UpdateAuthorException;
    void deleteAuthor(String id) throws DeleteAuthorException;
    List<Author> getAllAuthors();
}
