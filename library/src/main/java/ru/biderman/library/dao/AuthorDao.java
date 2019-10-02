package ru.biderman.library.dao;

import ru.biderman.library.domain.Author;

import java.util.List;

public interface AuthorDao {
    void addAuthor(Author author);
    void updateAuthor(Author author);
    void deleteAuthor(long id);
    List<Author> getAllAuthors();
    Author getAuthorById(long id);
}
