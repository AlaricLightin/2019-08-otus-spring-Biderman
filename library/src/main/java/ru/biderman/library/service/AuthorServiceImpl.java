package ru.biderman.library.service;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import ru.biderman.library.dao.AuthorDao;
import ru.biderman.library.domain.Author;
import ru.biderman.library.service.exceptions.DeleteAuthorException;
import ru.biderman.library.service.exceptions.UpdateAuthorException;

import javax.persistence.PersistenceException;
import java.util.List;

@Service
class AuthorServiceImpl implements AuthorService{
    private final AuthorDao authorDao;

    public AuthorServiceImpl(AuthorDao authorDao) {
        this.authorDao = authorDao;
    }

    @Override
    public void addAuthor(Author author) {
        authorDao.addAuthor(author);
    }

    @Override
    public void updateAuthor(long id, Author author) throws UpdateAuthorException {
        Author oldAuthor = authorDao.getAuthorById(id).orElseThrow(UpdateAuthorException::new);
        oldAuthor.setSurname(author.getSurname());
        oldAuthor.setOtherNames(author.getOtherNames());
        authorDao.updateAuthor(oldAuthor);
    }

    @Override
    public void deleteAuthor(long id) throws DeleteAuthorException {
        try {
            authorDao.deleteAuthor(id);
        }
        catch (DataAccessException| PersistenceException e) {
            throw new DeleteAuthorException();
        }

    }

    @Override
    public List<Author> getAllAuthors() {
        return authorDao.getAllAuthors();
    }
}
