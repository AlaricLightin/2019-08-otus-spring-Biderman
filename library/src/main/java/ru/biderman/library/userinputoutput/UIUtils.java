package ru.biderman.library.userinputoutput;

import ru.biderman.library.domain.Author;
import ru.biderman.library.domain.Book;
import ru.biderman.library.domain.Comment;
import ru.biderman.library.domain.Genre;

import java.util.stream.Collectors;

public class UIUtils {
    public static String getAuthorString(Author author) {
        return String.format("%d. %s %s", author.getId(), author.getSurname(), author.getOtherNames());
    }

    public static String getGenreString(Genre genre) {
        return String.format("%d. %s", genre.getId(), genre.getText());
    }

    public static String getBookString(Book book) {
        String authorString = book.getAuthorList().stream()
                .map(author -> String.format("%s %s", author.getSurname(), author.getOtherNames()))
                .collect(Collectors.joining(", "));

        if (!authorString.isEmpty())
            authorString = authorString + ", ";

        String genreString = book.getGenres().stream()
                .map(Genre::getText)
                .collect(Collectors.joining(", "));

        return String.format("%d. %s\"%s\". %s", book.getId(), authorString, book.getTitle(), genreString);
    }

    public static String getCommentString(Comment comment) {
        return String.format("(id:%d) %tc %s: %s", comment.getId(), comment.getDateTime(), comment.getUser(), comment.getText());
    }
}
