package ru.biderman.library.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.biderman.library.domain.Book;
import ru.biderman.library.domain.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    void deleteByBook(Book book);
    List<Comment> findByBook(Book book);
}
