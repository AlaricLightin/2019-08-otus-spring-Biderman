package ru.biderman.librarymongo.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.ZonedDateTime;

@Document(collection = "comments")
public class Comment {
    @Id
    private String id;

    @Field("user")
    private String user;

    @Field("dateTime")
    private ZonedDateTime dateTime;

    @Field("text")
    private String text;

    @Field("book")
    @DBRef
    private Book book;

    public Comment() {
    }

    public Comment(String user, ZonedDateTime dateTime, String text, Book book) {
        this.user = user;
        this.dateTime = dateTime;
        this.text = text;
        this.book = book;
    }

    public String getId() {
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
