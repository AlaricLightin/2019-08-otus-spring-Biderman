package ru.biderman.librarywebclassic.services;

import ru.biderman.librarywebclassic.domain.Author;
import ru.biderman.librarywebclassic.services.exceptions.AuthorNotFoundException;
import ru.biderman.librarywebclassic.services.exceptions.DeleteAuthorException;

import java.util.List;

public interface AuthorService {
    void addAuthor(Author author);
    void updateAuthor(long id, Author author) throws AuthorNotFoundException;
    void deleteById(long id) throws DeleteAuthorException;
    List<Author> getAllAuthors();
    Author findById(long id) throws AuthorNotFoundException;
    List<Long> getUsedAuthorIdList();
    long getCount();
}
