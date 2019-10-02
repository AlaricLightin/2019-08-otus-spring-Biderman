package ru.biderman.library.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.biderman.library.dao.GenreDao;
import ru.biderman.library.dao.GenreDaoJpa;
import ru.biderman.library.domain.Genre;
import ru.biderman.library.service.exceptions.AddGenreException;
import ru.biderman.library.service.exceptions.UpdateGenreException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.biderman.library.testutils.TestData.*;

@DisplayName("Сервис работы с жанрами (при интеграционном тесте) ")
@DataJpaTest
@ExtendWith(SpringExtension.class)
@EntityScan(basePackageClasses = Genre.class)
@Import({GenreDaoJpa.class, GenreServiceImpl.class})
class GenreServiceImplIntegrationTest {
    @Autowired
    GenreServiceImpl genreService;

    @Autowired
    GenreDao genreDao;

    @Autowired
    TestEntityManager testEntityManager;

    @DisplayName("должен добавлять жанр")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void shouldAddGenre() throws AddGenreException {
        final String NEW_GENRE = "New genre";
        genreService.addGenre(NEW_GENRE);
        Genre genre = genreDao.getGenreByText(NEW_GENRE);
        assertThat(genre).hasFieldOrPropertyWithValue("text", NEW_GENRE);
    }

    @DisplayName("должен бросать исключение, если жанр существует")
    @Test
    void shouldThrowExceptionIfGenreExists()  {
        assertThrows(AddGenreException.class, () -> genreService.addGenre(EXISTING_GENRE));
    }

    @DisplayName("должен обновлять жанр")
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void shouldUpdateGenre() throws UpdateGenreException {
        final String NEW_GENRE = "New genre";
        genreService.updateGenre(EXISTING_GENRE_ID, NEW_GENRE);
        Genre genre = testEntityManager.find(Genre.class, EXISTING_GENRE_ID);
        assertThat(genre).hasFieldOrPropertyWithValue("text", NEW_GENRE);
    }

    @DisplayName("должен бросать исключение при редактировании, если имя занято")
    @Test
    void shouldThrowExceptionIfUpdateNameIsUsed() {
        assertThrows(UpdateGenreException.class, () -> genreService.updateGenre(GENRE_FOR_DELETE_ID, EXISTING_GENRE));
    }
}