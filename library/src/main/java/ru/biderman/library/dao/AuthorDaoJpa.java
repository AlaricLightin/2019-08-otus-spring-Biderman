package ru.biderman.library.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.biderman.library.domain.Author;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

@SuppressWarnings("JpaQlInspection")
@Repository
public class AuthorDaoJpa implements AuthorDao{
    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public void addAuthor(Author author) {
        em.persist(author);
    }

    @Override
    @Transactional
    public void updateAuthor(Author author) {
        em.merge(author);
    }

    @Override
    @Transactional
    public void deleteAuthor(long id){
        Query query = em.createQuery("DELETE FROM Author a WHERE a.id = :id");
        query.setParameter("id", id);
        query.executeUpdate();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Author> getAllAuthors() {
        TypedQuery<Author> query = em.createQuery("select a from Author a", Author.class);
        return query.getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public Author getAuthorById(long id) {
        return em.find(Author.class, id);
    }
}
