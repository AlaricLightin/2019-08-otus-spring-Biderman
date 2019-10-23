package ru.biderman.librarymongo.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.biderman.librarymongo.domain.Author;
import ru.biderman.librarymongo.repositories.AuthorRepository;
import ru.biderman.librarymongo.repositories.BookRepository;
import ru.biderman.librarymongo.service.exceptions.DeleteAuthorException;
import ru.biderman.librarymongo.service.exceptions.ServiceException;
import ru.biderman.librarymongo.service.exceptions.UpdateAuthorException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@DisplayName("Сервис по работе с авторами ")
class AuthorServiceImplTest {
    private static final String AUTHOR_SURNAME = "Surname";
    private static final String AUTHOR_NAME = "Name";
    private final String AUTHOR_ID = "1";
    private AuthorRepository authorRepository;
    private BookRepository bookRepository;
    private AuthorService authorService;

    @BeforeEach
    void initAuthorDao() {
        authorRepository = mock(AuthorRepository.class);
        bookRepository = mock(BookRepository.class);
        authorService = new AuthorServiceImpl(authorRepository, bookRepository);
    }

    @DisplayName("должен возвращать всех")
    @Test
    void shouldGetAll() {
        final List<Author> authors = Collections.singletonList(new Author(AUTHOR_ID, AUTHOR_SURNAME, AUTHOR_NAME));
        when(authorRepository.findAll()).thenReturn(authors);
        assertEquals(authors, authorService.getAllAuthors());
    }

    @DisplayName("должен добавлять автора")
    @Test
    void shouldAddAuthor() {
        Author author = Author.createNewAuthor(AUTHOR_SURNAME, AUTHOR_NAME);
        authorService.addAuthor(author);
        verify(authorRepository).save(author);
    }

    @DisplayName("должен удалять автора")
    @Test
    void shouldDeleteAuthor() throws ServiceException{
        when(bookRepository.isAuthorUsed(AUTHOR_ID)).thenReturn(false);
        authorService.deleteAuthor(AUTHOR_ID);
        verify(authorRepository).deleteById(AUTHOR_ID);
    }

    @DisplayName("должен бросать исключение, если автора удалить не удаётся")
    @Test
    void shouldThrowExceptionIfCouldNotDelete(){
        when(bookRepository.isAuthorUsed(AUTHOR_ID)).thenReturn(true);
        assertThrows(DeleteAuthorException.class, () -> authorService.deleteAuthor(AUTHOR_ID));
    }

    @DisplayName("должен обновлять автора")
    @Test
    void shouldUpdate() throws ServiceException{
        final String NEW_SURNAME = "New-surname";
        String NEW_NAME = "New-name";
        Author newAuthor = Author.createNewAuthor(NEW_SURNAME, NEW_NAME);
        Author oldAuthor = new Author(AUTHOR_ID, AUTHOR_SURNAME, AUTHOR_NAME);
        when(authorRepository.findById(AUTHOR_ID)).thenReturn(Optional.of(oldAuthor));
        authorService.updateAuthor(AUTHOR_ID, newAuthor);
        verify(authorRepository).save(oldAuthor);
        assertThat(oldAuthor)
                .hasFieldOrPropertyWithValue("surname", NEW_SURNAME)
                .hasFieldOrPropertyWithValue("otherNames", NEW_NAME);
    }

    @DisplayName("должен бросать исключение при обновлении, если автора нет")
    @Test
    void shouldThrowUpdateExceptionIfNoAuthor() {
        Author newAuthor = Author.createNewAuthor("New-surname", "New-name");
        when(authorRepository.findById(AUTHOR_ID)).thenReturn(Optional.empty());
        assertThrows(UpdateAuthorException.class, () -> authorService.updateAuthor(AUTHOR_ID, newAuthor));
    }
}