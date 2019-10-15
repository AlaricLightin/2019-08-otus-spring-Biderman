package ru.biderman.library.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

public class BookRepositoryImpl implements BookRepositoryCustom {
    @Autowired
    private BookRepository bookRepository;

    private final CommentRepository commentRepository;

    public BookRepositoryImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    @Transactional
    public void deleteByIdWithComments(long id) {
        bookRepository.findById(id).ifPresent(book -> {
            commentRepository.deleteByBook(book);
            bookRepository.delete(book);
        });
    }
}
