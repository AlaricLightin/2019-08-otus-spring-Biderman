package ru.biderman.studenttest.dao;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserInterfaceImplTest {

    private UserInterfaceStreams createMockUIStreams(String inputString, ByteArrayOutputStream outputStream) {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputString.getBytes());
        PrintStream printStream = new PrintStream(outputStream);

        UserInterfaceStreams result = mock(UserInterfaceStreams.class);
        when(result.getInputStream()).thenReturn(inputStream);
        when(result.getPrintStream()).thenReturn(printStream);

        return result;
    }

    private static class InputCheckerTest extends InputChecker<String> {
        private String s;

        InputCheckerTest(String s) {
            this.s = s;
        }

        @Override
        public Optional<String> getCheckError() {
            if ("1".equals(s))
                return Optional.empty();
            else
                return Optional.of("Error");
        }

        @Override
        public Optional<String> getResult() {
            if ("1".equals(s))
                return Optional.of("1");
            else
                return Optional.empty();
        }
    }

    @Test
    void readValueSimple() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        UserInterfaceStreams userInterfaceStreams = createMockUIStreams("1", outputStream);
        UserInterface userInterface = new UserInterfaceImpl(userInterfaceStreams);

        Optional<String> s = userInterface.readValue("Prompt", InputCheckerTest::new);
        assertEquals("Prompt" + System.lineSeparator(),
                outputStream.toString());
        assertTrue(s.isPresent());
        assertEquals("1", s.get());
    }

    @Test
    void readValueExit() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        UserInterfaceStreams userInterfaceStreams = createMockUIStreams("q", outputStream);
        UserInterface userInterface = new UserInterfaceImpl(userInterfaceStreams);

        Optional<String> s = userInterface.readValue("Prompt", InputCheckerTest::new);
        assertEquals("Prompt" + System.lineSeparator(),
                outputStream.toString());
        assertFalse(s.isPresent());
    }

    @Test
    void errorEnterAndExit() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        UserInterfaceStreams userInterfaceStreams = createMockUIStreams("2\nq", outputStream);
        UserInterface userInterface = new UserInterfaceImpl(userInterfaceStreams);

        Optional<String> s = userInterface.readValue("Prompt", InputCheckerTest::new);
        assertEquals("Prompt" + System.lineSeparator() +
                "Error" + System.lineSeparator(),
                outputStream.toString());
        assertFalse(s.isPresent());
    }

    @Test
    void errorAndRightAnswer() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        UserInterfaceStreams userInterfaceStreams = createMockUIStreams("2\n1", outputStream);
        UserInterface userInterface = new UserInterfaceImpl(userInterfaceStreams);

        Optional<String> s = userInterface.readValue("Prompt", InputCheckerTest::new);
        assertEquals("Prompt" + System.lineSeparator() +
                        "Error" + System.lineSeparator(),
                outputStream.toString());
        assertTrue(s.isPresent());
    }
}