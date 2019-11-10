package ru.biderman.librarywebbackend.services;

import org.springframework.stereotype.Service;
import ru.biderman.librarywebbackend.domain.Genre;
import ru.biderman.librarywebbackend.repositories.GenreRepository;

import java.util.List;

@Service
class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepository;

    public GenreServiceImpl(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    @Override
    public List<Genre> getAllGenres() {
        return genreRepository.findAll();
    }
}
