package ru.biderman.library.userinputoutput;

import org.springframework.stereotype.Service;
import ru.biderman.library.domain.Author;
import ru.biderman.library.domain.Book;
import ru.biderman.library.domain.Genre;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Класс для ввода информации о книге
 */
@Service
public class BookReaderImpl implements BookReader {
    private final UserInterface userInterface;

    public BookReaderImpl(UserInterface userInterface) {
        this.userInterface = userInterface;
    }

    @Override
    public Book getBook(List<Author> allAuthors, List<Genre> allGenres) {
        Map<Long, Genre> genreMap = allGenres.stream()
                .collect(Collectors.toMap(Genre::getId, Function.identity()));
        Map<Long, Author> authorMap = allAuthors.stream()
                .collect(Collectors.toMap(Author::getId, Function.identity()));

        userInterface.printText("shell.all-authors-prompt");
        ArrayList<Author> resultAuthorList = new ArrayList<>();
        while (true) {
            Optional<Author> author = userInterface.readValue(new AuthorInputUI(authorMap), "");
            if (!author.map(resultAuthorList::add).orElse(false))
                break;
        }

        String title = userInterface.readValue(new BookTitleInputUI(), null).orElse("");

        userInterface.printText("shell.all-genres-prompt");
        ArrayList<Genre> resultGenreList = new ArrayList<>();
        while(true) {
            Optional<Genre> genre = userInterface.readValue(new GenreInputUI(genreMap), "");
            if (!genre.map(resultGenreList::add).orElse(false))
                break;
        }

        return Book.createNewBook(resultAuthorList, title, new HashSet<>(resultGenreList));
    }
}
