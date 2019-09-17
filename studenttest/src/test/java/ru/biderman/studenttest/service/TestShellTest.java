package ru.biderman.studenttest.service;

import org.junit.jupiter.api.Test;
import ru.biderman.studenttest.domain.UserInterfaceProperties;
import ru.biderman.studenttest.userinputoutput.UserInterface;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class TestShellTest {

    @Test
    void login() {
        UserInterface userInterface = mock(UserInterface.class);
        when(userInterface.getText(eq("test-run.greeting"), any())).thenAnswer(
                invocationOnMock -> {
                    Object[] args = invocationOnMock.getArguments();
                    if (args.length != 2)
                        fail();

                    if (!(args[1] instanceof String[]))
                        fail();

                    String[] strings = (String []) args[1];
                    return strings[0] + " " + strings[1];
                }
        );

        TestShell testShell = new TestShell(userInterface, null, null);
        assertEquals("Name Surname", testShell.login("Name", "Surname"));
    }

    @Test
    void startTestWithSuccess() throws Exception{
        final int QUESTION_COUNT = 5;
        UserInterface userInterface = mock(UserInterface.class);

        UserInterfaceProperties userInterfaceProperties = mock(UserInterfaceProperties.class);
        when(userInterfaceProperties.getQuestionsCount()).thenReturn(QUESTION_COUNT);

        TestService testService = mock(TestService.class);

        TestShell testShell = new TestShell(userInterface, userInterfaceProperties, testService);

        testShell.startTest();
        verify(userInterface).printText("test-run.prompt");
        verify(testService).test(QUESTION_COUNT);
    }

    @Test
    void startTestButCancel() throws Exception {
        final int QUESTION_COUNT = 5;
        UserInterface userInterface = mock(UserInterface.class);

        UserInterfaceProperties userInterfaceProperties = mock(UserInterfaceProperties.class);
        when(userInterfaceProperties.getQuestionsCount()).thenReturn(QUESTION_COUNT);

        TestService testService = mock(TestService.class);
        when(testService.test(QUESTION_COUNT)).thenThrow(TestCanceledException.class);

        TestShell testShell = new TestShell(userInterface, userInterfaceProperties, testService);

        testShell.startTest();
        verify(userInterface).printText("test-run.prompt");
        verify(testService).test(QUESTION_COUNT);
        verify(userInterface).printText("test-run.canceled");
    }

    @Test
    void startTestButNotEnoughQuestion() throws Exception {
        final int QUESTION_COUNT = 5;
        UserInterface userInterface = mock(UserInterface.class);

        UserInterfaceProperties userInterfaceProperties = mock(UserInterfaceProperties.class);
        when(userInterfaceProperties.getQuestionsCount()).thenReturn(QUESTION_COUNT);

        TestService testService = mock(TestService.class);
        when(testService.test(QUESTION_COUNT)).thenThrow(NotEnoughQuestionException.class);

        TestShell testShell = new TestShell(userInterface, userInterfaceProperties, testService);

        testShell.startTest();
        verify(userInterface).printText("test-run.prompt");
        verify(testService).test(QUESTION_COUNT);
        verify(userInterface).printText("error.not-enough-questions");
    }

    @Test
    void printResult() throws Exception{
        final int QUESTION_COUNT = 5;
        final int ANSWER_COUNT = 3;
        final String USER_NAME = "Username";
        final String USER_SURNAME = "User-surname";
        UserInterface userInterface = mock(UserInterface.class);
        when(userInterface.getText(eq("test-run.result"), any())).thenAnswer(
                invocationOnMock -> {
                    Object[] args = invocationOnMock.getArguments();
                    if (args.length != 2)
                        fail();

                    return Stream.of((Object[]) args[1])
                            .map(Object::toString)
                            .collect(Collectors.joining(" "));
                }
        );

        UserInterfaceProperties userInterfaceProperties = mock(UserInterfaceProperties.class);
        when(userInterfaceProperties.getQuestionsCount()).thenReturn(QUESTION_COUNT);

        TestService testService = mock(TestService.class);
        when(testService.test(QUESTION_COUNT)).thenReturn(ANSWER_COUNT);

        TestShell testShell = new TestShell(userInterface, userInterfaceProperties, testService);
        testShell.login(USER_NAME, USER_SURNAME);
        testShell.startTest();
        assertEquals(String.join(" ", USER_NAME, USER_SURNAME, String.valueOf(ANSWER_COUNT), String.valueOf(QUESTION_COUNT)),
                testShell.printResult());
    }

    @Test
    void isLoggedIn() {
        UserInterface userInterface = mock(UserInterface.class);
        TestShell testShell = new TestShell(userInterface, null, null);

        assertFalse(testShell.isLoggedIn().isAvailable());
        testShell.login("Username", "User-surname");
        assertTrue(testShell.isLoggedIn().isAvailable());
    }

    @Test
    void isTested() throws Exception{
        final int QUESTION_COUNT = 5;
        final int ANSWER_COUNT = 3;

        UserInterface userInterface = mock(UserInterface.class);

        UserInterfaceProperties userInterfaceProperties = mock(UserInterfaceProperties.class);
        when(userInterfaceProperties.getQuestionsCount()).thenReturn(QUESTION_COUNT);

        TestService testService = mock(TestService.class);
        when(testService.test(QUESTION_COUNT)).thenReturn(ANSWER_COUNT);

        TestShell testShell = new TestShell(userInterface, userInterfaceProperties, testService);
        assertFalse(testShell.isTested().isAvailable());
        testShell.login("Username", "User-surname");
        assertFalse(testShell.isTested().isAvailable());
        testShell.startTest();
        assertTrue(testShell.isTested().isAvailable());
    }

    @Test
    void isTestedWithCancel() throws Exception{
        final int QUESTION_COUNT = 5;

        UserInterface userInterface = mock(UserInterface.class);

        UserInterfaceProperties userInterfaceProperties = mock(UserInterfaceProperties.class);
        when(userInterfaceProperties.getQuestionsCount()).thenReturn(QUESTION_COUNT);

        TestService testService = mock(TestService.class);
        when(testService.test(QUESTION_COUNT)).thenThrow(TestCanceledException.class);

        TestShell testShell = new TestShell(userInterface, userInterfaceProperties, testService);
        assertFalse(testShell.isTested().isAvailable());
        testShell.login("Username", "User-surname");
        assertFalse(testShell.isTested().isAvailable());
        testShell.startTest();
        assertFalse(testShell.isTested().isAvailable());
    }
}