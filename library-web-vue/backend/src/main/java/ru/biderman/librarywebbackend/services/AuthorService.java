package ru.biderman.librarywebbackend.services;

import ru.biderman.librarywebbackend.domain.Author;

import java.util.List;

public interface AuthorService {
    List<Author> getAllAuthors();
}
