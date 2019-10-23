package ru.biderman.librarymongo.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "authors")
public class Author {
    @Id
    private String id;

    private String surname;

    private String otherNames;

    public static Author createNewAuthor(String surname, String otherNames) {
        return new Author(null, surname, otherNames);
    }

    public Author() {
    }

    public Author(String id, String surname, String otherNames) {
        this.id = id;
        this.surname = surname;
        this.otherNames = otherNames;
    }

    public String getId() {
        return id;
    }

    public String getSurname() {
        return surname;
    }

    public String getOtherNames() {
        return otherNames;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setOtherNames(String otherNames) {
        this.otherNames = otherNames;
    }
}
