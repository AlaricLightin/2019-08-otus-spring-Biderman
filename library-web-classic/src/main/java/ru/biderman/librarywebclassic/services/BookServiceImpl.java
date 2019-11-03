package ru.biderman.librarywebclassic.services;

import org.springframework.stereotype.Service;
import ru.biderman.librarywebclassic.domain.Book;
import ru.biderman.librarywebclassic.repositories.BookRepository;
import ru.biderman.librarywebclassic.services.exceptions.BookNotFoundException;

import java.util.List;

@Service
class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public void save(Book book) {
        bookRepository.save(book);
    }

    @Override
    public void deleteById(long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    public Book getBookById(long id) throws BookNotFoundException{
        return bookRepository.findById(id).orElseThrow(BookNotFoundException::new);
    }
}
