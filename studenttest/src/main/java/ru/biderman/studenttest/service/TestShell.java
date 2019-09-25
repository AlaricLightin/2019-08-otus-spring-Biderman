package ru.biderman.studenttest.service;

import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import ru.biderman.studenttest.domain.UserInterfaceProperties;
import ru.biderman.studenttest.userinputoutput.UserInterface;

@ShellComponent
public class TestShell {
    private static final int UNFINISHED_TEST = -1;

    private final UserInterface userInterface;
    private final UserInterfaceProperties userInterfaceProperties;
    private final TestService testService;
    private String userName = null;
    private String userSurname = null;
    private int testResult = UNFINISHED_TEST;

    public TestShell(UserInterface userInterface, UserInterfaceProperties userInterfaceProperties, TestService testService) {
        this.userInterface = userInterface;
        this.userInterfaceProperties = userInterfaceProperties;
        this.testService = testService;
    }

    @ShellMethod(value = "Login", key = {"login", "l"})
    String login(String userName, String userSurname) {
        this.userName = userName;
        this.userSurname = userSurname;

        return userInterface.getText("test-run.greeting", new String[]{userName, userSurname});
    }

    @ShellMethod(value = "Start test", key = {"start", "s"})
    @ShellMethodAvailability("isLoggedIn")
    void startTest() {
        userInterface.printText("test-run.prompt");
        try {
            int questionCount = userInterfaceProperties.getQuestionsCount();
            testResult = testService.test(questionCount);
        }
        catch (TestCanceledException e) {
            userInterface.printText("test-run.canceled");
            testResult = UNFINISHED_TEST;
        }
        catch (NotEnoughQuestionException e) {
            userInterface.printText("error.not-enough-questions");
            testResult = UNFINISHED_TEST;
        }
    }

    @ShellMethod(value = "Print test result", key = {"result", "r"})
    @ShellMethodAvailability("isTested")
    String printResult() {
        int questionCount = userInterfaceProperties.getQuestionsCount();
        return userInterface.getText("test-run.result", new Object[]{userName, userSurname, testResult, questionCount});
    }

    Availability isLoggedIn() {
        return userName != null && userSurname != null
                ? Availability.available()
                : Availability.unavailable("you are not logged in");
    }

    Availability isTested() {
        Availability resultAvailability = isLoggedIn();
        if (resultAvailability.isAvailable())
            return testResult >= 0
                    ? Availability.available()
                    : Availability.unavailable("you are need to run test");
        else
            return resultAvailability;
    }
}
