package ru.biderman.librarywebclassic.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.biderman.librarywebclassic.domain.Author;
import ru.biderman.librarywebclassic.repositories.AuthorRepository;
import ru.biderman.librarywebclassic.services.exceptions.AuthorNotFoundException;
import ru.biderman.librarywebclassic.services.exceptions.DeleteAuthorException;
import ru.biderman.librarywebclassic.services.exceptions.ServiceException;

import javax.persistence.PersistenceException;
import java.util.Arrays;
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
    private final long AUTHOR_ID = 1;
    private AuthorRepository authorRepository;
    private AuthorService authorService;

    @BeforeEach
    void initAuthorDao() {
        authorRepository = mock(AuthorRepository.class);
        authorService = new AuthorServiceImpl(authorRepository);
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
        authorService.deleteById(AUTHOR_ID);
        verify(authorRepository).deleteById(AUTHOR_ID);
    }

    @DisplayName("должен бросать исключение, если автора удалить не удаётся")
    @Test
    void shouldThrowExceptionIfCouldNotDelete(){
        doThrow(PersistenceException.class).when(authorRepository).deleteById(AUTHOR_ID);
        assertThrows(DeleteAuthorException.class, () -> authorService.deleteById(AUTHOR_ID));
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
        assertThrows(AuthorNotFoundException.class, () -> authorService.updateAuthor(AUTHOR_ID, newAuthor));
    }

    @DisplayName("должен возвращать список id используемых авторов")
    @Test
    void shouldGetUsedAuthorIds() {
        List<Long> resultList = Arrays.asList(100L, 101L);
        when(authorRepository.getUsedAuthorIdList()).thenReturn(resultList);
        assertThat(authorService.getUsedAuthorIdList()).isEqualTo(resultList);
    }

    @DisplayName("должен возвращать общее число авторов")
    @Test
    void shouldGetCount() {
        final long authorCount = 10;
        when(authorRepository.count()).thenReturn(authorCount);
        assertThat(authorService.getCount()).isEqualTo(authorCount);
    }
}