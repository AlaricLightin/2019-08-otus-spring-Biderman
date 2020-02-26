package ru.biderman.swdataservice.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.biderman.swdataservice.model.Film;
import ru.biderman.swdataservice.services.FilmDataService;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FilmController.class)
@DisplayName("Контроллер информации о фильмах")
class FilmControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    FilmDataService filmDataService;

    private final static int FILM_ID = 100;
    private final static String FILM_TITLE = "Film title";
    private final static String FILM_DIRECTOR = "Film director";

    @DisplayName("должен показывать страницу")
    @Test
    void shouldShowPage() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attribute("showDetails", false))
                .andReturn();
    }

    @DisplayName("должен показывать результат поиска")
    @Test
    void shouldShowResult() throws Exception {
        Film film = new Film(FILM_TITLE, FILM_DIRECTOR, LocalDate.now());
        when(filmDataService.findById(FILM_ID)).thenReturn(Optional.of(film));

        MvcResult result = mockMvc.perform(post("/").flashAttr("id", FILM_ID))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attribute("showDetails", true))
                .andExpect(model().attribute("film", film))
                .andReturn();

        assertThat(result.getResponse().getContentAsString())
                .contains(FILM_TITLE)
                .contains(FILM_DIRECTOR);
    }
}