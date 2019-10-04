package ru.biderman.library.service;

import org.springframework.stereotype.Service;

@Service
public class DatabaseServiceImpl implements DatabaseService{
    private final AuthorService authorService;
    private final GenreService genreService;
    private final BookService bookService;

    public DatabaseServiceImpl(AuthorService authorService,
                               GenreService genreService,
                               BookService bookService) {
        this.authorService = authorService;
        this.genreService = genreService;
        this.bookService = bookService;
    }

    @Override
    public AuthorService getAuthorService() {
        return authorService;
    }

    @Override
    public GenreService getGenreService() {
        return genreService;
    }

    @Override
    public BookService getBookService() {
        return bookService;
    }
}
