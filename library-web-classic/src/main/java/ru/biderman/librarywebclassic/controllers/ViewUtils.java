package ru.biderman.librarywebclassic.controllers;

import ru.biderman.librarywebclassic.domain.Author;
import ru.biderman.librarywebclassic.domain.Book;

import java.util.stream.Collectors;
import java.util.stream.Stream;

class ViewUtils {
    static String getAuthorString(Author author) {
        return String.format("%s %s", author.getOtherNames(), author.getSurname());
    }

    static String getBookString(Book book) {
        String authorsString = book.getAuthorList().stream()
                .map(ViewUtils::getAuthorString)
                .collect(Collectors.joining(", "));
        String titleString = !book.getTitle().isEmpty() ? String.format("\"%s\"", book.getTitle()) : "";
        return Stream.of(authorsString, titleString)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.joining(", "));
    }
}
