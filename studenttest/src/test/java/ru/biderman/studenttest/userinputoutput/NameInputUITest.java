package ru.biderman.studenttest.userinputoutput;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.context.MessageSource;
import ru.biderman.studenttest.userinputoutput.exceptions.InvalidNameException;
import ru.biderman.studenttest.userinputoutput.exceptions.UserInputException;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class NameInputUITest {
    private final NameInputUI nameInputUI = new NameInputUI();

    @Test
    void getPrompt() {
        MessageSource messageSource = mock(MessageSource.class);
        when(messageSource.getMessage(eq(NameInputUI.PROMPT_ID), eq(null), any())).thenReturn("prompt");
        assertEquals("prompt", nameInputUI.getPrompt(messageSource, new Locale("ru-RU")));
    }

    @Test
    void convertStringSuccess() throws UserInputException {
        assertEquals("Name Surname", nameInputUI.convertString("Name Surname"));
    }

    @ParameterizedTest
    @EmptySource
    @ValueSource(strings = {"NameOnly"})
    void convertStringError(String s) {
        assertThrows(InvalidNameException.class, () -> nameInputUI.convertString(s));
    }
}