package ru.biderman.studenttest.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.biderman.studenttest.userinputoutput.UserInterfaceStreams;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class TestRunnerIntegrationTest {
    private static final String USER_NAME = "User User";
    private static final String CORRECT_ANSWER = "1";
    private static final Locale TEST_LOCALE = new Locale("en_US");
    private static final int QUESTION_COUNT = 1;
    private static final String EXIT_INPUT = "q";

    @MockBean
    UserInterfaceStreams userInterfaceStreams;

    @Autowired
    TestRunner testRunner;

    @Autowired
    MessageSource messageSource;

    @Test
    @DirtiesContext
    void correctAnswered() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        String inputString = USER_NAME + System.lineSeparator() + CORRECT_ANSWER + System.lineSeparator();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputString.getBytes());
        PrintStream printStream = new PrintStream(outputStream);

        when(userInterfaceStreams.getInputStream()).thenReturn(inputStream);
        when(userInterfaceStreams.getPrintStream()).thenReturn(printStream);

        testRunner.run();
        String testString = messageSource.getMessage(
                "test-run.result", new Object[]{USER_NAME, QUESTION_COUNT, QUESTION_COUNT}, TEST_LOCALE);
        assertTrue(outputStream.toString().contains(testString));
    }

    @Test
    @DirtiesContext
    void testCanceled() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(EXIT_INPUT.getBytes());
        PrintStream printStream = new PrintStream(outputStream);

        when(userInterfaceStreams.getInputStream()).thenReturn(inputStream);
        when(userInterfaceStreams.getPrintStream()).thenReturn(printStream);

        testRunner.run();
        String testString = messageSource.getMessage(
                "test-run.canceled", null, TEST_LOCALE);
        assertTrue(outputStream.toString().contains(testString));
    }
}
