package ru.biderman.librarywebbackend.services;

import org.springframework.stereotype.Service;
import ru.biderman.librarywebbackend.domain.Author;
import ru.biderman.librarywebbackend.repositories.AuthorRepository;

import java.util.List;

@Service
class AuthorServiceImpl implements AuthorService{
    private final AuthorRepository authorRepository;

    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }
}
