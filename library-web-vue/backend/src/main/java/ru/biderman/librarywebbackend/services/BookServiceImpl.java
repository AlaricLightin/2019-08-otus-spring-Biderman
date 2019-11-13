package ru.biderman.librarywebbackend.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.biderman.librarywebbackend.domain.Book;
import ru.biderman.librarywebbackend.repositories.BookRepository;
import ru.biderman.librarywebbackend.services.exceptions.BookNotFoundException;

import java.util.List;

@Service
class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public Book createBook(Book book) {
        return bookRepository.save(book);
    }

    @Override
    @Transactional
    public Book updateBook(Book book) throws BookNotFoundException {
        if (bookRepository.existsById(book.getId()))
            return bookRepository.save(book);
        else
            throw new BookNotFoundException();
    }

    @Override
    @Transactional
    public void deleteById(long id) throws BookNotFoundException{
        if (bookRepository.existsById(id))
            bookRepository.deleteById(id);
        else
            throw new BookNotFoundException();
    }

    @Override
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    public Book getBookById(long id) {
        return bookRepository.findById(id).orElse(null);
    }
}
