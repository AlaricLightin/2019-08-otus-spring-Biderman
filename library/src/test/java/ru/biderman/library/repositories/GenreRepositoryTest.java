package ru.biderman.library.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.biderman.library.domain.Genre;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий для работы с жанрами ")
@DataJpaTest
@ExtendWith(SpringExtension.class)
@EntityScan(basePackageClasses = Genre.class)
class GenreRepositoryTest {
    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @DisplayName("должен добавлять жанр.")
    @Test
    void shouldAddGenre(){
        final String NEW_GENRE_Name = "Test genre 1";
        Genre newGenre = Genre.createNewGenre(NEW_GENRE_Name);
        genreRepository.save(newGenre);
        assertThat(newGenre.getId()).isGreaterThan(0);
        Genre genre = testEntityManager.find(Genre.class, newGenre.getId());
        assertThat(genre).isEqualToComparingFieldByField(newGenre);
    }
}