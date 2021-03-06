package ru.biderman.librarywebclassic.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "authors")
@Data
@NoArgsConstructor
@AllArgsConstructor
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
}
