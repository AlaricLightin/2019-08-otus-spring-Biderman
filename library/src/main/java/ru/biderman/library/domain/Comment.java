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

    @Column(name = "user")
    private String user;

    @Column(name = "date_time")
    private ZonedDateTime dateTime;

    @Column(name = "comment_text")
    private String text;

    public Comment() {
    }

    public Comment(String user, ZonedDateTime dateTime, String text) {
        this.user = user;
        this.dateTime = dateTime;
        this.text = text;
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
}
