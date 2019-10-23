package ru.biderman.librarymongo.service;

import org.springframework.stereotype.Service;

@Service
public class DatabaseServiceImpl implements DatabaseService{
    private final AuthorService authorService;
    private final BookService bookService;
    private final CommentService commentService;

    public DatabaseServiceImpl(AuthorService authorService,
                               BookService bookService,
                               CommentService commentService) {
        this.authorService = authorService;
        this.bookService = bookService;
        this.commentService = commentService;
    }

    @Override
    public AuthorService getAuthorService() {
        return authorService;
    }

    @Override
    public BookService getBookService() {
        return bookService;
    }

    @Override
    public CommentService getCommentService() {
        return commentService;
    }
}
