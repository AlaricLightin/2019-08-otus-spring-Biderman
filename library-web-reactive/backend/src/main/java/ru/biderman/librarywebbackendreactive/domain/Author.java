package ru.biderman.librarywebbackendreactive.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "authors")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Author {
    @Id
    private String id;

    @Field("surname")
    private String surname;

    @Field("otherNames")
    private String otherNames;

    public static Author createNewAuthor(String surname, String otherNames) {
        return new Author(null, surname, otherNames);
    }
}
