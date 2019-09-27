package ru.biderman.library.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import ru.biderman.library.dao.AuthorDao;
import ru.biderman.library.dao.DaoException;
import ru.biderman.library.domain.Author;
import ru.biderman.library.service.exceptions.DeleteAuthorException;
import ru.biderman.library.service.exceptions.ServiceException;
import ru.biderman.library.service.exceptions.UpdateAuthorException;

import java.util.Collections;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("Сервис по работе с авторами ")
class AuthorServiceImplTest {
    private static final String AUTHOR_SURNAME = "Surname";
    private static final String AUTHOR_NAME = "Name";
    private final long AUTHOR_ID = 1;
    private AuthorDao authorDao;
    private AuthorService authorService;

    @BeforeEach
    void initAuthorDao() {
        authorDao = mock(AuthorDao.class);
        authorService = new AuthorServiceImpl(authorDao);
    }

    @DisplayName("должен возвращать всех")
    @Test
    void shouldGetAll() {
        final Map<Long, Author> resultMap = Collections.singletonMap(1L, new Author(AUTHOR_ID, AUTHOR_SURNAME, AUTHOR_NAME));
        when(authorDao.getAllAuthors()).thenReturn(resultMap);
        assertEquals(resultMap, authorService.getAllAuthors());
    }

    @DisplayName("должен добавлять автора")
    @Test
    void shouldAddAuthor() throws ServiceException {
        Author author = Author.createNewAuthor(AUTHOR_SURNAME, AUTHOR_NAME);
        authorService.addAuthor(author);
        verify(authorDao).addAuthor(author);
    }

    @DisplayName("должен удалять автора")
    @Test
    void shouldDeleteAuthor() throws ServiceException, DaoException{
        when(authorDao.isUsed(AUTHOR_ID)).thenReturn(false);
        authorService.deleteAuthor(AUTHOR_ID);
        verify(authorDao).deleteAuthor(AUTHOR_ID);
    }

    @DisplayName("должен бросать исключение, если автор используется")
    @Test
    void shouldThrowExceptionIfAuthorIsUsed() {
        when(authorDao.isUsed(AUTHOR_ID)).thenReturn(true);
        assertThrows(DeleteAuthorException.class, () -> authorService.deleteAuthor(AUTHOR_ID));
    }

    @DisplayName("должен бросать исключение, если автора удалить не удаётся")
    @Test
    void shouldThrowExceptionIfCouldNotDelete() throws DaoException{
        when(authorDao.isUsed(AUTHOR_ID)).thenReturn(false);
        doThrow(DaoException.class).when(authorDao).deleteAuthor(AUTHOR_ID);
        assertThrows(DeleteAuthorException.class, () -> authorService.deleteAuthor(AUTHOR_ID));
    }

    @DisplayName("должен обновлять автора")
    @Test
    void shouldUpdate() throws ServiceException{
        Author newAuthor = Author.createNewAuthor("New-surname", "New-name");
        when(authorDao.getAuthorById(AUTHOR_ID)).thenReturn(new Author(AUTHOR_ID, AUTHOR_SURNAME, AUTHOR_NAME));
        authorService.updateAuthor(AUTHOR_ID, newAuthor);
        verify(authorDao).updateAuthor(AUTHOR_ID, newAuthor);
    }

    @DisplayName("должен бросать исключение при обновлении, если жанра нет")
    @Test
    void shouldThrowUpdateExceptionIfNoGenre() {
        Author newAuthor = Author.createNewAuthor("New-surname", "New-name");
        when(authorDao.getAuthorById(AUTHOR_ID)).thenReturn(null);
        assertThrows(UpdateAuthorException.class, () -> authorService.updateAuthor(AUTHOR_ID, newAuthor));
    }
}