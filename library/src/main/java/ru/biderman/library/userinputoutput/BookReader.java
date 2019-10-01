package ru.biderman.library.userinputoutput;

import org.springframework.stereotype.Service;
import ru.biderman.library.domain.Author;
import ru.biderman.library.domain.Book;
import ru.biderman.library.domain.Genre;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;

/**
 * Класс для ввода информации о книге
 */
@Service
public class BookReader {
    private final UserInterface userInterface;

    public BookReader(UserInterface userInterface) {
        this.userInterface = userInterface;
    }

    public Book getBook(Map<Long, Author> authorMap, Map<Long, Genre> genreMap) {
        userInterface.printText("shell.all-authors-prompt");
        ArrayList<Author> authors = new ArrayList<>();
        while (true) {
            Optional<Author> author = userInterface.readValue(new AuthorInputUI(authorMap), "");
            if (!author.map(authors::add).orElse(false))
                break;
        }

        String title = userInterface.readValue(new BookTitleInputUI(), null).orElse("");

        userInterface.printText("shell.all-genres-prompt");
        ArrayList<Genre> genres = new ArrayList<>();
        while(true) {
            Optional<Genre> genre = userInterface.readValue(new GenreInputUI(genreMap), "");
            if (!genre.map(genres::add).orElse(false))
                break;
        }

        return Book.createNewBook(authors, title, new HashSet<>(genres));
    }
}
