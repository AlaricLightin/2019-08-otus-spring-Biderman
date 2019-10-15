package ru.biderman.library.domain;

import javax.persistence.*;

@Entity
@Table(name = "authors")
public class Author {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "surname", nullable = false)
    private String surname;

    @Column(name = "other_names")
    private String otherNames;

    public static Author createNewAuthor(String surname, String otherNames) {
        return new Author(0, surname, otherNames);
    }

    public Author() {
    }

    public Author(long id, String surname, String otherNames) {
        this.id = id;
        this.surname = surname;
        this.otherNames = otherNames;
    }

    public long getId() {
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
