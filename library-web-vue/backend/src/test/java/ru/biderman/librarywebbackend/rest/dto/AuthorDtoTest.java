package ru.biderman.librarywebbackend.rest.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.biderman.librarywebbackend.domain.Author;

import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("Объект AuthorDto ")
class AuthorDtoTest {
    @DisplayName("должен создаваться из Author")
    @Test
    void shouldCorrectCreateFromAuthor() {
        final long id = 100;
        final String surname = "Surname";
        final String otherNames = "Name";

        Author author = new Author(id, surname, otherNames);
        assertThat(AuthorDto.createFromAuthor(author))
                .hasFieldOrPropertyWithValue("id", id)
                .hasFieldOrPropertyWithValue("surname", surname)
                .hasFieldOrPropertyWithValue("otherNames", otherNames);
    }
}