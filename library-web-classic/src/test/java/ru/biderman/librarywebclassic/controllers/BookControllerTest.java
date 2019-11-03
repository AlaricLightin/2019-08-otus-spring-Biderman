package ru.biderman.librarywebclassic.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.biderman.librarywebclassic.domain.Author;
import ru.biderman.librarywebclassic.domain.Book;
import ru.biderman.librarywebclassic.domain.Genre;
import ru.biderman.librarywebclassic.services.DatabaseService;
import ru.biderman.librarywebclassic.services.exceptions.BookNotFoundException;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
@DisplayName("Контроллер для работы с книгами")
class BookControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    DatabaseService databaseService;

    private static final long BOOK_ID = 100;
    private static final long AUTHOR_ID = 200;
    private static final String AUTHOR_SURNAME = "Author-surname";
    private static final String AUTHOR_NAME = "Author-name";
    private static final String GENRE = "Genre";
    private static final long GENRE_ID = 300;
    private static final String NEW_BOOK_TITLE = "New Book Title";

    private Book createTestBook() {
        return new Book(BOOK_ID,
                Collections.singletonList(new Author(AUTHOR_ID, AUTHOR_SURNAME, AUTHOR_NAME)),
                NEW_BOOK_TITLE,
                Collections.singleton(new Genre(GENRE_ID, GENRE)));
    }

    @DisplayName("должен возвращать список всех книг")
    @Test
    void shouldGetAllBooks() throws Exception{
        Book book = createTestBook();
        when(databaseService.getAllBooks()).thenReturn(Collections.singletonList(book));

        MvcResult result = mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("books"))
                .andExpect(model().attributeExists("books"))
                .andReturn();

        final String editFormat = "/books/edit?id=%s";
        final String deleteFormat = "/books/delete?id=%s";
        assertThat(result.getResponse().getContentAsString())
                .contains(AUTHOR_NAME)
                .contains(AUTHOR_SURNAME)
                .contains(NEW_BOOK_TITLE)
                .contains(GENRE)
                .contains(String.format(editFormat, BOOK_ID))
                .contains(String.format(deleteFormat, BOOK_ID));
    }

    @Nested
    @DisplayName("должен создавать форму для редактирования ")
    class CreateEditForm {
        private Author author1;
        private Author author2;
        private Genre genre1;
        private Genre genre2;

        @BeforeEach
        void initServices() {
            author1 = new Author(AUTHOR_ID, AUTHOR_SURNAME, AUTHOR_NAME);
            author2 = new Author(AUTHOR_ID + 1, "Surname2", "Name2");
            when(databaseService.getAllAuthors()).thenReturn(Arrays.asList(author1, author2));

            genre1 = new Genre(GENRE_ID, GENRE);
            genre2 = new Genre(GENRE_ID + 1, "Another genre");
            when(databaseService.getAllGenres()).thenReturn(Arrays.asList(genre1, genre2));
        }

        @DisplayName("существующей книги")
        @Test
        void forExistingBook() throws Exception{
            Book book = createTestBook();
            when(databaseService.getBookById(BOOK_ID)).thenReturn(book);

            MvcResult result = mockMvc.perform(get("/books/edit?id=" + BOOK_ID))
                    .andExpect(status().isOk())
                    .andExpect(view().name("book-edit"))
                    .andExpect(model().attribute("book", book))
                    .andExpect(model().attribute("allAuthors", hasSize(2)))
                    .andExpect(model().attribute("allGenres", hasSize(2)))
                    .andReturn();

            assertThat(result.getResponse().getContentAsString())
                    .contains(author1.getSurname())
                    .contains(author1.getOtherNames())
                    .contains(NEW_BOOK_TITLE)
                    .contains(genre1.getText())
                    .contains(author2.getSurname())
                    .contains(genre2.getText());
        }

        @DisplayName("новой книги")
        @Test
        void forNewBook() throws Exception {
            mockMvc.perform(get("/books/edit?id=0"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("book-edit"))
                    .andExpect(model().attribute("book", hasProperty("title", emptyString())))
                    .andExpect(model().attribute("allAuthors", hasSize(2)))
                    .andExpect(model().attribute("allGenres", hasSize(2)))
                    .andReturn();

        }
    }

    @DisplayName("должен создавать форму для редактирования книги")
    @Test
    void shouldCreateEditForm() throws Exception{
        Book book = createTestBook();
        when(databaseService.getBookById(BOOK_ID)).thenReturn(book);

        Author author1 = new Author(AUTHOR_ID, AUTHOR_SURNAME, AUTHOR_NAME);
        Author author2 = new Author(AUTHOR_ID + 1, "Surname2", "Name2");
        when(databaseService.getAllAuthors()).thenReturn(Arrays.asList(author1, author2));

        Genre genre1 = new Genre(GENRE_ID, GENRE);
        Genre genre2 = new Genre(GENRE_ID + 1, "Another genre");
        when(databaseService.getAllGenres()).thenReturn(Arrays.asList(genre1, genre2));

        MvcResult result = mockMvc.perform(get("/books/edit?id=" + BOOK_ID))
                .andExpect(status().isOk())
                .andExpect(view().name("book-edit"))
                .andExpect(model().attribute("book", book))
                .andExpect(model().attribute("allAuthors", hasSize(2)))
                .andExpect(model().attribute("allGenres", hasSize(2)))
                .andReturn();

        assertThat(result.getResponse().getContentAsString())
                .contains(AUTHOR_NAME)
                .contains(AUTHOR_SURNAME)
                .contains(NEW_BOOK_TITLE)
                .contains(GENRE)
                .contains(author2.getSurname())
                .contains(genre2.getText());
    }

    @DisplayName("должен принимать данные о книге")
    @Test
    void shouldReceivePostedNewBook() throws Exception{
        Book book = mock(Book.class);

        mockMvc.perform(post("/books/edit").flashAttr("book", book))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"))
                .andReturn();

        verify(databaseService).saveBook(book);
    }

    @DisplayName("должен показывать форму для удаления книги")
    @Test
    void shouldShowBookDeleteForm() throws Exception {
        Book book = createTestBook();
        when(databaseService.getBookById(BOOK_ID)).thenReturn(book);

        MvcResult result = mockMvc.perform(get(String.format("/books/delete?id=%s", BOOK_ID)))
                .andExpect(status().isOk())
                .andExpect(view().name("delete-book"))
                .andExpect(model().attribute("id", BOOK_ID))
                .andReturn();

        assertThat(result.getResponse().getContentAsString())
                .contains(NEW_BOOK_TITLE)
                .contains(AUTHOR_SURNAME);
    }

    @DisplayName("должен удалять книгу")
    @Test
    void shouldDeleteBook() throws Exception {
        mockMvc.perform(post(String.format("/books/delete?id=%s", BOOK_ID)))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"))
                .andReturn();

        verify(databaseService).deleteBookById(BOOK_ID);
    }

    @DisplayName("должен показывать страницу с исключением при запросе отсутствующей книги")
    @Test
    void shouldShowErrorPageWhenBookIsAbsent() throws Exception {
        doThrow(BookNotFoundException.class).when(databaseService).getBookById(BOOK_ID);
        mockMvc.perform(get(String.format("/books/delete?id=%s", BOOK_ID)))
                .andExpect(status().isNotFound())
                .andExpect(view().name("error"))
                .andReturn();
    }
}