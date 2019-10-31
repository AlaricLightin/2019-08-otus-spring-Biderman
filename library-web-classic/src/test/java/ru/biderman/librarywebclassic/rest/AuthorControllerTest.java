package ru.biderman.librarywebclassic.rest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.biderman.librarywebclassic.domain.Author;
import ru.biderman.librarywebclassic.services.AuthorService;
import ru.biderman.librarywebclassic.services.exceptions.AuthorNotFoundException;
import ru.biderman.librarywebclassic.services.exceptions.DeleteAuthorException;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthorController.class)
@DisplayName("Контроллер для работы с авторами ")
class AuthorControllerTest {
    private static final long AUTHOR_ID = 200;
    private static final String AUTHOR_SURNAME = "Author-surname";
    private static final String AUTHOR_NAME = "Author-name";

    @Autowired
    MockMvc mockMvc;

    @MockBean
    AuthorService authorService;

    @DisplayName("должен показывать всех")
    @Test
    void shouldGetAll() throws Exception{
        final long authorId2 = AUTHOR_ID + 1;
        when(authorService.getAllAuthors()).thenReturn(Arrays.asList(
                new Author(AUTHOR_ID, AUTHOR_SURNAME, AUTHOR_NAME),
                new Author(authorId2, "Surname", "Name")
        ));
        when(authorService.getUsedAuthorIdList()).thenReturn(Collections.singletonList(authorId2));

        MvcResult result = mockMvc.perform(get("/authors"))
                .andExpect(status().isOk())
                .andExpect(view().name("authors"))
                .andExpect(model().attributeExists("authors"))
                .andExpect(model().attributeExists("usedAuthorIdList"))
                .andReturn();

        final String editFormat = "/authors/edit?id=%s";
        final String deleteFormat = "/authors/delete?id=%s";
        assertThat(result.getResponse().getContentAsString())
                .contains(String.valueOf(AUTHOR_ID))
                .contains(AUTHOR_NAME)
                .contains(AUTHOR_SURNAME)
                .contains(String.format(editFormat, AUTHOR_ID))
                .contains(String.format(deleteFormat, AUTHOR_ID))
                .doesNotContain(String.format(deleteFormat, authorId2));
    }

    @DisplayName("должен создавать окно для редактирования автора")
    @Test
    void shouldGetEditForm() throws Exception {
        Author author = new Author(AUTHOR_ID, AUTHOR_SURNAME, AUTHOR_NAME);
        when(authorService.findById(AUTHOR_ID)).thenReturn(author);

        MvcResult result = mockMvc.perform(get(String.format("/authors/edit?id=%s", AUTHOR_ID)))
                .andExpect(status().isOk())
                .andExpect(view().name("author-edit"))
                .andExpect(model().attribute("author", author))
                .andReturn();

        assertThat(result.getResponse().getContentAsString())
                .contains(AUTHOR_NAME)
                .contains(AUTHOR_SURNAME);
    }

    @DisplayName("должен создавать окно для добавления нового автора")
    @Test
    void shouldGetCreateForm() throws Exception {
        mockMvc.perform(get("/authors/edit?id=0"))
                .andExpect(status().isOk())
                .andExpect(view().name("author-edit"))
                .andExpect(model().attribute("author", hasProperty("surname", equalTo(""))))
                .andExpect(model().attribute("author", hasProperty("otherNames", equalTo(""))))
                .andReturn();

    }

    @DisplayName("должен сообщать об ошибке, если автора нет")
    @Test
    void shouldShowErrorIfEditedAuthorAbsent() throws Exception {
        doThrow(AuthorNotFoundException.class).when(authorService).findById(AUTHOR_ID);
        mockMvc.perform(get(String.format("/authors/edit?id=%s", AUTHOR_ID)))
                .andExpect(status().isNotFound())
                .andExpect(view().name("error"))
                .andReturn();
    }

    @DisplayName("должен принимать информацию о новом авторе")
    @Test
    void shouldReceivePostedNewAuthor() throws Exception {
        Author author = new Author(0, AUTHOR_SURNAME, AUTHOR_NAME);
        mockMvc.perform(post("/authors/edit").flashAttr("author", author))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/authors"))
                .andReturn();

        verify(authorService).addAuthor(author);
    }

    @DisplayName("должен принимать информацию об отредактированном авторе")
    @Test
    void shouldReceivePostedUpdatedAuthor() throws Exception {
        Author author = new Author(AUTHOR_ID, AUTHOR_SURNAME, AUTHOR_NAME);
        mockMvc.perform(post("/authors/edit").flashAttr("author", author))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/authors"))
                .andReturn();

        verify(authorService).updateAuthor(AUTHOR_ID, author);
    }

    @DisplayName("должен удалять автора")
    @Test
    void shouldDeleteAuthor() throws Exception {
        mockMvc.perform(get(String.format("/authors/delete?id=%s", AUTHOR_ID)))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/authors"))
                .andReturn();

        verify(authorService).deleteById(AUTHOR_ID);
    }

    @DisplayName("должен сообщать об ошибке, если нельзя удалить автора")
    @Test
    void shouldShowErrorIfCouldNotDelete() throws Exception {
        doThrow(DeleteAuthorException.class).when(authorService).deleteById(AUTHOR_ID);

        mockMvc.perform(get(String.format("/authors/delete?id=%s", AUTHOR_ID)))
                .andExpect(status().isForbidden())
                .andReturn();
    }
}