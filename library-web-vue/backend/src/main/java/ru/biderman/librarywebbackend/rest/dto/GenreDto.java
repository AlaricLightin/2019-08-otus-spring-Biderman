package ru.biderman.librarywebbackend.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.biderman.librarywebbackend.domain.Genre;

@Data
@AllArgsConstructor
public class GenreDto {
    private long id;
    private String text;

    public static GenreDto createFromGenre(Genre genre) {
        return new GenreDto(genre.getId(), genre.getText());
    }
}
