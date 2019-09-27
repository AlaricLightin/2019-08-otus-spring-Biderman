package ru.biderman.library.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.biderman.library.domain.Author;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Dao для работы с авторами ")
@JdbcTest
@ExtendWith(SpringExtension.class)
@Import(AuthorDaoJdbc.class)
class AuthorDaoJdbcTest {
    private static final String EXISTING_AUTHOR_SURNAME1 = "Ivanov";
    private static final String EXISTING_AUTHOR_OTHER_NAMES1 = "Ivan Ivanovich";
    private static final String EXISTING_AUTHOR_SURNAME2 = "Smith";
    private static final String EXISTING_AUTHOR_OTHER_NAMES2 = "John";
    private static final String AUTHOR_FOR_DELETE_SURNAME = "ForDelete";
    private static final String AUTHOR_FOR_DELETE_OTHER_NAMES = "Author";
    private static final long EXISTING_AUTHOR_ID = 1;
    private static final long AUTHOR_FOR_DELETE_ID = 3;
    private static final long NON_EXISTENT_AUTHOR_ID = 100;

    @Autowired
    AuthorDaoJdbc authorDaoJdbc;

    @DisplayName("должен добавлять автора.")
    @Test
    void shouldAddGenre() {
        final String NEW_AUTHOR_SURNAME = "Петров";
        final String NEW_AUTHOR_OTHER_NAMES = "Петров";
        authorDaoJdbc.addAuthor(Author.createNewAuthor(NEW_AUTHOR_SURNAME, NEW_AUTHOR_OTHER_NAMES));
        Map<Long, Author> authors = authorDaoJdbc.getAllAuthors();
        assertThat(authors.values()).extracting("surname", "otherNames").containsOnly(
                tuple(EXISTING_AUTHOR_SURNAME1, EXISTING_AUTHOR_OTHER_NAMES1),
                tuple(EXISTING_AUTHOR_SURNAME2, EXISTING_AUTHOR_OTHER_NAMES2),
                tuple(AUTHOR_FOR_DELETE_SURNAME, AUTHOR_FOR_DELETE_OTHER_NAMES),
                tuple(NEW_AUTHOR_SURNAME, NEW_AUTHOR_OTHER_NAMES)
        );
    }

    @DisplayName("должен возвращать полный список авторов.")
    @Test
    void shouldGetAll() {
        Map<Long, Author> authors = authorDaoJdbc.getAllAuthors();
        assertThat(authors.values()).extracting("surname", "otherNames").containsOnly(
                tuple(EXISTING_AUTHOR_SURNAME1, EXISTING_AUTHOR_OTHER_NAMES1),
                tuple(EXISTING_AUTHOR_SURNAME2, EXISTING_AUTHOR_OTHER_NAMES2),
                tuple(AUTHOR_FOR_DELETE_SURNAME, AUTHOR_FOR_DELETE_OTHER_NAMES)
        );
    }

    @DisplayName("должен возвращать автора по id.")
    @Test
    void shouldGetAuthorById() {
        Author author = authorDaoJdbc.getAuthorById(EXISTING_AUTHOR_ID);
        assertThat(author)
                .hasFieldOrPropertyWithValue("surname", EXISTING_AUTHOR_SURNAME1)
                .hasFieldOrPropertyWithValue("otherNames", EXISTING_AUTHOR_OTHER_NAMES1);
    }

    @DisplayName("должен возвращать null, если автора нет")
    @Test
    void shouldGetNullIfAuthorAbsent() {
        assertNull(authorDaoJdbc.getAuthorById(NON_EXISTENT_AUTHOR_ID));
    }

    @DisplayName("должен редактировать автора")
    @Test
    void shouldUpdateGenre() {
        final String surname = "New-surname";
        final String name = "New-name";
        authorDaoJdbc.updateAuthor(EXISTING_AUTHOR_ID, Author.createNewAuthor(surname, name));
        Author author = authorDaoJdbc.getAuthorById(EXISTING_AUTHOR_ID);
        assertThat(author)
                .hasFieldOrPropertyWithValue("surname", surname)
                .hasFieldOrPropertyWithValue("otherNames", name);
    }


    @DisplayName("должен удалять автора.")
    @Test
    void shouldDeleteAuthor() throws DaoException {
        authorDaoJdbc.deleteAuthor(AUTHOR_FOR_DELETE_ID);
        Map<Long, Author> authors = authorDaoJdbc.getAllAuthors();
        assertThat(authors).hasSize(2);
    }

    @DisplayName("должен бросать исключение, если автора удалить нельзя")
    @Test
    void shouldThrowExceptionIfCouldNotDelete() {
        assertThrows(DaoException.class, () -> authorDaoJdbc.deleteAuthor(EXISTING_AUTHOR_ID));
    }

    @DisplayName("должен сообщать, используется ли автор")
    @ParameterizedTest
    @MethodSource("generateData")
    void shouldGetIsUsed(long id, boolean isUsed) {
        assertThat(authorDaoJdbc.isUsed(id)).isEqualTo(isUsed);
    }

    private static Stream<Arguments> generateData() {
        return Stream.of(
                Arguments.of(1, true),
                Arguments.of(2, true),
                Arguments.of(3, false)
        );
    }
}