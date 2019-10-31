package ru.biderman.librarywebclassic.services;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import ru.biderman.librarywebclassic.domain.Author;
import ru.biderman.librarywebclassic.repositories.AuthorRepository;
import ru.biderman.librarywebclassic.services.exceptions.AuthorNotFoundException;
import ru.biderman.librarywebclassic.services.exceptions.DeleteAuthorException;

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
    public void updateAuthor(long id, Author author) throws AuthorNotFoundException {
        Author oldAuthor = authorRepository.findById(id).orElseThrow(AuthorNotFoundException::new);
        oldAuthor.setSurname(author.getSurname());
        oldAuthor.setOtherNames(author.getOtherNames());
        authorRepository.save(oldAuthor);
    }

    @Override
    public void deleteById(long id) throws DeleteAuthorException {
        try {
            authorRepository.deleteById(id);
        }
        catch (DataAccessException | PersistenceException e) {
            throw new DeleteAuthorException();
        }
    }

    @Override
    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }

    @Override
    public Author findById(long id) throws AuthorNotFoundException{
        return authorRepository.findById(id).orElseThrow(AuthorNotFoundException::new);
    }

    @Override
    public List<Long> getUsedAuthorIdList() {
        return authorRepository.getUsedAuthorIdList();
    }
}
