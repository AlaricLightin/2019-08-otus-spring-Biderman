package ru.biderman.studenttest.service;

import org.junit.jupiter.api.Test;
import ru.biderman.studenttest.userinputoutput.UserInterface;

import java.util.Optional;

import static org.mockito.Mockito.*;

class TestRunnerTest {
    private final int QUESTION_COUNT = 2;

    @Test
    void run() throws NotEnoughQuestionException, TestCanceledException{
        final int ANSWER_COUNT = 1;

        UserInterface userInterface = mock(UserInterface.class);

        TestService testService = mock(TestService.class);
        when(testService.test(QUESTION_COUNT)).thenReturn(ANSWER_COUNT);

        final String USER = "User User";
        NameService nameService = mock(NameService.class);
        when(nameService.getName()).thenReturn(Optional.of(USER));

        TestRunner testRunner = new TestRunner(nameService, testService, userInterface, QUESTION_COUNT);
        testRunner.run();
        verify(userInterface).printText("test-run.prompt");
        verify(userInterface).printText("test-run.result", new Object[]{USER, ANSWER_COUNT, QUESTION_COUNT});
    }

    @Test
    void runNameCanceled() {
        UserInterface userInterface = mock(UserInterface.class);
        TestService testService = mock(TestService.class);
        NameService nameService = mock(NameService.class);
        when(nameService.getName()).thenReturn(Optional.empty());
        TestRunner testRunner = new TestRunner(nameService, testService, userInterface, QUESTION_COUNT);
        testRunner.run();
        verify(userInterface).printText("test-run.prompt");
        verify(userInterface).printText("test-run.canceled");
    }

    @Test
    void testQuestionCanceled() throws NotEnoughQuestionException, TestCanceledException{
        UserInterface userInterface = mock(UserInterface.class);
        TestService testService = mock(TestService.class);
        when(testService.test(QUESTION_COUNT)).thenThrow(TestCanceledException.class);

        NameService nameService = mock(NameService.class);
        when(nameService.getName()).thenReturn(Optional.empty());

        TestRunner testRunner = new TestRunner(nameService, testService, userInterface, QUESTION_COUNT);
        testRunner.run();

        verify(userInterface).printText("test-run.prompt");
        verify(userInterface).printText("test-run.canceled");
    }
}