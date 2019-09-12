package ru.biderman.studenttest.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import ru.biderman.studenttest.userinputoutput.UserInterface;

import java.util.Optional;

@PropertySource("classpath:application.properties")
@Service
public class TestRunner {
    private final NameService nameService;
    private final TestService testService;
    private final UserInterface userInterface;
    private final int questionCount;

    TestRunner(NameService nameService,
                      TestService testService,
                      UserInterface userInterface,
                      @Value("${questions.count}") int questionCount) {
        this.nameService = nameService;
        this.testService = testService;
        this.userInterface = userInterface;
        this.questionCount = questionCount;
    }

    public void run() {
        userInterface.printText("test-run.prompt");
        Optional<String> userName = nameService.getName();
        if (!userName.isPresent()) {
            userInterface.printText("test-run.canceled");
            return;
        }

        try {
            int result = testService.test(questionCount);
            userInterface.printText("test-run.result", new Object[]{userName.get(), result, questionCount});

        }
        catch (TestCanceledException e) {
            userInterface.printText("test-run.canceled");
        }
        catch (NotEnoughQuestionException e) {
            userInterface.printText("error.not-enough-questions");
        }
    }
}
