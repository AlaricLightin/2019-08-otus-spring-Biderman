package ru.biderman.librarywebbackend.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.biderman.librarywebbackend.domain.Genre;
import ru.biderman.librarywebbackend.repositories.GenreRepository;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("Сервис по работе с жанрами ")
class GenreServiceImplTest {
    private static final String GENRE_TEXT = "Some genre";
    private GenreRepository genreRepository;
    private GenreService genreService;

    @BeforeEach
    void initGenreDao() {
        genreRepository = mock(GenreRepository.class);
        genreService = new GenreServiceImpl(genreRepository);
    }

    @DisplayName("должен возвращать все")
    @Test
    void shouldGetAll() {
        long GENRE_ID = 1;
        final List<Genre> genreList = Collections.singletonList(new Genre(GENRE_ID, GENRE_TEXT));
        when(genreRepository.findAll()).thenReturn(genreList);
        assertEquals(genreList, genreService.getAllGenres());
    }
}