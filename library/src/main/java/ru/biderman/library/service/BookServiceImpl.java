package ru.biderman.library.service;

import org.springframework.stereotype.Service;
import ru.biderman.library.dao.BookDao;
import ru.biderman.library.domain.Book;
import ru.biderman.library.domain.Comment;

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
    public void deleteBook(Book book) {
        bookDao.deleteBook(book);
    }

    @Override
    public List<Book> getAllBooks() {
        return bookDao.getAllBooks();
    }

    @Override
    public Book getBookById(long id) {
        return bookDao.getBookById(id);
    }

    @Override
    public void addComment(Book book, Comment comment) {
        book.getCommentList().add(comment);
        bookDao.updateBook(book);
    }

    @Override
    public void deleteComment(Book book, long commentId) {
        book.getCommentList().stream()
                .filter(comment -> comment.getId() == commentId)
                .findFirst()
                .ifPresent(comment -> {
                    book.getCommentList().remove(comment);
                    bookDao.updateBook(book);
                });
    }
}
