package ru.biderman.library.dao;

import ru.biderman.library.domain.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreDao {
    void addGenre(Genre genre);
    void updateGenre(Genre genre);
    void deleteGenre(long id);
    List<Genre> getAllGenres();
    Optional<Genre> getGenreById(long id);
}
