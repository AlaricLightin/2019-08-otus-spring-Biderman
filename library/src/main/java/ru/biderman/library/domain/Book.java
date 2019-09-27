package ru.biderman.library.domain;

import java.util.List;

public class Book {
    private final long id;
    private final List<Author> authorList;
    private final String title;
    private final List<Genre> genreList;

    public static Book createNewBook(List<Author> authorList, String title, List<Genre> genreList) {
        return new Book(-1, authorList, title, genreList);
    }

    public Book(long id, List<Author> authorList, String title, List<Genre> genreList) {
        this.id = id;
        this.authorList = authorList;
        this.title = title;
        this.genreList = genreList;
    }

    public long getId() {
        return id;
    }

    public List<Author> getAuthorList() {
        return authorList;
    }

    public String getTitle() {
        return title;
    }

    public List<Genre> getGenreList() {
        return genreList;
    }
}
