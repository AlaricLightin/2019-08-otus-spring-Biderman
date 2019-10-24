package ru.biderman.librarymongo.service;

import org.springframework.stereotype.Service;
import ru.biderman.librarymongo.domain.Book;
import ru.biderman.librarymongo.repositories.BookRepository;

import java.util.List;

@Service
class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public void addBook(Book book) {
        bookRepository.save(book);
    }

    @Override
    public void deleteById(String id) {
        bookRepository.deleteById(id);
    }

    @Override
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    public Book getBookById(String id) {
        return bookRepository.findById(id).orElse(null);
    }

    @Override
    public void updateGenre(String oldGenresText, String newGenresText) {
        bookRepository.updateGenre(oldGenresText, newGenresText);
    }
}
