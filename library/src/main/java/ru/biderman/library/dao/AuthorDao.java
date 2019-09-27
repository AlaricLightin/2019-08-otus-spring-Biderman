package ru.biderman.library.dao;

import ru.biderman.library.domain.Author;

import java.util.Map;

public interface AuthorDao {
    void addAuthor(Author author);
    void updateAuthor(long id, Author author);
    void deleteAuthor(long id) throws DaoException;
    Map<Long, Author> getAllAuthors();
    Author getAuthorById(long id);
    boolean isUsed(long id);
}
