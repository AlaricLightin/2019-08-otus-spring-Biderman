package ru.biderman.library.domain;

public class Author {
    private final long id;
    private final String surname;
    private final String otherNames;

    public static Author createNewAuthor(String surname, String otherNames) {
        return new Author(-1, surname, otherNames);
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
}
