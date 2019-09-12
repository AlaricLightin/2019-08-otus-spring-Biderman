package ru.biderman.studenttest.userinputoutput;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import ru.biderman.studenttest.userinputoutput.exceptions.UserInputException;

import java.io.PrintStream;
import java.util.Locale;
import java.util.Optional;
import java.util.Scanner;

@PropertySource("classpath:application.properties")
@Service
public class UserInterfaceImpl implements UserInterface {
    static final String EXIT_INPUT = "q";

    private final PrintStream printStream;
    private final Scanner scanner;
    private final MessageSource messageSource;
    private final Locale locale;

    UserInterfaceImpl(UserInterfaceStreams userInterfaceStreams,
                      MessageSource messageSource,
                      @Value("${user-interface.locale}")Locale locale) {
        printStream = userInterfaceStreams.getPrintStream();
        scanner = new Scanner(userInterfaceStreams.getInputStream());
        this.messageSource = messageSource;
        this.locale = locale;
    }

    @Override
    public <T> Optional<T> readValue(DataInputUI<T> dataInputUI) {
        printStream.println(dataInputUI.getPrompt(messageSource, locale));
        while (true) {
            String s = scanner.nextLine();
            if(EXIT_INPUT.equals(s))
                return Optional.empty();

            try {
                T result = dataInputUI.convertString(s);
                return Optional.of(result);
            }
            catch (UserInputException e) {
                printStream.println(
                        messageSource.getMessage(e.getLocalizedMessageId(), e.getLocalizedMessageArgs(), locale));
            }
        }
    }

    @Override
    public void printText(String messageCode, Object[] args) {
        printStream.println(
                messageSource.getMessage(messageCode, args, locale)
        );
    }
}
