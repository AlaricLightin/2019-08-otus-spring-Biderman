package ru.biderman.library.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.biderman.library.domain.Book;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@SuppressWarnings("JpaQlInspection")
@Repository
@Transactional
public class BookDaoJpa implements BookDao {
    @PersistenceContext
    private EntityManager em;

    private final CommentDao commentDao;

    public BookDaoJpa(CommentDao commentDao) {
        this.commentDao = commentDao;
    }

    @Override
    public void addBook(Book book) {
        em.persist(book);
    }

    private void deleteBook(Book book) {
        commentDao.deleteAllCommentsByBook(book);
        em.remove(book);
    }

    @Override
    public void deleteById(long id) {
        Book book = getBookById(id);
        if (book != null)
            deleteBook(book);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Book> getAllBooks() {
        TypedQuery<Book> query = em.createQuery("select b from Book b", Book.class);
        return query.getResultList();
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public Book getBookById(long id) {
        TypedQuery<Book> query = em.createQuery("select b from Book b where b.id = :id",
                Book.class);
        query.setParameter("id", id);
        try {
            return query.getSingleResult();
        }
        catch (NoResultException e) {
            return null;
        }
    }
}
