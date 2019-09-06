package ru.biderman.studenttest.dao;

import org.springframework.stereotype.Service;

import java.io.PrintStream;
import java.util.Optional;
import java.util.Scanner;
import java.util.function.Function;

@Service
public class UserInterfaceImpl implements UserInterface {
    private final PrintStream printStream;
    private final Scanner scanner;

    UserInterfaceImpl(UserInterfaceStreams userInterfaceStreams) {
        printStream = userInterfaceStreams.getPrintStream();
        scanner = new Scanner(userInterfaceStreams.getInputStream());
    }

    @Override
    public <T> Optional<T> readValue(String prompt, Function<String, InputChecker<T>> inputCheckerFunction) {
        printStream.println(prompt);
        while (true) {
            String s = scanner.nextLine();
            if("q".equals(s))
                return Optional.empty();

            InputChecker<T> value = inputCheckerFunction.apply(s);
            if (value.getCheckError().isPresent())
                printStream.println(value.getCheckError().get());
            else
                return value.getResult();
        }
    }
}
