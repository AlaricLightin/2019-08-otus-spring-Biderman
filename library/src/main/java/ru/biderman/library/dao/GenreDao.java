package ru.biderman.library.dao;

import ru.biderman.library.domain.Genre;

import java.util.Map;

public interface GenreDao {
    void addGenre(Genre genre) throws DaoException;
    void updateGenre(Genre genre) throws DaoException;
    void deleteGenre(long id) throws DaoException;
    Map<Long, Genre> getAllGenres();
    Genre getGenreById(long id);
    Genre getGenreByTitle(String title);
}
