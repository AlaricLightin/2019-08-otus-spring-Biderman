package ru.biderman.library.service;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import ru.biderman.library.domain.Author;
import ru.biderman.library.repositories.AuthorRepository;
import ru.biderman.library.service.exceptions.DeleteAuthorException;
import ru.biderman.library.service.exceptions.UpdateAuthorException;

import javax.persistence.PersistenceException;
import java.util.List;

@Service
class AuthorServiceImpl implements AuthorService{
    private final AuthorRepository authorRepository;

    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public void addAuthor(Author author) {
        authorRepository.save(author);
    }

    @Override
    public void updateAuthor(long id, Author author) throws UpdateAuthorException {
        Author oldAuthor = authorRepository.findById(id).orElseThrow(UpdateAuthorException::new);
        oldAuthor.setSurname(author.getSurname());
        oldAuthor.setOtherNames(author.getOtherNames());
        authorRepository.save(oldAuthor);
    }

    @Override
    public void deleteAuthor(long id) throws DeleteAuthorException {
        try {
            authorRepository.deleteById(id);
        }
        catch (DataAccessException| PersistenceException e) {
            throw new DeleteAuthorException();
        }
    }

    @Override
    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }
}
