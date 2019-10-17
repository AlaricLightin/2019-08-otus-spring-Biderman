package ru.biderman.library.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.biderman.library.domain.Author;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий для работы с авторами ")
@DataJpaTest
class AuthorRepositoryTest {
    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @DisplayName("должен добавлять автора.")
    @Test
    void shouldAddGenre() {
        final String NEW_AUTHOR_SURNAME = "Петров";
        final String NEW_AUTHOR_OTHER_NAMES = "Петров";
        Author newAuthor = Author.createNewAuthor(NEW_AUTHOR_SURNAME, NEW_AUTHOR_OTHER_NAMES);
        authorRepository.save(newAuthor);
        assertThat(newAuthor.getId()).isGreaterThan(0);

        Author author = testEntityManager.find(Author.class, newAuthor.getId());
        assertThat(author).isEqualToComparingFieldByField(newAuthor);
    }
}