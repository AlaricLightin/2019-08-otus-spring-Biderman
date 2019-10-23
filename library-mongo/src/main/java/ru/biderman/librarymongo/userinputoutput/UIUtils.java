package ru.biderman.librarymongo.userinputoutput;

import ru.biderman.librarymongo.domain.Author;
import ru.biderman.librarymongo.domain.Book;
import ru.biderman.librarymongo.domain.Comment;

import java.util.stream.Collectors;

public class UIUtils {
    public static String getAuthorString(Author author) {
        return String.format("(id: %s) %s %s", author.getId(), author.getSurname(), author.getOtherNames());
    }

    public static String getBookString(Book book) {
        String authorString = book.getAuthorList().stream()
                .map(author -> String.format("%s %s", author.getSurname(), author.getOtherNames()))
                .collect(Collectors.joining(", "));

        if (!authorString.isEmpty())
            authorString = authorString + ", ";

        String genreString = String.join(", ", book.getGenres());

        return String.format("(id: %s) %s\"%s\". %s", book.getId(), authorString, book.getTitle(), genreString);
    }

    public static String getCommentString(Comment comment) {
        return String.format("(id: %s) %tc %s: %s", comment.getId(), comment.getDateTime(), comment.getUser(), comment.getText());
    }
}
