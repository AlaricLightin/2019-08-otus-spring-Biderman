package ru.biderman.library.userinputoutput;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.context.MessageSource;
import ru.biderman.library.domain.Genre;
import ru.biderman.library.userinputoutput.exceptions.NoSuchGenreException;
import ru.biderman.library.userinputoutput.exceptions.UserInputException;

import java.util.HashMap;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("Интерфейс для ввода жанра ")
class GenreInputUITest {
    @DisplayName("должен генерировать строку-приглашение")
    @Test
    void shouldGetPrompt() {
        final String messageCode = "shell.genre-prompt";
        final Locale locale = new Locale("en_US");
        final String resultPrompt = "Prompt 1";

        GenreInputUI genreInputUI = new GenreInputUI(null);
        MessageSource messageSource = mock(MessageSource.class);
        when(messageSource.getMessage(messageCode, null, locale)).thenReturn(resultPrompt);

        assertEquals(resultPrompt, genreInputUI.getPrompt(messageSource, locale));
    }

    @Nested
    class Convert {
        private final long GENRE_ID = 1;
        private final String GENRE_TITLE = "Some genre";

        private HashMap<Long, Genre> genreMap;
        private GenreInputUI genreInputUI;

        @BeforeEach
        void init() {
            genreMap = new HashMap<>();
            genreInputUI = new GenreInputUI(genreMap);
        }

        @DisplayName("должен получать жанр по id")
        @Test
        void shouldGetGenreById() throws UserInputException {
            Genre genre = new Genre(GENRE_ID, GENRE_TITLE);
            genreMap.put(GENRE_ID, genre);

            assertEquals(genre, genreInputUI.convertString(String.valueOf(GENRE_ID)));
        }

        @DisplayName("должен получать жанр по названию")
        @Test
        void shouldGetGenreByTitle() throws UserInputException {
            Genre genre = new Genre(GENRE_ID, GENRE_TITLE);
            genreMap.put(GENRE_ID, genre);

            assertEquals(genre, genreInputUI.convertString(GENRE_TITLE));
        }

        @DisplayName("должен бросать исключение, если такого жанра нет")
        @ParameterizedTest
        @ValueSource(strings = {"2", "Another genre"})
        void shouldThrowExceptionIfNoSuchGenre(String s) {
            assertThrows(NoSuchGenreException.class, () -> genreInputUI.convertString(s));
        }
    }
}