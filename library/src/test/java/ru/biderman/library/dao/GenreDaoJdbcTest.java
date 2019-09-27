package ru.biderman.library.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.biderman.library.domain.Genre;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Dao для работы с жанрами ")
@JdbcTest
@ExtendWith(SpringExtension.class)
@Import(GenreDaoJdbc.class)
class GenreDaoJdbcTest {
    private static final String EXISTING_GENRE = "Test genre 0";
    private static final String GENRE_FOR_DELETE = "Genre for delete";
    private static final int EXISTING_GENRE_ID = 1;
    private static final int GENRE_FOR_DELETE_ID = 2;
    private static final String NON_EXISTING_GENRE = "NON_EXISTING_GENRE";
    private static final int NON_EXISTING_GENRE_ID = 3;

    @Autowired
    GenreDaoJdbc genreDaoJdbc;

    @DisplayName("должен добавлять жанр.")
    @Test
    void shouldAddGenre() throws DaoException {
        final String NEW_GENRE = "Test genre 1";
        genreDaoJdbc.addGenre(Genre.createNewGenre(NEW_GENRE));
        Map<Long, Genre> genres = genreDaoJdbc.getAllGenres();
        assertThat(genres.values()).extracting("title").containsOnly(EXISTING_GENRE, GENRE_FOR_DELETE, NEW_GENRE);
    }

    @DisplayName("должен бросать исключение, если добавляемый жанр уже есть")
    @Test
    void shouldThrowExceptionIfAddingDuplicate() {
        assertThrows(DaoException.class, () -> genreDaoJdbc.addGenre(Genre.createNewGenre(EXISTING_GENRE)));
    }

    @DisplayName("должен редактировать жанр")
    @Test
    void shouldUpdateGenre() {
        final String NEW_TITLE = "New title";
        genreDaoJdbc.updateGenre(EXISTING_GENRE_ID, NEW_TITLE);
        Genre genre = genreDaoJdbc.getGenreById(EXISTING_GENRE_ID);
        assertThat(genre).hasFieldOrPropertyWithValue("title", NEW_TITLE);
    }

    @DisplayName("должен возвращать полный список жанров.")
    @Test
    void shouldGetAll() {
        Map<Long, Genre> genres = genreDaoJdbc.getAllGenres();
        assertThat(genres.values()).extracting("title").containsOnly(EXISTING_GENRE, GENRE_FOR_DELETE);
    }

    @DisplayName("должен возвращать жанр по id.")
    @Test
    void shouldGetGenreById() {
        Genre genre = genreDaoJdbc.getGenreById(EXISTING_GENRE_ID);
        assertThat(genre).hasFieldOrPropertyWithValue("title", EXISTING_GENRE);
    }

    @DisplayName("должен возвращать null, если жанра с таким id нет")
    @Test
    void shouldGetNullByAbsentId() {
        assertNull(genreDaoJdbc.getGenreById(NON_EXISTING_GENRE_ID));
    }

    @DisplayName("должен возвращать жанр по названию.")
    @Test
    void shouldGetGenreByTitle() {
        Genre genre = genreDaoJdbc.getGenreByTitle(EXISTING_GENRE);
        assertThat(genre).hasFieldOrPropertyWithValue("title", EXISTING_GENRE);
    }

    @DisplayName("должен возвращать null, если жанра с таким id нет")
    @Test
    void shouldGetNullByAbsentTitle() {
        assertNull(genreDaoJdbc.getGenreByTitle(NON_EXISTING_GENRE));
    }

    @DisplayName("должен удалять жанр.")
    @Test
    void shouldDeleteGenre() throws DaoException {
        genreDaoJdbc.deleteGenre(GENRE_FOR_DELETE_ID);
        Map<Long, Genre> genres = genreDaoJdbc.getAllGenres();
        assertThat(genres).hasSize(1);
    }

    @DisplayName("должен бросать исключение, если жанр удалить нельзя")
    @Test
    void shouldThrowExceptionIfCouldNotDelete() {
        assertThrows(DaoException.class, () -> genreDaoJdbc.deleteGenre(EXISTING_GENRE_ID));
    }

    @DisplayName("должен сообщать, используется ли жанр")
    @ParameterizedTest
    @MethodSource("generateData")
    void shouldGetIsUsed(long id, boolean isUsed) {
        assertThat(genreDaoJdbc.isUsed(id)).isEqualTo(isUsed);
    }

    private static Stream<Arguments> generateData() {
        return Stream.of(
                Arguments.of(1, true),
                Arguments.of(2, false)
        );
    }
}