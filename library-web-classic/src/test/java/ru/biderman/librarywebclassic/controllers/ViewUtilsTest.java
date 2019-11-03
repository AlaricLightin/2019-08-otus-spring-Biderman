package ru.biderman.librarywebclassic.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.biderman.librarywebclassic.domain.Author;
import ru.biderman.librarywebclassic.domain.Book;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ViewUtilsTest {
    @DisplayName("Должен возвращать строку по книге")
    @Test
    void shouldGetBookString() {
        assertEquals("Ivan Ivanov, \"Title\"",
                ViewUtils.getBookString(
                        new Book(1, Collections.singletonList(Author.createNewAuthor("Ivanov", "Ivan")),
                                "Title",
                                Collections.emptySet())
                ));

        assertEquals("\"Title\"",
                ViewUtils.getBookString(
                        new Book(1, Collections.emptyList(),
                                "Title",
                                Collections.emptySet())
                ));

        assertEquals("Ivan Ivanov, Petr Petrov, \"Title\"",
                ViewUtils.getBookString(
                        new Book(1, Arrays.asList(
                                Author.createNewAuthor("Ivanov", "Ivan"),
                                Author.createNewAuthor("Petrov", "Petr")
                        ),
                                "Title",
                                Collections.emptySet())
                ));

    }
}