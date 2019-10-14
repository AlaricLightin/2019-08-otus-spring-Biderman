package ru.biderman.library.service;

import ru.biderman.library.domain.Book;
import ru.biderman.library.domain.Comment;

import java.util.List;

public interface CommentService {
    void addComment(Comment comment);
    void deleteCommentById(long id);
    List<Comment> getCommentsByBook(Book book);
}
