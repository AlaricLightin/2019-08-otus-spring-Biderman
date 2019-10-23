package ru.biderman.librarymongo.service;

public interface DatabaseService {
    AuthorService getAuthorService();
    BookService getBookService();
    CommentService getCommentService();
}
