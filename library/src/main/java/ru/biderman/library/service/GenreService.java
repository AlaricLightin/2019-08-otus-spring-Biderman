package ru.biderman.library.service;

import ru.biderman.library.domain.Genre;
import ru.biderman.library.service.exceptions.AddGenreException;
import ru.biderman.library.service.exceptions.DeleteGenreException;
import ru.biderman.library.service.exceptions.UpdateGenreException;

import java.util.Map;

public interface GenreService {
    void addGenre(String title) throws AddGenreException;
    void updateGenre(long id, String title) throws UpdateGenreException;
    void deleteGenre(long id) throws DeleteGenreException;
    Map<Long, Genre> getAllGenres();
}
