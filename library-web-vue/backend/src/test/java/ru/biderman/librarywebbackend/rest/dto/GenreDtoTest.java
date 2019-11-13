package ru.biderman.librarywebbackend.rest.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.biderman.librarywebbackend.domain.Genre;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Объект GenreDto ")
class GenreDtoTest {
    @DisplayName("должен создаваться из Genre")
    @Test
    void shouldCreateFromGenre() {
        final long id = 100;
        final String text = "Text";
        Genre genre = new Genre(id, text);

        assertThat(GenreDto.createFromGenre(genre))
                .hasFieldOrPropertyWithValue("id", id)
                .hasFieldOrPropertyWithValue("text", text);
    }
}