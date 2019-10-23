package ru.biderman.librarymongo.service;

import ru.biderman.librarymongo.domain.Book;
import ru.biderman.librarymongo.domain.Comment;

import java.util.List;

public interface CommentService {
    void addComment(Comment comment);
    void deleteCommentById(String id);
    List<Comment> getCommentsByBook(Book book);
}
