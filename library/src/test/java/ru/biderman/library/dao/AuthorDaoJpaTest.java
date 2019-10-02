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
import ru.biderman.library.domain.Author;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.assertNull;
import static ru.biderman.library.testutils.TestData.*;

@DisplayName("Dao для работы с авторами ")
@DataJpaTest
@ExtendWith(SpringExtension.class)
@EntityScan(basePackageClasses = Author.class)
@Import(AuthorDaoJpa.class)
class AuthorDaoJpaTest {
    private static final long NON_EXISTENT_AUTHOR_ID = 4;

    @Autowired
    TestEntityManager testEntityManager;

    @Autowired
    AuthorDaoJpa authorDaoJpa;

    @DisplayName("должен добавлять автора.")
    @Test
    void shouldAddGenre() {
        final String NEW_AUTHOR_SURNAME = "Петров";
        final String NEW_AUTHOR_OTHER_NAMES = "Петров";
        Author newAuthor = Author.createNewAuthor(NEW_AUTHOR_SURNAME, NEW_AUTHOR_OTHER_NAMES);
        authorDaoJpa.addAuthor(newAuthor);
        assertThat(newAuthor.getId()).isGreaterThan(0);

        Author author = testEntityManager.find(Author.class, newAuthor.getId());
        assertThat(author).isEqualToComparingFieldByField(newAuthor);
    }

    @DisplayName("должен возвращать полный список авторов.")
    @Test
    void shouldGetAll() {
        List<Author> authors = authorDaoJpa.getAllAuthors();
        assertThat(authors).extracting("surname", "otherNames").containsOnly(
                tuple(EXISTING_AUTHOR_SURNAME1, EXISTING_AUTHOR_OTHER_NAMES1),
                tuple(EXISTING_AUTHOR_SURNAME2, EXISTING_AUTHOR_OTHER_NAMES2),
                tuple(AUTHOR_FOR_DELETE_SURNAME, AUTHOR_FOR_DELETE_OTHER_NAMES)
        );
    }

    @DisplayName("должен возвращать автора по id.")
    @Test
    void shouldGetAuthorById() {
        Author author = authorDaoJpa.getAuthorById(EXISTING_AUTHOR_ID);
        assertThat(author)
                .hasFieldOrPropertyWithValue("surname", EXISTING_AUTHOR_SURNAME1)
                .hasFieldOrPropertyWithValue("otherNames", EXISTING_AUTHOR_OTHER_NAMES1);
    }

    @DisplayName("должен возвращать null, если автора нет")
    @Test
    void shouldGetNullIfAuthorAbsent() {
        assertNull(authorDaoJpa.getAuthorById(NON_EXISTENT_AUTHOR_ID));
    }

    @DisplayName("должен редактировать автора")
    @Test
    void shouldUpdateGenre() {
        final String surname = "New-surname";
        final String name = "New-name";
        Author author = testEntityManager.find(Author.class, EXISTING_AUTHOR_ID);
        author.setSurname(surname);
        author.setOtherNames(name);

        authorDaoJpa.updateAuthor(author);

        author = testEntityManager.find(Author.class, EXISTING_AUTHOR_ID);
        assertThat(author)
                .hasFieldOrPropertyWithValue("surname", surname)
                .hasFieldOrPropertyWithValue("otherNames", name);
    }


    @DisplayName("должен удалять автора.")
    @Test
    void shouldDeleteAuthor() {
        authorDaoJpa.deleteAuthor(AUTHOR_FOR_DELETE_ID);
        Author deletedAuthor = testEntityManager.find(Author.class, AUTHOR_FOR_DELETE_ID);
        assertNull(deletedAuthor);
    }
}