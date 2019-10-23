package ru.biderman.librarymongo.service;

import org.springframework.stereotype.Service;
import ru.biderman.librarymongo.domain.Author;
import ru.biderman.librarymongo.repositories.AuthorRepository;
import ru.biderman.librarymongo.repositories.BookRepository;
import ru.biderman.librarymongo.service.exceptions.DeleteAuthorException;
import ru.biderman.librarymongo.service.exceptions.UpdateAuthorException;

import java.util.List;

@Service
class AuthorServiceImpl implements AuthorService{
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    public AuthorServiceImpl(AuthorRepository authorRepository, BookRepository bookRepository) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    public void addAuthor(Author author) {
        authorRepository.save(author);
    }

    @Override
    public void updateAuthor(String id, Author author) throws UpdateAuthorException {
        Author oldAuthor = authorRepository.findById(id).orElseThrow(UpdateAuthorException::new);
        oldAuthor.setSurname(author.getSurname());
        oldAuthor.setOtherNames(author.getOtherNames());
        authorRepository.save(oldAuthor);
    }

    @Override
    public void deleteAuthor(String id) throws DeleteAuthorException {
        if (!bookRepository.isAuthorUsed(id))
            authorRepository.deleteById(id);
        else
            throw new DeleteAuthorException();
    }

    @Override
    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }
}
