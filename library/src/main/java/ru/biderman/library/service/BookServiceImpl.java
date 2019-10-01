package ru.biderman.library.service;

import org.springframework.stereotype.Service;
import ru.biderman.library.dao.BookDao;
import ru.biderman.library.domain.Book;

import java.util.List;

@Service
class BookServiceImpl implements BookService {
    private final BookDao bookDao;

    public BookServiceImpl(BookDao bookDao) {
        this.bookDao = bookDao;
    }

    @Override
    public void addBook(Book book) {
        bookDao.addBook(book);
    }

    @Override
    public void deleteBook(long id) {
        bookDao.deleteBook(id);
    }

    @Override
    public List<Book> getAllBooks() {
        return bookDao.getAllBooks();
    }
}