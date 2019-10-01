package ru.biderman.library.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.biderman.library.domain.Genre;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Dao для работы с жанрами ")
@DataJpaTest
@ExtendWith(SpringExtension.class)
@EntityScan(basePackageClasses = Genre.class)
@Import(GenreDaoJpa.class)
class GenreDaoJpaTest {
    private static final String EXISTING_GENRE = "Test-genre";
    private static final String GENRE_FOR_DELETE = "Genre for delete";
    private static final long EXISTING_GENRE_ID = 1;
    private static final long GENRE_FOR_DELETE_ID = 2;
    private static final String NON_EXISTING_GENRE = "NON_EXISTING_GENRE";
    private static final long NON_EXISTING_GENRE_ID = 3;

    @Autowired
    TestEntityManager testEntityManager;

    @Autowired
    GenreDaoJpa genreDaoJpa;

    @DisplayName("должен добавлять жанр.")
    @Test
    void shouldAddGenre() throws DaoException {
        final String NEW_GENRE_Name = "Test genre 1";
        Genre newGenre = Genre.createNewGenre(NEW_GENRE_Name);
        genreDaoJpa.addGenre(newGenre);
        assertThat(newGenre.getId()).isGreaterThan(0);
        Genre genre = testEntityManager.find(Genre.class, newGenre.getId());
        assertThat(genre).isEqualToComparingFieldByField(newGenre);
    }

    @DisplayName("должен бросать исключение, если добавляемый жанр уже есть")
    @Test
    void shouldThrowExceptionIfAddingDuplicate() {
        assertThrows(DaoException.class, () -> genreDaoJpa.addGenre(Genre.createNewGenre(EXISTING_GENRE)));
    }

    @DisplayName("должен редактировать жанр")
    @Test
    void shouldUpdateGenre() throws DaoException{
        final String NEW_TITLE = "New title";
        Genre genre = new Genre(EXISTING_GENRE_ID, NEW_TITLE);
        genreDaoJpa.updateGenre(genre);
        Genre updatedGenre = testEntityManager.find(Genre.class, EXISTING_GENRE_ID);
        assertThat(updatedGenre).isEqualToComparingFieldByField(genre);
    }

    @DisplayName("должен бросать исключение, если идёт смена названия на существующее")
    @Test
    void shouldThrowExceptionIfNameExists() {
        Genre updatedGenre = new Genre(GENRE_FOR_DELETE_ID, EXISTING_GENRE);
        assertThrows(DaoException.class, () -> genreDaoJpa.updateGenre(updatedGenre));
    }

    @DisplayName("должен возвращать полный список жанров.")
    @Test
    void shouldGetAll() {
        Map<Long, Genre> genres = genreDaoJpa.getAllGenres();
        assertThat(genres.values()).extracting("title").containsOnly(EXISTING_GENRE, GENRE_FOR_DELETE);
    }

    @DisplayName("должен возвращать жанр по id.")
    @Test
    void shouldGetGenreById() {
        Genre genre = genreDaoJpa.getGenreById(EXISTING_GENRE_ID);
        assertThat(genre).hasFieldOrPropertyWithValue("title", EXISTING_GENRE);
    }

    @DisplayName("должен возвращать null, если жанра с таким id нет")
    @Test
    void shouldGetNullByAbsentId() {
        assertNull(genreDaoJpa.getGenreById(NON_EXISTING_GENRE_ID));
    }

    @DisplayName("должен возвращать жанр по названию.")
    @Test
    void shouldGetGenreByTitle() {
        Genre genre = genreDaoJpa.getGenreByTitle(EXISTING_GENRE);
        assertThat(genre).hasFieldOrPropertyWithValue("title", EXISTING_GENRE);
    }

    @DisplayName("должен возвращать null, если жанра с таким id нет")
    @Test
    void shouldGetNullByAbsentTitle() {
        assertNull(genreDaoJpa.getGenreByTitle(NON_EXISTING_GENRE));
    }

    @DisplayName("должен удалять жанр.")
    @Test
    void shouldDeleteGenre() throws DaoException {
        genreDaoJpa.deleteGenre(GENRE_FOR_DELETE_ID);
        Genre deletedGenre = testEntityManager.find(Genre.class, GENRE_FOR_DELETE_ID);
        assertNull(deletedGenre);
    }

    @DisplayName("должен бросать исключение, если жанр удалить нельзя")
    @Test
    void shouldThrowExceptionIfCouldNotDelete() {
        assertThrows(DaoException.class, () -> genreDaoJpa.deleteGenre(EXISTING_GENRE_ID));
    }
}