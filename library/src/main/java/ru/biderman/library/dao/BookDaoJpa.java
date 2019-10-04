package ru.biderman.library.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.biderman.library.domain.Book;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@SuppressWarnings("JpaQlInspection")
@Repository
public class BookDaoJpa implements BookDao {
    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public void addBook(Book book) {
        em.persist(book);
    }

    @Override
    @Transactional
    public void updateBook(Book book) {
        em.merge(book);
    }

    @Override
    @Transactional
    public void deleteBook(Book book) {
        em.remove(book);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Book> getAllBooks() {
        TypedQuery<Book> query = em.createQuery("select b from Book b", Book.class);
        return query.getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public Book getBookById(long id) {
        TypedQuery<Book> query = em.createQuery("select b from Book b left join fetch b.commentList where b.id = :id",
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
