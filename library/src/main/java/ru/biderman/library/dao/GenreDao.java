package ru.biderman.library.dao;

import ru.biderman.library.domain.Genre;

import java.util.List;

public interface GenreDao {
    void addGenre(Genre genre);
    void updateGenre(Genre genre);
    void deleteGenre(long id);
    List<Genre> getAllGenres();
    Genre getGenreById(long id);
    Genre getGenreByText(String text);
}
