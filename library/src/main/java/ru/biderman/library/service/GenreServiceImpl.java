package ru.biderman.library.service;

import org.springframework.stereotype.Service;
import ru.biderman.library.dao.DaoException;
import ru.biderman.library.dao.GenreDao;
import ru.biderman.library.domain.Genre;
import ru.biderman.library.service.exceptions.AddGenreException;
import ru.biderman.library.service.exceptions.DeleteGenreException;
import ru.biderman.library.service.exceptions.UpdateGenreException;

import java.util.Map;

@Service
class GenreServiceImpl implements GenreService {
    private final GenreDao genreDao;

    public GenreServiceImpl(GenreDao genreDao) {
        this.genreDao = genreDao;
    }

    @Override
    public void addGenre(String title) throws AddGenreException {
        try {
            Genre genre = genreDao.getGenreByTitle(title);
            if (genre == null)
                genreDao.addGenre(Genre.createNewGenre(title));
            else
                throw new AddGenreException();
        }
        catch (DaoException e) {
            throw new AddGenreException();
        }
    }

    @Override
    public void updateGenre(long id, String title) throws UpdateGenreException {
        Genre genre = genreDao.getGenreById(id);
        if (genre != null) {
            genre.setTitle(title);
            try {
                genreDao.updateGenre(genre);
            } catch (DaoException e) {
                throw new UpdateGenreException();
            }
        }
        else
            throw new UpdateGenreException();
    }

    @Override
    public void deleteGenre(long id) throws DeleteGenreException {
        try {
            genreDao.deleteGenre(id);
        }
        catch (DaoException e) {
            throw new DeleteGenreException();
        }
    }

    @Override
    public Map<Long, Genre> getAllGenres() {
        return genreDao.getAllGenres();
    }
}
