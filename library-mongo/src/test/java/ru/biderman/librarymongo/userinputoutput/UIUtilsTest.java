package ru.biderman.librarymongo.userinputoutput;

import org.junit.jupiter.api.Test;
import ru.biderman.librarymongo.domain.Author;
import ru.biderman.librarymongo.domain.Book;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UIUtilsTest {

    @Test
    void getBookString() {
        assertEquals("(id: 1) Ivanov Ivan, \"Title\". Some genre",
                UIUtils.getBookString(
                        new Book("1", Collections.singletonList(Author.createNewAuthor("Ivanov", "Ivan")),
                                "Title",
                                Collections.singleton("Some genre"))
                ));

        assertEquals("(id: 1) \"Title\". Some genre",
                UIUtils.getBookString(
                        new Book("1", Collections.emptyList(),
                                "Title",
                                Collections.singleton("Some genre"))
                ));

        assertEquals("(id: 1) Ivanov Ivan, Petrov Petr, \"Title\". Some genre",
                UIUtils.getBookString(
                        new Book("1", Arrays.asList(
                                    Author.createNewAuthor("Ivanov", "Ivan"),
                                    Author.createNewAuthor("Petrov", "Petr")
                                ),
                                "Title",
                                Collections.singleton("Some genre"))
                ));
    }
}