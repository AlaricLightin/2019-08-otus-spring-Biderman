package ru.biderman.library.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.biderman.library.domain.Genre;

import javax.persistence.*;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

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
    public Map<Long, Genre> getAllGenres() {
        TypedQuery<Genre> query = em.createQuery("select g from Genre g", Genre.class);
        return query.getResultStream()
                .collect(Collectors.toMap(Genre::getId, Function.identity()));
    }

    @Override
    @Transactional(readOnly = true)
    public Genre getGenreById(long id) {
        return em.find(Genre.class, id);
    }

    @Override
    @Transactional(readOnly = true)
    public Genre getGenreByTitle(String title) {
        TypedQuery<Genre> query = em.createQuery("select g from Genre g where g.title = :title", Genre.class);
        query.setParameter("title", title);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
