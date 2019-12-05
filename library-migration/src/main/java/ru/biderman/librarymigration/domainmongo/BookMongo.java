package ru.biderman.librarymigration.domainmongo;

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
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookMongo {
    @Id
    private String id;

    @Field("authorList")
    @DBRef
    private List<AuthorMongo> authorList;

    @Field("title")
    private String title;

    @Field("genres")
    private Set<String> genres;
}
