package ru.biderman.librarywebbackend.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.biderman.librarywebbackend.domain.Author;

@Data
@AllArgsConstructor
public class AuthorDto {
    private long id;
    private String surname;
    private String otherNames;

    public static AuthorDto createFromAuthor(Author author) {
        return new AuthorDto(author.getId(), author.getSurname(), author.getOtherNames());
    }
}
