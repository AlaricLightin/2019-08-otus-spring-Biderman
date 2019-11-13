package ru.biderman.librarywebbackend.rest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.biderman.librarywebbackend.domain.Genre;
import ru.biderman.librarywebbackend.services.GenreService;

import java.util.Arrays;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Контроллер по работе с жанрами ")
@WebMvcTest(GenreController.class)
class GenreControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    GenreService genreService;

    @DisplayName("должен возвращать все жанры")
    @Test
    void shouldGetAll() throws Exception {
        Genre genre0 = new Genre(101, "Genre 1");
        Genre genre1 = new Genre(102, "Genre 1");
        when(genreService.getAllGenres()).thenReturn(Arrays.asList(genre0, genre1));

        mockMvc.perform(get("/genre").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(genre0.getId()))
                .andExpect(jsonPath("$[0].text").value(genre0.getText()))
                .andExpect(jsonPath("$[1].id").value(genre1.getId()))
                .andExpect(jsonPath("$[1].text").value(genre1.getText()))
                .andReturn();

    }
}