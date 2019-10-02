package ru.biderman.library.shell;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.biderman.library.domain.Author;
import ru.biderman.library.domain.Book;
import ru.biderman.library.domain.Genre;
import ru.biderman.library.service.AuthorService;
import ru.biderman.library.service.BookService;
import ru.biderman.library.service.GenreService;
import ru.biderman.library.service.exceptions.*;
import ru.biderman.library.userinputoutput.BookReader;
import ru.biderman.library.userinputoutput.UIUtils;
import ru.biderman.library.userinputoutput.UserInterface;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@ShellComponent
public class LibraryShell {
    private final BookService bookService;
    private final GenreService genreService;
    private final AuthorService authorService;
    private final BookReader bookReader;
    private final UserInterface userInterface;

    public LibraryShell(BookService bookService,
                        GenreService genreService,
                        AuthorService authorService,
                        BookReader bookReader, UserInterface userInterface) {
        this.bookService = bookService;
        this.genreService = genreService;
        this.authorService = authorService;
        this.bookReader = bookReader;
        this.userInterface = userInterface;
    }

    @ShellMethod(value = "Print all genres", key = {"print-genres"})
    String printAllGenres(){
        List<Genre> genres = genreService.getAllGenres();
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
            genreService.addGenre(title);
            return userInterface.getText("shell.genre-added");
        }
        catch (AddGenreException e) {
            return userInterface.getText("shell.error.add-genre-error");
        }
    }

    @ShellMethod(value = "Update genre", key = {"update-genre"})
    String updateGenre(long id, String title) {
        try {
            genreService.updateGenre(id, title);
            return userInterface.getText("shell.genre-updated");
        }
        catch (UpdateGenreException e) {
            return userInterface.getText("shell.error.update-genre-error");
        }
    }

    @ShellMethod(value = "Delete genre", key = {"delete-genre"})
    String deleteGenre(long id) {
        try {
            genreService.deleteGenre(id);
            return userInterface.getText("shell.genre-deleted");
        }
        catch (DeleteGenreException e) {
            return userInterface.getText("shell.error.delete-genre-error");
        }
    }

    @ShellMethod(value = "Print all authors", key = {"print-authors"})
    String printAllAuthors() {
        List<Author> authors = authorService.getAllAuthors();
        if (authors.size() > 0)
            return authors.stream()
                    .map(UIUtils::getAuthorString)
                    .collect(Collectors.joining("\n"));
        else
            return userInterface.getText("shell.error.no-authors-found");
    }

    @ShellMethod(value = "Add author", key = {"add-author"})
    String addAuthor(String surname, String otherNames) {
        try {
            authorService.addAuthor(Author.createNewAuthor(surname, otherNames));
            return userInterface.getText("shell.author-added");
        }
        catch (AddAuthorException e) {
            return userInterface.getText("shell.error.add-author-error");
        }
    }

    @ShellMethod(value = "Update author", key = {"update-author"})
    String updateAuthor(long id, String surname, String otherNames) {
        try {
            authorService.updateAuthor(id, Author.createNewAuthor(surname, otherNames));
            return userInterface.getText("shell.author-updated");
        }
        catch (UpdateAuthorException e) {
            return userInterface.getText("shell.error.update-author-error");
        }
    }

    @ShellMethod(value = "Delete author", key = {"delete-author"})
    String deleteAuthor(long id) {
        try {
            authorService.deleteAuthor(id);
            return userInterface.getText("shell.author-deleted");
        }
        catch (DeleteAuthorException e) {
            return userInterface.getText("shell.error.delete-author-error");
        }
    }

    @ShellMethod(value = "Print all books", key = {"print-books"})
    String printAllBooks() {
        List<Book> books = bookService.getAllBooks();
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
        List<Author> authors = authorService.getAllAuthors();
        List<Genre> genres = genreService.getAllGenres();
        Book book = bookReader.getBook(authors, genres);
        bookService.addBook(book);
        return userInterface.getText("shell.book-added");
    }

    @ShellMethod(value = "Delete book", key = {"delete-book"})
    String deleteBook(long id) {
        bookService.deleteBook(id);
        return userInterface.getText("shell.book-deleted");
    }

//    @ShellMethod(value = "Start console", key = {"console"})
//    String console() throws Exception{
//        Console.main();
//        return "Console started";
//    }
}
