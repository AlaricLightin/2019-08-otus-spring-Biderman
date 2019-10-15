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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.biderman.library.domain.Genre;
import ru.biderman.library.repositories.GenreRepository;
import ru.biderman.library.service.exceptions.AddGenreException;
import ru.biderman.library.service.exceptions.UpdateGenreException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.biderman.library.testutils.TestData.*;

@DisplayName("Сервис работы с жанрами (при интеграционном тесте) ")
@DataJpaTest
@ExtendWith(SpringExtension.class)
@EntityScan(basePackageClasses = Genre.class)
@Import(GenreServiceImpl.class)
class GenreServiceImplIntegrationTest {
    @Autowired
    GenreServiceImpl genreService;

    @Autowired
    GenreRepository genreRepository;

    @Autowired
    TestEntityManager testEntityManager;

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
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    void shouldThrowExceptionIfUpdateNameIsUsed() {
        assertThrows(UpdateGenreException.class, () -> genreService.updateGenre(GENRE_FOR_DELETE_ID, EXISTING_GENRE));
    }
}