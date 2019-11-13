package ru.biderman.librarywebbackend.rest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.UriComponentsBuilder;
import ru.biderman.librarywebbackend.domain.Author;
import ru.biderman.librarywebbackend.domain.Book;
import ru.biderman.librarywebbackend.domain.Genre;
import ru.biderman.librarywebbackend.services.BookService;
import ru.biderman.librarywebbackend.services.exceptions.BookNotFoundException;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("Контроллер по работе с книгами ")
@WebMvcTest(BookController.class)
class BookControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    BookService bookService;

    private final long BOOK_ID = 1000;
    private final long AUTHOR1_ID = 101;
    private final long AUTHOR2_ID = 102;
    private final long GENRE_ID = 11;

    private final String BOOK_TITLE = "Book Title";

    private final Author TEST_AUTHOR1 = new Author(AUTHOR1_ID, "Author1", "Name");
    private final Author TEST_AUTHOR2 = new Author(AUTHOR2_ID, "Author2", "Name2");
    private final Genre TEST_GENRE = new Genre(GENRE_ID, "Genre-text");

    private Book createTestBook() {
        return new Book(BOOK_ID,
                Arrays.asList(TEST_AUTHOR1, TEST_AUTHOR2),
                BOOK_TITLE,
                Collections.singleton(TEST_GENRE));
    }

    private JSONObject createTestJsonBook(long id) throws JSONException {
        JSONObject bookJson = new JSONObject();
        bookJson.put("id", id);
        bookJson.put("authors", new JSONArray(Arrays.asList(AUTHOR1_ID, AUTHOR2_ID)));
        bookJson.put("title", BOOK_TITLE);
        bookJson.put("genres", new JSONArray(Collections.singletonList(GENRE_ID)));
        return bookJson;
    }

    @DisplayName("должен возвращать все")
    @Test
    void shouldGetAll() throws Exception {
        Book book0 = createTestBook();
        Book book1 = new Book(BOOK_ID + 1, Collections.emptyList(), "Title 1", Collections.emptySet());

        when(bookService.getAllBooks()).thenReturn(Arrays.asList(book0, book1));

        mockMvc.perform(get("/book").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(BOOK_ID))
                .andExpect(jsonPath("$[0].title").value(BOOK_TITLE))
                .andExpect(jsonPath("$[0].authors[0]").value(TEST_AUTHOR1.getId()))
                .andExpect(jsonPath("$[0].genres[0]").value(TEST_GENRE.getId()))
                .andExpect(jsonPath("$[1].id").value(book1.getId()))
                .andReturn();
    }

    @DisplayName("должен создавать книгу")
    @Test
    void shouldSaveBook() throws Exception {
        JSONObject bookJson = createTestJsonBook(0);
        when(bookService.createBook(any())).thenReturn(createTestBook());

        mockMvc.perform(post("/book")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(bookJson.toString())
        )
                .andExpect(status().isCreated())
                .andExpect(header().string("Location",
                        UriComponentsBuilder.newInstance()
                                .scheme("http")
                                .host("localhost")
                                .path("/book/{id}")
                                .buildAndExpand(BOOK_ID)
                                .toUriString()
                ))
                .andReturn();

        ArgumentCaptor<Book> argumentCaptor = ArgumentCaptor.forClass(Book.class);
        verify(bookService).createBook(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue())
                .hasFieldOrPropertyWithValue("id", 0L)
                .hasFieldOrPropertyWithValue("title", BOOK_TITLE);
    }

    @DisplayName("должен обновлять книгу")
    @Test
    void shouldUpdateBook() throws Exception {
        JSONObject bookJson = createTestJsonBook(BOOK_ID);
        mockMvc.perform(put("/book/" + BOOK_ID)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(bookJson.toString())
        )
                .andExpect(status().isOk())
                .andReturn();

        ArgumentCaptor<Book> argumentCaptor = ArgumentCaptor.forClass(Book.class);
        verify(bookService).updateBook(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue())
                .hasFieldOrPropertyWithValue("id", BOOK_ID)
                .hasFieldOrPropertyWithValue("title", BOOK_TITLE);
    }

    @DisplayName("должен удалять книгу")
    @Test
    void shouldDeleteBook() throws Exception {
        mockMvc.perform(delete("/book/" + BOOK_ID))
                .andExpect(status().isOk())
                .andReturn();

        verify(bookService).deleteById(BOOK_ID);
    }

    @DisplayName("должен возвращать ошибку, если обновляемой книги нет")
    @Test
    void shouldShowErrorIfUpdatedBookAbsent() throws Exception{
        JSONObject bookJson = createTestJsonBook(BOOK_ID);
        doThrow(BookNotFoundException.class).when(bookService).updateBook(any());
        mockMvc.perform(put("/book/" + BOOK_ID)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(bookJson.toString())
        )
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @DisplayName("должен возвращать ошибку, если удаляемой книги нет")
    @Test
    void shouldShowErrorIfDeletedBookAbsent() throws Exception {
        doThrow(BookNotFoundException.class).when(bookService).deleteById(BOOK_ID);
        mockMvc.perform(delete("/book/" + BOOK_ID))
                .andExpect(status().isNotFound())
                .andReturn();
    }
}