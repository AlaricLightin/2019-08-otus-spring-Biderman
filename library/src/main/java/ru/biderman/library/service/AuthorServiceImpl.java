package ru.biderman.library.service;

import org.springframework.stereotype.Service;
import ru.biderman.library.dao.AuthorDao;
import ru.biderman.library.dao.DaoException;
import ru.biderman.library.domain.Author;
import ru.biderman.library.service.exceptions.DeleteAuthorException;
import ru.biderman.library.service.exceptions.UpdateAuthorException;

import java.util.Map;

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
        Author oldAuthor = authorDao.getAuthorById(id);
        if (oldAuthor != null)
            authorDao.updateAuthor(id, author);
        else
            throw new UpdateAuthorException();
    }

    @Override
    public void deleteAuthor(long id) throws DeleteAuthorException {
        try {
            if (!authorDao.isUsed(id))
                authorDao.deleteAuthor(id);
            else
                throw new DeleteAuthorException();
        }
        catch (DaoException e) {
            throw new DeleteAuthorException();
        }

    }

    @Override
    public Map<Long, Author> getAllAuthors() {
        return authorDao.getAllAuthors();
    }
}
