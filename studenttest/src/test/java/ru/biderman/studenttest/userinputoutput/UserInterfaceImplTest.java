package ru.biderman.studenttest.userinputoutput;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.context.MessageSource;
import ru.biderman.studenttest.domain.UserInterfaceProperties;
import ru.biderman.studenttest.userinputoutput.exceptions.UserInputException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Locale;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserInterfaceImplTest {
    private static MessageSource messageSource;
    private static final Locale locale = new Locale("ru-RU");

    private static final String CORRECT_INPUT = "correct_input";
    private static final String INCORRECT_INPUT = "incorrect_input";
    private static final String INCORRECT_INPUT_ERROR_ID = "message.incorrect_input";
    private static final String INCORRECT_INPUT_ERROR_MESSAGE = "Incorrect input error";
    private static final Integer[] INCORRECT_INPUT_ARGS = new Integer[]{1, 2};

    private static final String PROMPT = "prompt";

    private UserInterfaceStreams createMockUIStreams(String inputString, ByteArrayOutputStream outputStream) {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputString.getBytes());
        PrintStream printStream = new PrintStream(outputStream);

        UserInterfaceStreams result = mock(UserInterfaceStreams.class);
        when(result.getInputStream()).thenReturn(inputStream);
        when(result.getPrintStream()).thenReturn(printStream);

        return result;
    }

    @BeforeAll
    static void initMessageSource() {
        messageSource = mock(MessageSource.class);
        when(messageSource.getMessage(INCORRECT_INPUT_ERROR_ID, INCORRECT_INPUT_ARGS, locale))
                .thenReturn(INCORRECT_INPUT_ERROR_MESSAGE);
    }

    private UserInterface createUserInterface(UserInterfaceStreams userInterfaceStreams) {
        UserInterfaceProperties userInterfaceProperties = mock(UserInterfaceProperties.class);
        when(userInterfaceProperties.getLocale()).thenReturn(new Locale("ru-RU"));
        return new UserInterfaceImpl(userInterfaceStreams, userInterfaceProperties, messageSource);
    }

    private static class TestDataInputUI implements DataInputUI<String> {
        @Override
        public String getPrompt(MessageSource messageSource, Locale locale) {
            return PROMPT;
        }

        @Override
        public String convertString(String s) throws UserInputException {
            if (CORRECT_INPUT.equals(s))
                return s;
            else
                throw new UserInputException() {
                    @Override
                    public String getLocalizedMessageId() {
                        return INCORRECT_INPUT_ERROR_ID;
                    }

                    @Override
                    public Object[] getLocalizedMessageArgs() {
                        return INCORRECT_INPUT_ARGS;
                    }
                };
        }
    }

    @Test
    void readOnlyCorrectValue() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        UserInterfaceStreams userInterfaceStreams = createMockUIStreams(CORRECT_INPUT, outputStream);
        UserInterface userInterface = createUserInterface(userInterfaceStreams);

        Optional<String> s = userInterface.readValue(new TestDataInputUI());
        assertEquals(PROMPT + System.lineSeparator(),
                outputStream.toString());
        assertThat(s).hasValue(CORRECT_INPUT);
    }

    @Test
    void readExit() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        UserInterfaceStreams userInterfaceStreams = createMockUIStreams(UserInterfaceImpl.EXIT_INPUT, outputStream);
        UserInterface userInterface = createUserInterface(userInterfaceStreams);

        Optional<String> s = userInterface.readValue(new TestDataInputUI());
        assertEquals(PROMPT + System.lineSeparator(),
                outputStream.toString());
        assertFalse(s.isPresent());
    }

    @Test
    void errorEnterAndExit() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        UserInterfaceStreams userInterfaceStreams = createMockUIStreams(
                INCORRECT_INPUT + "\n" + UserInterfaceImpl.EXIT_INPUT, outputStream);
        UserInterface userInterface = createUserInterface(userInterfaceStreams);

        Optional<String> s = userInterface.readValue(new TestDataInputUI());
        assertEquals(PROMPT + System.lineSeparator() +
                INCORRECT_INPUT_ERROR_MESSAGE + System.lineSeparator(),
                outputStream.toString());
        assertFalse(s.isPresent());
    }

    @Test
    void errorAndRightAnswer() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        UserInterfaceStreams userInterfaceStreams = createMockUIStreams(
                INCORRECT_INPUT + "\n" + CORRECT_INPUT, outputStream);
        UserInterface userInterface = createUserInterface(userInterfaceStreams);

        Optional<String> s = userInterface.readValue(new TestDataInputUI());
        assertEquals(PROMPT + System.lineSeparator() +
                        INCORRECT_INPUT_ERROR_MESSAGE + System.lineSeparator(),
                outputStream.toString());
        assertThat(s).hasValue(CORRECT_INPUT);
    }

    @Test
    void printText() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        UserInterfaceStreams userInterfaceStreams = createMockUIStreams("", outputStream);
        UserInterface userInterface = createUserInterface(userInterfaceStreams);
        userInterface.printText(INCORRECT_INPUT_ERROR_ID, INCORRECT_INPUT_ARGS);
        assertEquals(INCORRECT_INPUT_ERROR_MESSAGE + System.lineSeparator(), outputStream.toString());
    }
}