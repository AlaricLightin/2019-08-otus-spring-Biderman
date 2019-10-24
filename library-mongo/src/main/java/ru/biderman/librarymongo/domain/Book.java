package ru.biderman.librarymongo.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;
import java.util.Set;

@Document(collection = "books")
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

    public Book() {
    }

    public Book(String id, List<Author> authorList, String title, Set<String> genres) {
        this.id = id;
        this.authorList = authorList;
        this.title = title;
        this.genres = genres;
    }

    public String getId() {
        return id;
    }

    public List<Author> getAuthorList() {
        return authorList;
    }

    public String getTitle() {
        return title;
    }

    public Set<String> getGenres() {
        return genres;
    }
}
