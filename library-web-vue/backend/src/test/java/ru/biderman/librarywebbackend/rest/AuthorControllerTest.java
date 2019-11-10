package ru.biderman.librarywebbackend.rest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.biderman.librarywebbackend.domain.Author;
import ru.biderman.librarywebbackend.services.AuthorService;

import java.util.Arrays;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Контроллер для работы с авторами ")
@WebMvcTest(AuthorController.class)
class AuthorControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    AuthorService authorService;

    @DisplayName("должен возвращать всех")
    @Test
    void shouldGetAll() throws Exception{
        Author author0 = new Author(101, "Author1", "Name");
        Author author1 = new Author(102, "Author2", "Name2");

        when(authorService.getAllAuthors()).thenReturn(Arrays.asList(author0, author1));

        mockMvc.perform(get("/author").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(author0.getId()))
                .andExpect(jsonPath("$[0].surname").value(author0.getSurname()))
                .andExpect(jsonPath("$[0].otherNames").value(author0.getOtherNames()))
                .andExpect(jsonPath("$[1].id").value(author1.getId()))
                .andExpect(jsonPath("$[1].surname").value(author1.getSurname()))
                .andExpect(jsonPath("$[1].otherNames").value(author1.getOtherNames()))
                .andReturn();
    }
}