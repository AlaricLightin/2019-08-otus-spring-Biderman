package ru.biderman.library.dao;

import ru.biderman.library.domain.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorDao {
    void addAuthor(Author author);
    void updateAuthor(Author author);
    void deleteAuthor(long id);
    List<Author> getAllAuthors();
    Optional<Author> getAuthorById(long id);
}
