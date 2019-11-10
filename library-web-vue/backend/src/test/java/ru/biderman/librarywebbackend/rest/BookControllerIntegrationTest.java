package ru.biderman.librarywebbackend.rest;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import ru.biderman.librarywebbackend.domain.Author;
import ru.biderman.librarywebbackend.domain.Genre;

import java.util.Collections;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Контроллеры при интеграционном тесте ")
class BookControllerIntegrationTest {
    @Autowired
    MockMvc mockMvc;

    private static final Genre TEST_GENRE = new Genre(1, "Test-genre");
    private static final Author TEST_AUTHOR1 = new Author(1, "Ivanov", "Ivan Ivanovich");
    private static final Author TEST_AUTHOR2 = new Author(2, "Smith", "John");
    private static final long BOOK_ID = 1;
    private static final String BOOK_TITLE = "Book Name";

    @DisplayName("должен возвращать все")
    @Test
    void shouldGetAll() throws Exception{
        mockMvc.perform(get("/book").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(BOOK_ID))
                .andExpect(jsonPath("$[0].title").value(BOOK_TITLE))
                .andExpect(jsonPath("$[0].authors[0]").value(TEST_AUTHOR1.getId()))
                .andExpect(jsonPath("$[0].authors[1]").value(TEST_AUTHOR2.getId()))
                .andExpect(jsonPath("$[0].genres[0]").value(TEST_GENRE.getId()))
                .andReturn();
    }

    @DisplayName("должен добавлять книгу")
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void shouldAddBook() throws Exception {
        final String newBookTitle = "New Book Title";
        JSONObject bookJson = new JSONObject();
        bookJson.put("authors", new JSONArray(Collections.singletonList(TEST_AUTHOR2.getId())));
        bookJson.put("title", newBookTitle);
        bookJson.put("genres", new JSONArray(Collections.singletonList(TEST_GENRE.getId())));

        mockMvc.perform(post("/book")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(bookJson.toString())
        )
                .andExpect(status().isCreated())
                .andReturn();

        mockMvc.perform(get("/book").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(content().string(containsString(newBookTitle)))
                .andReturn();
    }

    @DisplayName("должен удалять книгу")
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void shouldDeleteBook() throws Exception {
        mockMvc.perform(delete("/book/" + BOOK_ID))
                .andExpect(status().isOk())
                .andReturn();

        mockMvc.perform(get("/book").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(0)))
                .andReturn();
    }

    @DisplayName("должен редактировать книгу")
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void shouldEditBook() throws Exception {
        final String newBookTitle = "New Book Title";
        JSONObject bookJson = new JSONObject();
        bookJson.put("authors", new JSONArray(Collections.singletonList(TEST_AUTHOR2.getId())));
        bookJson.put("title", newBookTitle);
        bookJson.put("genres", new JSONArray(Collections.singletonList(TEST_GENRE.getId())));

        mockMvc.perform(put("/book/" + BOOK_ID)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(bookJson.toString())
        )
                .andExpect(status().isOk())
                .andReturn();

        mockMvc.perform(get("/book").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(content().string(containsString(newBookTitle)))
                .andReturn();
    }

    @DisplayName("должен возвращать ошибку при удалении несуществующей книги")
    @Test
    void shouldGetErrorIfDeletedBookIsAbsent() throws Exception {
        mockMvc.perform(delete("/book/" + (BOOK_ID + 1)))
                .andExpect(status().isNotFound())
                .andReturn();
    }
}