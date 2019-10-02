package ru.biderman.library.dao;

import ru.biderman.library.domain.Genre;

import java.util.Map;

public interface GenreDao {
    void addGenre(Genre genre);
    void updateGenre(Genre genre);
    void deleteGenre(long id);
    Map<Long, Genre> getAllGenres();
    Genre getGenreById(long id);
    Genre getGenreByTitle(String title);
}
