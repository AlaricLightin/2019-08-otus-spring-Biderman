package ru.biderman.library.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import ru.biderman.library.dao.GenreDao;
import ru.biderman.library.domain.Genre;
import ru.biderman.library.service.exceptions.AddGenreException;
import ru.biderman.library.service.exceptions.DeleteGenreException;
import ru.biderman.library.service.exceptions.ServiceException;
import ru.biderman.library.service.exceptions.UpdateGenreException;

import javax.persistence.PersistenceException;
import java.util.Collections;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@DisplayName("Сервис по работе с жанрами ")
class GenreServiceImplTest {
    private static final String GENRE_TITLE = "Some genre";
    private final long GENRE_ID = 1;
    private GenreDao genreDao;
    private GenreService genreService;

    @BeforeEach
    void initGenreDao() {
        genreDao = mock(GenreDao.class);
        genreService = new GenreServiceImpl(genreDao);
    }

    @DisplayName("должен возвращать все")
    @Test
    void shouldGetAll() {
        final Map<Long, Genre> resultMap = Collections.singletonMap(1L, new Genre(1, GENRE_TITLE));
        when(genreDao.getAllGenres()).thenReturn(resultMap);
        assertEquals(resultMap, genreService.getAllGenres());
    }

    @DisplayName("должен добавлять жанр")
    @Test
    void shouldAddGenre() throws ServiceException {
        genreService.addGenre(GENRE_TITLE);
        ArgumentCaptor<Genre> argumentCaptor = ArgumentCaptor.forClass(Genre.class);
        verify(genreDao).addGenre(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue()).isEqualToComparingFieldByField(Genre.createNewGenre(GENRE_TITLE));
    }

    @DisplayName("должен бросать исключение, если не удаётся добавить жанр")
    @Test
    void shouldThrowExceptionIfGenreNotAdded() {
        doThrow(PersistenceException.class).when(genreDao).addGenre(any());
        assertThrows(AddGenreException.class, () -> genreService.addGenre(GENRE_TITLE));
    }

    @DisplayName("должен удалять жанр")
    @Test
    void shouldDeleteGenre() throws ServiceException{
        genreService.deleteGenre(GENRE_ID);
        verify(genreDao).deleteGenre(GENRE_ID);
    }

    @DisplayName("должен бросать исключение, если жанр удалить не удаётся")
    @Test
    void shouldThrowExceptionIfCouldNotDelete() {
        doThrow(PersistenceException.class).when(genreDao).deleteGenre(GENRE_ID);
        assertThrows(DeleteGenreException.class, () -> genreService.deleteGenre(GENRE_ID));
    }

    @DisplayName("должен обновлять жанр")
    @Test
    void shouldUpdateGenre() throws ServiceException{
        final String NEW_TITLE = "New genre";
        Genre oldGenre = new Genre(GENRE_ID, GENRE_TITLE);
        when(genreDao.getGenreById(GENRE_ID)).thenReturn(oldGenre);
        genreService.updateGenre(GENRE_ID, NEW_TITLE);
        verify(genreDao).updateGenre(oldGenre);
        assertThat(oldGenre).hasFieldOrPropertyWithValue("title", NEW_TITLE);
    }

    @DisplayName("должен бросать исключение при обновлении, если жанра нет")
    @Test
    void shouldThrowUpdateExceptionIfNoGenre() {
        final String NEW_TITLE = "New genre";
        when(genreDao.getGenreById(GENRE_ID)).thenReturn(null);
        assertThrows(UpdateGenreException.class, () -> genreService.updateGenre(GENRE_ID, NEW_TITLE));
    }

    @DisplayName("должен бросать исключение при обновлении, если обновить не удаётся")
    @Test
    void shouldThrowUpdateExceptionIfCouldNotUpdate(){
        final String NEW_TITLE = "New genre";
        Genre oldGenre = new Genre(GENRE_ID, GENRE_TITLE);
        when(genreDao.getGenreById(GENRE_ID)).thenReturn(oldGenre);
        doThrow(PersistenceException.class).when(genreDao).updateGenre(oldGenre);
        assertThrows(UpdateGenreException.class, () -> genreService.updateGenre(GENRE_ID, NEW_TITLE));
    }
}