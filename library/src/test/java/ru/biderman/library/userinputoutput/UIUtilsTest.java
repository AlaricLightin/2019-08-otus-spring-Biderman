package ru.biderman.library.userinputoutput;

import org.junit.jupiter.api.Test;
import ru.biderman.library.domain.Author;
import ru.biderman.library.domain.Book;
import ru.biderman.library.domain.Genre;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UIUtilsTest {

    @Test
    void getBookString() {
        assertEquals("1. Ivanov Ivan, \"Title\". Some genre",
                UIUtils.getBookString(
                        new Book(1, Collections.singletonList(Author.createNewAuthor("Ivanov", "Ivan")),
                                "Title",
                                Collections.singleton(Genre.createNewGenre("Some genre")))
                ));

        assertEquals("1. \"Title\". Some genre",
                UIUtils.getBookString(
                        new Book(1, Collections.emptyList(),
                                "Title",
                                Collections.singleton(Genre.createNewGenre("Some genre")))
                ));

        assertEquals("1. Ivanov Ivan, Petrov Petr, \"Title\". Some genre",
                UIUtils.getBookString(
                        new Book(1, Arrays.asList(
                                    Author.createNewAuthor("Ivanov", "Ivan"),
                                    Author.createNewAuthor("Petrov", "Petr")
                                ),
                                "Title",
                                Collections.singleton(Genre.createNewGenre("Some genre")))
                ));
    }
}