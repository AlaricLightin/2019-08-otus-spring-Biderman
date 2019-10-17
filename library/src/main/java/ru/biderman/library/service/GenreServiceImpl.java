package ru.biderman.library.service;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import ru.biderman.library.domain.Genre;
import ru.biderman.library.repositories.GenreRepository;
import ru.biderman.library.service.exceptions.AddGenreException;
import ru.biderman.library.service.exceptions.DeleteGenreException;
import ru.biderman.library.service.exceptions.UpdateGenreException;

import javax.persistence.PersistenceException;
import java.util.List;

@Service
class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepository;

    public GenreServiceImpl(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    @Override
    public void addGenre(String title) throws AddGenreException {
        try {
            genreRepository.save(Genre.createNewGenre(title));
        }
        catch (DataAccessException|PersistenceException e) {
            throw new AddGenreException();
        }
    }

    @Override
    public void updateGenre(long id, String title) throws UpdateGenreException {
        Genre genre = genreRepository.findById(id).orElseThrow(UpdateGenreException::new);
        genre.setText(title);
        try {
            genreRepository.save(genre);
        } catch (DataAccessException|PersistenceException e) {
            throw new UpdateGenreException();
        }
    }

    @Override
    public void deleteGenre(long id) throws DeleteGenreException {
        try {
            genreRepository.deleteById(id);
        }
        catch (DataAccessException|PersistenceException e) {
            throw new DeleteGenreException();
        }
    }

    @Override
    public List<Genre> getAllGenres() {
        return genreRepository.findAll();
    }
}
