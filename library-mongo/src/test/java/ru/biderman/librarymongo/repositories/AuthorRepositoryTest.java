package ru.biderman.librarymongo.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.test.annotation.DirtiesContext;
import ru.biderman.librarymongo.domain.Author;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий для работы с авторами ")
@DataMongoTest
@ComponentScan("ru.biderman.librarymongo.repositories")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class AuthorRepositoryTest {
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private MongoOperations mongoOperations;

    @DisplayName("должен добавлять автора")
    @Test
    void shouldAddAuthor() {
        final String NEW_AUTHOR_SURNAME = "Петров";
        final String NEW_AUTHOR_OTHER_NAMES = "Петр";
        Author newAuthor = Author.createNewAuthor(NEW_AUTHOR_SURNAME, NEW_AUTHOR_OTHER_NAMES);
        authorRepository.save(newAuthor);
        assertThat(newAuthor.getId()).isNotNull().isNotEmpty();

        Author author = mongoOperations.findById(newAuthor.getId(), Author.class);
        assertThat(author).isEqualToComparingFieldByField(newAuthor);
    }

}