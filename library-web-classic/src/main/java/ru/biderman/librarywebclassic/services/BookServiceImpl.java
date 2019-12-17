package ru.biderman.librarywebclassic.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.biderman.librarywebclassic.domain.Book;
import ru.biderman.librarywebclassic.repositories.BookRepository;
import ru.biderman.librarywebclassic.services.exceptions.BookNotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final AvailabilityForMinorsService availabilityForMinorsService;

    @Override
    @Transactional
    public void save(Book book, boolean adultOnly) {
        bookRepository.save(book);
        availabilityForMinorsService.setRights(book, adultOnly);
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

    @Override
    public long getCount() {
        return bookRepository.count();
    }
}
