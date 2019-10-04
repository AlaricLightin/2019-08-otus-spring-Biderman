package ru.biderman.library.service;

public interface DatabaseService {
    AuthorService getAuthorService();
    GenreService getGenreService();
    BookService getBookService();
}
