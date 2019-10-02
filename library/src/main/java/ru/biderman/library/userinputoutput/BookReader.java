package ru.biderman.library.userinputoutput;

import ru.biderman.library.domain.Author;
import ru.biderman.library.domain.Book;
import ru.biderman.library.domain.Genre;

import java.util.List;

public interface BookReader {
    Book getBook(List<Author> allAuthors, List<Genre> allGenres);
}
