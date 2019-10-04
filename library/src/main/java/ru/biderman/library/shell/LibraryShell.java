package ru.biderman.library.shell;

import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import ru.biderman.library.domain.Author;
import ru.biderman.library.domain.Book;
import ru.biderman.library.domain.Comment;
import ru.biderman.library.domain.Genre;
import ru.biderman.library.service.DatabaseService;
import ru.biderman.library.service.exceptions.*;
import ru.biderman.library.userinputoutput.BookReader;
import ru.biderman.library.userinputoutput.UIUtils;
import ru.biderman.library.userinputoutput.UserInterface;

import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@ShellComponent
public class LibraryShell {
    private final DatabaseService databaseService;
    private final BookReader bookReader;
    private final UserInterface userInterface;
    private String userName = null;

    public LibraryShell(DatabaseService databaseService, BookReader bookReader, UserInterface userInterface) {
        this.databaseService = databaseService;
        this.bookReader = bookReader;
        this.userInterface = userInterface;
    }

    @ShellMethod(value = "Login", key = {"login"})
    String login(String userName) {
        this.userName = userName;
        return userInterface.getText("shell.welcome", new String[]{userName});
    }

    @ShellMethod(value = "Logout", key = {"logout"})
    String logout() {
        this.userName = null;
        return userInterface.getText("shell.logout");
    }

    @ShellMethod(value = "Print all genres", key = {"print-genres"})
    String printAllGenres(){
        List<Genre> genres = databaseService.getGenreService().getAllGenres();
        if (genres.size() > 0)
            return genres.stream()
                    .map(UIUtils::getGenreString)
                    .collect(Collectors.joining("\n"));
        else
            return userInterface.getText("shell.error.no-genres-found");
    }

    @ShellMethod(value = "Add genre", key = {"add-genre"})
    String addGenre(String title) {
        try {
            databaseService.getGenreService().addGenre(title);
            return userInterface.getText("shell.genre-added");
        }
        catch (AddGenreException e) {
            return userInterface.getText("shell.error.add-genre-error");
        }
    }

    @ShellMethod(value = "Update genre", key = {"update-genre"})
    String updateGenre(long id, String title) {
        try {
            databaseService.getGenreService().updateGenre(id, title);
            return userInterface.getText("shell.genre-updated");
        }
        catch (UpdateGenreException e) {
            return userInterface.getText("shell.error.update-genre-error");
        }
    }

    @ShellMethod(value = "Delete genre", key = {"delete-genre"})
    String deleteGenre(long id) {
        try {
            databaseService.getGenreService().deleteGenre(id);
            return userInterface.getText("shell.genre-deleted");
        }
        catch (DeleteGenreException e) {
            return userInterface.getText("shell.error.delete-genre-error");
        }
    }

    @ShellMethod(value = "Print all authors", key = {"print-authors"})
    String printAllAuthors() {
        List<Author> authors = databaseService.getAuthorService().getAllAuthors();
        if (authors.size() > 0)
            return authors.stream()
                    .map(UIUtils::getAuthorString)
                    .collect(Collectors.joining("\n"));
        else
            return userInterface.getText("shell.error.no-authors-found");
    }

    @ShellMethod(value = "Add author", key = {"add-author"})
    String addAuthor(String surname, String otherNames) {
        databaseService.getAuthorService().addAuthor(Author.createNewAuthor(surname, otherNames));
        return userInterface.getText("shell.author-added");
    }

    @ShellMethod(value = "Update author", key = {"update-author"})
    String updateAuthor(long id, String surname, String otherNames) {
        try {
            databaseService.getAuthorService().updateAuthor(id, Author.createNewAuthor(surname, otherNames));
            return userInterface.getText("shell.author-updated");
        }
        catch (UpdateAuthorException e) {
            return userInterface.getText("shell.error.update-author-error");
        }
    }

    @ShellMethod(value = "Delete author", key = {"delete-author"})
    String deleteAuthor(long id) {
        try {
            databaseService.getAuthorService().deleteAuthor(id);
            return userInterface.getText("shell.author-deleted");
        }
        catch (DeleteAuthorException e) {
            return userInterface.getText("shell.error.delete-author-error");
        }
    }

    @ShellMethod(value = "Print all books", key = {"print-books"})
    String printAllBooks() {
        List<Book> books = databaseService.getBookService().getAllBooks();
        if (books.size() > 0)
            return books.stream()
                    .sorted(Comparator.comparing(Book::getId))
                    .map(UIUtils::getBookString)
                    .collect(Collectors.joining("\n"));
        else
            return userInterface.getText("shell.error.no-books-found");
    }

    @ShellMethod(value = "Add book", key = {"add-book"})
    String addBook() {
        List<Author> authors = databaseService.getAuthorService().getAllAuthors();
        List<Genre> genres = databaseService.getGenreService().getAllGenres();
        Book book = bookReader.getBook(authors, genres);
        databaseService.getBookService().addBook(book);
        return userInterface.getText("shell.book-added");
    }

    @ShellMethod(value = "Delete book", key = {"delete-book"})
    String deleteBook(long id) {
        Book book = databaseService.getBookService().getBookById(id);
        if (book != null) {
            databaseService.getBookService().deleteBook(book);
            return userInterface.getText("shell.book-deleted");
        }
        else
            return userInterface.getText("shell.error.no-such-book");
    }

    @ShellMethod(value = "Add comment to book", key = {"add-comment"})
    String addComment(long bookId, String text) {
        Book book = databaseService.getBookService().getBookById(bookId);
        if (book != null) {
            Comment comment = new Comment(userName, ZonedDateTime.now(), text);
            databaseService.getBookService().addComment(book, comment);
            return userInterface.getText("shell.comment-added");
        }
        else
            return userInterface.getText("shell.error.no-such-book");
    }

    @ShellMethod(value = "Delete comment", key = {"delete-comment"})
    String deleteComment(long bookId, long commentId ){
        Book book = databaseService.getBookService().getBookById(bookId);
        if (book != null) {
            databaseService.getBookService().deleteComment(book, commentId);
            return userInterface.getText("shell.comment-deleted");
        }
        else
            return userInterface.getText("shell.error.no-such-book");
    }

    @ShellMethod(value = "Print comments", key = {"print-comments"})
    String printComments(long bookId) {
        Book book = databaseService.getBookService().getBookById(bookId);
        if (book != null) {
            StringBuilder sb = new StringBuilder();
            sb
                    .append(UIUtils.getBookString(book))
                    .append("\n")
                    .append("======\n");

            book.getCommentList().forEach(comment -> sb.append(UIUtils.getCommentString(comment)).append("\n"));
            return sb.toString();
        }
        else
            return userInterface.getText("shell.error.no-such-book");
    }

    @ShellMethodAvailability({"logout",
            "add-genre", "update-genre", "delete-genre",
            "add-author", "update-author", "delete-author",
            "add-book", "delete-book",
            "add-comment", "delete-comment"
    })
    Availability isLoggedIn() {
        return userName != null ? Availability.available() : Availability.unavailable("you are not logged in");
    }
}
