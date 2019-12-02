package ru.biderman.librarywebclassic.controllers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.biderman.librarywebclassic.domain.Author;
import ru.biderman.librarywebclassic.domain.Book;
import ru.biderman.librarywebclassic.domain.Genre;

import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
class BookDto {
    private long id;
    private List<Author> authorList;
    private String title;
    private Set<Genre> genres;
    boolean adultOnly;

    Book getBook() {
        return new Book(id, authorList, title, genres);
    }

}
