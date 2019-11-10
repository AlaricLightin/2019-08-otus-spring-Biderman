package ru.biderman.librarywebbackend.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.biderman.librarywebbackend.domain.Author;
import ru.biderman.librarywebbackend.repositories.AuthorRepository;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("Сервис по работе с авторами ")
class AuthorServiceImplTest {
    private static final String AUTHOR_SURNAME = "Surname";
    private static final String AUTHOR_NAME = "Name";
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
        long AUTHOR_ID = 1;
        final List<Author> authors = Collections.singletonList(new Author(AUTHOR_ID, AUTHOR_SURNAME, AUTHOR_NAME));
        when(authorRepository.findAll()).thenReturn(authors);
        assertEquals(authors, authorService.getAllAuthors());
    }

}