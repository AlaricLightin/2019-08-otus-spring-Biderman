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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;
import static ru.biderman.library.testutils.TestData.*;

@DisplayName("Dao для работы с жанрами ")
@DataJpaTest
@ExtendWith(SpringExtension.class)
@EntityScan(basePackageClasses = Genre.class)
@Import(GenreDaoJpa.class)
class GenreDaoJpaTest {
    @Autowired
    TestEntityManager testEntityManager;

    @Autowired
    GenreDaoJpa genreDaoJpa;

    @DisplayName("должен добавлять жанр.")
    @Test
    void shouldAddGenre(){
        final String NEW_GENRE_Name = "Test genre 1";
        Genre newGenre = Genre.createNewGenre(NEW_GENRE_Name);
        genreDaoJpa.addGenre(newGenre);
        assertThat(newGenre.getId()).isGreaterThan(0);
        Genre genre = testEntityManager.find(Genre.class, newGenre.getId());
        assertThat(genre).isEqualToComparingFieldByField(newGenre);
    }

    @DisplayName("должен редактировать жанр")
    @Test
    void shouldUpdateGenre(){
        final String NEW_TEXT = "New title";
        Genre genre = new Genre(EXISTING_GENRE_ID, NEW_TEXT);
        genreDaoJpa.updateGenre(genre);
        Genre updatedGenre = testEntityManager.find(Genre.class, EXISTING_GENRE_ID);
        assertThat(updatedGenre).isEqualToComparingFieldByField(genre);
    }

    @DisplayName("должен возвращать полный список жанров.")
    @Test
    void shouldGetAll() {
        List<Genre> genres = genreDaoJpa.getAllGenres();
        assertThat(genres).extracting("text").containsOnly(EXISTING_GENRE, GENRE_FOR_DELETE);
    }

    @DisplayName("должен возвращать жанр по id.")
    @Test
    void shouldGetGenreById() {
        Genre genre = genreDaoJpa.getGenreById(EXISTING_GENRE_ID);
        assertThat(genre).hasFieldOrPropertyWithValue("text", EXISTING_GENRE);
    }

    @DisplayName("должен возвращать null, если жанра с таким id нет")
    @Test
    void shouldGetNullByAbsentId() {
        assertNull(genreDaoJpa.getGenreById(NON_EXISTING_GENRE_ID));
    }

    @DisplayName("должен возвращать жанр по названию.")
    @Test
    void shouldGetGenreByTitle() {
        Genre genre = genreDaoJpa.getGenreByText(EXISTING_GENRE);
        assertThat(genre).hasFieldOrPropertyWithValue("text", EXISTING_GENRE);
    }

    @DisplayName("должен возвращать null, если жанра с таким названием нет")
    @Test
    void shouldGetNullByAbsentTitle() {
        assertNull(genreDaoJpa.getGenreByText(NON_EXISTING_GENRE));
    }

    @DisplayName("должен удалять жанр.")
    @Test
    void shouldDeleteGenre() {
        genreDaoJpa.deleteGenre(GENRE_FOR_DELETE_ID);
        Genre deletedGenre = testEntityManager.find(Genre.class, GENRE_FOR_DELETE_ID);
        assertNull(deletedGenre);
    }
}