package ru.biderman.librarymongo.userinputoutput;

import ru.biderman.librarymongo.domain.Author;
import ru.biderman.librarymongo.domain.Book;

import java.util.List;

public interface BookReader {
    Book getBook(List<Author> allAuthors);
}
