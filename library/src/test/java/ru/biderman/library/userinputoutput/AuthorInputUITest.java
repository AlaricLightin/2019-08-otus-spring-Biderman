package ru.biderman.library.userinputoutput;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.context.MessageSource;
import ru.biderman.library.domain.Author;
import ru.biderman.library.userinputoutput.exceptions.DuplicateAuthorUIException;
import ru.biderman.library.userinputoutput.exceptions.NoSuchAuthorException;
import ru.biderman.library.userinputoutput.exceptions.UserInputException;

import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("Интерфейс для ввода автора ")
class AuthorInputUITest {
    @DisplayName("должен генерировать строку-приглашение")
    @Test
    void shouldGetPrompt() {
        final String messageCode = "shell.author-prompt";
        final Locale locale = new Locale("en_US");
        final String resultPrompt = "Prompt 1";

        AuthorInputUI authorInputUI = new AuthorInputUI(null);
        MessageSource messageSource = mock(MessageSource.class);
        when(messageSource.getMessage(messageCode, null, locale)).thenReturn(resultPrompt);

        assertEquals(resultPrompt, authorInputUI.getPrompt(messageSource, locale));
    }

    @Nested
    class Convert {
        private final long AUTHOR_ID = 1;
        private final String AUTHOR_SURNAME = "Surname";
        private final String AUTHOR_NAME = "Name";

        private HashMap<Long, Author> authorMap;
        private AuthorInputUI authorInputUI;

        @BeforeEach
        void init() {
            authorMap = new HashMap<>();
            authorInputUI = new AuthorInputUI(authorMap);
        }

        @DisplayName("должен получать автора по id")
        @Test
        void shouldGetAuthorById() throws UserInputException {
            Author author = new Author(AUTHOR_ID, AUTHOR_SURNAME, AUTHOR_NAME);
            authorMap.put(AUTHOR_ID, author);

            assertEquals(author, authorInputUI.convertString(String.valueOf(AUTHOR_ID)));
        }

        @DisplayName("должен получать автора по фамилии")
        @Test
        void shouldGetAuthorBySurname() throws UserInputException {
            Author author = new Author(AUTHOR_ID, AUTHOR_SURNAME, AUTHOR_NAME);
            authorMap.put(AUTHOR_ID, author);

            assertEquals(author, authorInputUI.convertString(AUTHOR_SURNAME));
        }

        @DisplayName("должен бросать исключение при нескольких авторах с одной фамилией")
        @Test
        void shouldThrowExceptionIfSeveralAuthors() {
            authorMap.put(AUTHOR_ID, new Author(AUTHOR_ID, AUTHOR_SURNAME, AUTHOR_NAME));
            authorMap.put(2L, new Author(2, AUTHOR_SURNAME, "Another-name"));

            assertThrows(DuplicateAuthorUIException.class, () -> authorInputUI.convertString(AUTHOR_SURNAME));
        }

        @DisplayName("должен бросать исключение, если такого автора нет")
        @ParameterizedTest
        @ValueSource(strings = {"Another-surname", "2"})
        void shouldThrowExceptionIfNoSuchAuthor(String s) {
            assertThrows(NoSuchAuthorException.class, () -> authorInputUI.convertString(s));
        }
    }


}