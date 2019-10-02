package ru.biderman.library.domain;

import javax.persistence.*;

@Entity
@Table(name = "genres")
public class Genre {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "genre_text", nullable = false, unique = true)
    private String text;

    public static Genre createNewGenre(String title) {
        return new Genre(0, title);
    }

    public Genre() {
    }

    public Genre(long id, String text) {
        this.id = id;
        this.text = text;
    }

    public long getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
