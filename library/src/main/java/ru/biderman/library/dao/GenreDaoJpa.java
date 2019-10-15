package ru.biderman.library.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.biderman.library.domain.Genre;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("JpaQlInspection")
@Repository
@Transactional
public class GenreDaoJpa implements GenreDao {
    @PersistenceContext
    private EntityManager em;

    @Override
    public void addGenre(Genre genre) {
        em.persist(genre);
        em.flush();
    }

    @Override
    public void updateGenre(Genre genre) {
        em.merge(genre);
        em.flush();
    }

    @Override
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
    public Optional<Genre> getGenreById(long id) {
        return Optional.ofNullable(em.find(Genre.class, id));
    }
}
