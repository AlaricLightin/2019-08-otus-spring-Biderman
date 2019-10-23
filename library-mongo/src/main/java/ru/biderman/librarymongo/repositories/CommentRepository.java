package ru.biderman.librarymongo.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.biderman.librarymongo.domain.Book;
import ru.biderman.librarymongo.domain.Comment;

import java.util.List;

public interface CommentRepository extends MongoRepository<Comment, String> {
    void deleteByBook_Id(String id);
    List<Comment> findByBook(Book book);
}
