package ru.biderman.library.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.biderman.library.domain.Book;
import ru.biderman.library.domain.Comment;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

@SuppressWarnings("JpaQlInspection")
@Repository
@Transactional
public class CommentDaoJpa implements CommentDao {
    @PersistenceContext
    private EntityManager em;

    @Override
    public void addComment(Comment comment) {
        em.persist(comment);
    }

    @Override
    public void deleteCommentById(long id) {
        Query query = em.createQuery("DELETE FROM Comment c WHERE c.id = :id");
        query.setParameter("id", id);
        query.executeUpdate();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteAllCommentsByBook(Book book) {
        Query query = em.createQuery("DELETE FROM Comment c WHERE c.book = :book");
        query.setParameter("book", book);
        query.executeUpdate();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Comment> getCommentsByBook(Book book) {
        TypedQuery<Comment> query = em.createQuery("SELECT c FROM Comment c WHERE c.book = :book", Comment.class);
        query.setParameter("book", book);
        return query.getResultList();
    }
}
