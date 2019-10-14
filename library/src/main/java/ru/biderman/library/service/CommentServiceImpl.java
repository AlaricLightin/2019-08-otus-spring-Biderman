package ru.biderman.library.service;

import org.springframework.stereotype.Service;
import ru.biderman.library.dao.CommentDao;
import ru.biderman.library.domain.Book;
import ru.biderman.library.domain.Comment;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService{
    private final CommentDao commentDao;

    public CommentServiceImpl(CommentDao commentDao) {
        this.commentDao = commentDao;
    }

    @Override
    public void addComment(Comment comment) {
        commentDao.addComment(comment);
    }

    @Override
    public void deleteCommentById(long id) {
        commentDao.deleteCommentById(id);
    }

    @Override
    public List<Comment> getCommentsByBook(Book book) {
        return commentDao.getCommentsByBook(book);
    }
}
