package ru.biderman.librarywebclassic.services;

import org.springframework.stereotype.Service;
import ru.biderman.librarywebclassic.domain.Author;
import ru.biderman.librarywebclassic.domain.Book;
import ru.biderman.librarywebclassic.domain.Genre;
import ru.biderman.librarywebclassic.services.exceptions.AuthorNotFoundException;
import ru.biderman.librarywebclassic.services.exceptions.BookNotFoundException;
import ru.biderman.librarywebclassic.services.exceptions.DeleteAuthorException;

import java.util.List;

@Service
public class DatabaseServiceImpl implements DatabaseService {
    private final BookService bookService;
    private final AuthorService authorService;
    private final GenreService genreService;

    public DatabaseServiceImpl(BookService bookService, AuthorService authorService, GenreService genreService) {
        this.bookService = bookService;
        this.authorService = authorService;
        this.genreService = genreService;
    }

    @Override
    public void saveBook(Book book, boolean adultOnly) {
        bookService.save(book, adultOnly);
    }

    @Override
    public void deleteBookById(long id) {
        bookService.deleteById(id);
    }

    @Override
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    @Override
    public Book getBookById(long id) throws BookNotFoundException {
        return bookService.getBookById(id);
    }

    @Override
    public void addAuthor(Author author) {
        authorService.addAuthor(author);
    }

    @Override
    public void updateAuthor(long id, Author author) throws AuthorNotFoundException {
        authorService.updateAuthor(id, author);
    }

    @Override
    public void deleteAuthor(long id) throws DeleteAuthorException {
        authorService.deleteById(id);
    }

    @Override
    public List<Author> getAllAuthors() {
        return authorService.getAllAuthors();
    }

    @Override
    public Author findAuthorById(long id) throws AuthorNotFoundException {
        return authorService.findById(id);
    }

    @Override
    public List<Genre> getAllGenres() {
        return genreService.getAllGenres();
    }
}
