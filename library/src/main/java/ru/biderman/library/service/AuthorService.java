package ru.biderman.library.service;

import ru.biderman.library.domain.Author;
import ru.biderman.library.service.exceptions.DeleteAuthorException;
import ru.biderman.library.service.exceptions.UpdateAuthorException;

import java.util.List;

public interface AuthorService {
    void addAuthor(Author author);
    void updateAuthor(long id, Author author) throws UpdateAuthorException;
    void deleteAuthor(long id) throws DeleteAuthorException;
    List<Author> getAllAuthors();
}
