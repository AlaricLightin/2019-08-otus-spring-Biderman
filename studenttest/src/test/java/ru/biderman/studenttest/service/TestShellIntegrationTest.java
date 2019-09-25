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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class TestShellIntegrationTest {
    private static final Locale TEST_LOCALE = new Locale("en_US");
    private static final String CORRECT_ANSWER = "1";
    private static final String USER_NAME = "Username";
    private static final String USER_SURNAME = "User-surname";
    private static final int QUESTION_COUNT = 1;
    private static final String EXIT_INPUT = "q";

    @MockBean
    UserInterfaceStreams userInterfaceStreams;

    @Autowired
    TestShell testShell;

    @Autowired
    MessageSource messageSource;

    private String getMessageSourceText(String messageCode, Object[] args) {
        return messageSource.getMessage(messageCode, args, TEST_LOCALE);
    }

    @Test
    @DirtiesContext
    void login() {
        assertEquals(
                getMessageSourceText("test-run.greeting", new String[]{USER_NAME, USER_SURNAME}),
                testShell.login(USER_NAME, USER_SURNAME));
    }

    @Test
    @DirtiesContext
    void fullCheckWithSuccess() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        String inputString = CORRECT_ANSWER + System.lineSeparator();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputString.getBytes());
        PrintStream printStream = new PrintStream(outputStream);

        when(userInterfaceStreams.getInputStream()).thenReturn(inputStream);
        when(userInterfaceStreams.getPrintStream()).thenReturn(printStream);

        assertFalse(testShell.isLoggedIn().isAvailable());
        testShell.login(USER_NAME, USER_SURNAME);
        assertTrue(testShell.isLoggedIn().isAvailable());
        assertFalse(testShell.isTested().isAvailable());

        testShell.startTest();
        assertTrue(testShell.isTested().isAvailable());
        String testString = getMessageSourceText(
                "test-run.result", new Object[]{USER_NAME, USER_SURNAME, QUESTION_COUNT, QUESTION_COUNT});
        assertEquals(testString, testShell.printResult());
    }

    @Test
    @DirtiesContext
    void checkCanceled() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(EXIT_INPUT.getBytes());
        PrintStream printStream = new PrintStream(outputStream);

        when(userInterfaceStreams.getInputStream()).thenReturn(inputStream);
        when(userInterfaceStreams.getPrintStream()).thenReturn(printStream);

        testShell.login(USER_NAME, USER_SURNAME);
        testShell.startTest();
        assertFalse(testShell.isTested().isAvailable());
    }
}