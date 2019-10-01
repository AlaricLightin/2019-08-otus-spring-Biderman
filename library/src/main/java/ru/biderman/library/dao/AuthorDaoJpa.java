package ru.biderman.library.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.biderman.library.domain.Author;

import javax.persistence.*;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

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
    @Transactional(rollbackFor = DaoException.class)
    public void deleteAuthor(long id) throws DaoException{
        Query query = em.createQuery("DELETE FROM Author a WHERE a.id = :id");
        query.setParameter("id", id);
        try {
            query.executeUpdate();
        } catch (PersistenceException e) {
            throw new DaoException(e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Map<Long, Author> getAllAuthors() {
        TypedQuery<Author> query = em.createQuery("select a from Author a", Author.class);
        return query.getResultStream()
                .collect(Collectors.toMap(Author::getId, Function.identity()));
    }

    @Override
    @Transactional(readOnly = true)
    public Author getAuthorById(long id) {
        return em.find(Author.class, id);
    }
}
