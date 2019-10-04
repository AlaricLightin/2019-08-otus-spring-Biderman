package ru.biderman.library.domain;

public class Genre {
    private final long id;
    private final String title;

    public static Genre createNewGenre(String title) {
        return new Genre(-1, title);
    }

    public Genre(long id, String title) {
        this.id = id;
        this.title = title;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }
}
