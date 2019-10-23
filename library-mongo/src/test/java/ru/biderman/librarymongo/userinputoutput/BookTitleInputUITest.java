package ru.biderman.librarymongo.userinputoutput;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.MessageSource;
import ru.biderman.librarymongo.userinputoutput.exceptions.EmptyBookTitleException;
import ru.biderman.librarymongo.userinputoutput.exceptions.UserInputException;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("Интерфейс для ввода названия книги")
class BookTitleInputUITest {
    @DisplayName("должен генерировать строку-приглашение")
    @Test
    void shouldGetPrompt() {
        final String messageCode = "shell.book-title-prompt";
        final Locale locale = new Locale("en_US");
        final String resultPrompt = "Prompt";

        BookTitleInputUI bookTitleInputUI = new BookTitleInputUI();
        MessageSource messageSource = mock(MessageSource.class);
        when(messageSource.getMessage(messageCode, null, locale)).thenReturn(resultPrompt);

        assertEquals(resultPrompt, bookTitleInputUI.getPrompt(messageSource, locale));
    }

    @DisplayName("должен возвращать название, если оно не пусто")
    @Test
    void shouldGetTitleIfNotEmpty() throws UserInputException {
        final String TITLE = "Title";
        BookTitleInputUI bookTitleInputUI = new BookTitleInputUI();
        assertEquals(TITLE, bookTitleInputUI.convertString(TITLE));
    }

    @DisplayName("должен бросать исключение, если ввод пустой")
    @Test
    void shouldThrowExceptonIfEmpty() {
        BookTitleInputUI bookTitleInputUI = new BookTitleInputUI();
        assertThrows(EmptyBookTitleException.class, () -> bookTitleInputUI.convertString(""));
    }
}