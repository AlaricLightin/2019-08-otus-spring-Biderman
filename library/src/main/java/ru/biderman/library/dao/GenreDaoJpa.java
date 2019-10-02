package ru.biderman.library.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.biderman.library.domain.Genre;

import javax.persistence.*;
import java.util.List;

@SuppressWarnings("JpaQlInspection")
@Repository
public class GenreDaoJpa implements GenreDao {
    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public void addGenre(Genre genre) {
        em.persist(genre);
        em.flush();
    }

    @Override
    @Transactional
    public void updateGenre(Genre genre) {
        em.merge(genre);
        em.flush();
    }

    @Override
    @Transactional
    public void deleteGenre(long id) {
        Query query = em.createQuery("DELETE FROM Genre g WHERE g.id = :id");
        query.setParameter("id", id);
        query.executeUpdate();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Genre> getAllGenres() {
        TypedQuery<Genre> query = em.createQuery("select g from Genre g", Genre.class);
        return query.getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public Genre getGenreById(long id) {
        return em.find(Genre.class, id);
    }

    @Override
    @Transactional(readOnly = true)
    public Genre getGenreByText(String text) {
        TypedQuery<Genre> query = em.createQuery("select g from Genre g where g.text = :text", Genre.class);
        query.setParameter("text", text);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
