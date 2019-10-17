package ru.biderman.library.domain;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "username")
    private String user;

    @Column(name = "date_time")
    private ZonedDateTime dateTime;

    @Column(name = "comment_text")
    private String text;

    @ManyToOne(targetEntity = Book.class, optional = false)
    private Book book;

    public Comment() {
    }

    public Comment(String user, ZonedDateTime dateTime, String text, Book book) {
        this.user = user;
        this.dateTime = dateTime;
        this.text = text;
        this.book = book;
    }

    public long getId() {
        return id;
    }

    public String getUser() {
        return user;
    }

    public ZonedDateTime getDateTime() {
        return dateTime;
    }

    public String getText() {
        return text;
    }

    public Book getBook() {
        return book;
    }
}
