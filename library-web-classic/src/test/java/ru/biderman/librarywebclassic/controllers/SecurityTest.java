package ru.biderman.librarywebclassic.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.biderman.librarywebclassic.domain.Author;
import ru.biderman.librarywebclassic.domain.Book;
import ru.biderman.librarywebclassic.services.AuthorService;
import ru.biderman.librarywebclassic.services.DatabaseService;

import static org.mockito.Mockito.mock;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@DisplayName("Проверка безопасности ")
class SecurityTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    AuthorService authorService;

    @MockBean
    DatabaseService databaseService;

    @Nested
    @WithMockUser
    @DisplayName("должна блокировать обычного пользователя ")
    class BlockSimpleUser {
        @DisplayName("при заходе на страницы редактирования и удаления")
        @ParameterizedTest
        @ValueSource(strings = {"/books/edit?id=1", "/books/delete?id=1", "/authors/edit?id=1", "/authors/delete?id=1"})
        void shouldNotShowEditPages(String url) throws Exception {
            mockMvc.perform(get(url))
                    .andExpect(status().isForbidden())
                    .andReturn();
        }

        @DisplayName("при попытке редактировать книгу")
        @Test
        void shouldForbidEditBook() throws Exception {
            Book book = mock(Book.class);

            mockMvc.perform(post("/books/edit").flashAttr("book", book).with(csrf()))
                    .andExpect(status().isForbidden())
                    .andReturn();
        }

        @DisplayName("при попытке редактировать автора")
        @Test
        void shouldForbidEditAuthor() throws Exception {
            Author author = mock(Author.class);
            mockMvc.perform(post("/authors/edit").flashAttr("author", author).with(csrf()))
                    .andExpect(status().isForbidden())
                    .andReturn();
        }

        @DisplayName("при попытке удалить что-нибудь")
        @ParameterizedTest
        @ValueSource(strings = {"/books/delete?id=1", "/authors/delete?id=1"})
        void shouldForbidDelete(String url) throws Exception{
            mockMvc.perform(post(url).with(csrf()))
                    .andExpect(status().isForbidden())
                    .andReturn();
        }
    }

}
