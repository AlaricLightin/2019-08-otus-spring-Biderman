package ru.biderman.librarymongo.service;

import org.springframework.stereotype.Service;
import ru.biderman.librarymongo.domain.Book;
import ru.biderman.librarymongo.domain.Comment;
import ru.biderman.librarymongo.repositories.CommentRepository;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService{
    private final CommentRepository commentRepository;

    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public void addComment(Comment comment) {
        commentRepository.save(comment);
    }

    @Override
    public void deleteCommentById(String id) {
        commentRepository.deleteById(id);
    }

    @Override
    public List<Comment> getCommentsByBook(Book book) {
        return commentRepository.findByBook(book);
    }
}
