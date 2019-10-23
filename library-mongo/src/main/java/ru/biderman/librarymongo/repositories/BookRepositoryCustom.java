package ru.biderman.librarymongo.repositories;

public interface BookRepositoryCustom {
    void updateGenre(String oldGenreText, String newGenreText);
    boolean isAuthorUsed(String authorId);
}
