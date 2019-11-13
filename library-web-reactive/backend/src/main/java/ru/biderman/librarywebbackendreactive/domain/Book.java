package ru.biderman.librarywebbackendreactive.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;
import java.util.Set;

@Document(collection = "books")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Book {
    @Id
    private String id;

    @Field("authorList")
    @DBRef
    private List<Author> authorList;

    @Field("title")
    private String title;

    @Field("genres")
    private Set<String> genres;

    public static Book createNewBook(List<Author> authorList, String title, Set<String> genreList) {
        return new Book(null, authorList, title, genreList);
    }
}
