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
    @Transactional(rollbackFor = DaoException.class)
    public void addGenre(Genre genre) throws DaoException {
        try {
            em.persist(genre);
        } catch (PersistenceException e) {
            throw new DaoException(e);
        }
    }

    @Override
    @Transactional(rollbackFor = DaoException.class)
    public void updateGenre(Genre genre) throws DaoException {
        try {
            em.merge(genre);
            em.flush();
        }
        catch (PersistenceException e) {
            throw new DaoException(null);
        }

        //TODO переделать как в авторах
//        Genre g = getGenreById(genre.getId());
//        if (g == null)
//            throw new DaoException(null);
//
//        if (g.getTitle().equals(genre.getTitle()))
//            return;
//
//        Genre gTitle = getGenreByTitle(genre.getTitle());
//        if (gTitle == null)
//            em.merge(genre);
//        else
//            throw new DaoException(null);
    }

    @Override
    @Transactional(rollbackFor = DaoException.class)
    public void deleteGenre(long id) throws DaoException {
        Query query = em.createQuery("DELETE FROM Genre g WHERE g.id = :id");
        query.setParameter("id", id);
        try {
            query.executeUpdate();
        } catch (PersistenceException e) {
            throw new DaoException(e);
        }
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
