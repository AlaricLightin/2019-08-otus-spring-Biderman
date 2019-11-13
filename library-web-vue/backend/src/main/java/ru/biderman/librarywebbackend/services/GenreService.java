package ru.biderman.librarywebbackend.services;

import ru.biderman.librarywebbackend.domain.Genre;

import java.util.List;

public interface GenreService {
    List<Genre> getAllGenres();
}
