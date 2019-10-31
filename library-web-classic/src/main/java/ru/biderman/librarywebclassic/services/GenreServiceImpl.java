package ru.biderman.librarywebclassic.services;

import org.springframework.stereotype.Service;
import ru.biderman.librarywebclassic.domain.Genre;
import ru.biderman.librarywebclassic.repositories.GenreRepository;

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
