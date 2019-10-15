package ru.biderman.library.dao;

import ru.biderman.library.domain.Book;
import ru.biderman.library.domain.Comment;

import java.util.List;

public interface CommentDao {
    void addComment(Comment comment);
    void deleteCommentById(long id);
    void deleteAllCommentsByBook(Book book);
    List<Comment> getCommentsByBook(Book book);
}
