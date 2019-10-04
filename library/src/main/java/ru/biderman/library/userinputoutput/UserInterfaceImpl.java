package ru.biderman.library.userinputoutput;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import ru.biderman.library.userinputoutput.exceptions.UserInputException;

import java.io.PrintStream;
import java.util.Locale;
import java.util.Optional;
import java.util.Scanner;

@Service
public class UserInterfaceImpl implements UserInterface {
    private final MessageSource messageSource;
    private final Locale locale = new Locale("en_US");
    private final UserInterfaceStreams userInterfaceStreams;
    private Scanner scanner;

    UserInterfaceImpl(UserInterfaceStreams userInterfaceStreams,
                      MessageSource messageSource) {
        this.messageSource = messageSource;
        this.userInterfaceStreams = userInterfaceStreams;
    }

    private PrintStream getPrintStream() {
        return userInterfaceStreams.getPrintStream();
    }

    private Scanner getScanner() {
        if (scanner == null) {
            synchronized (this) {
                if (scanner == null)
                    scanner = new Scanner(userInterfaceStreams.getInputStream());
            }
        }

        return scanner;
    }

    @Override
    public <T> Optional<T> readValue(DataInputUI<T> dataInputUI, String exitString) {
        getPrintStream().println(dataInputUI.getPrompt(messageSource, locale));
        while (true) {
            String s = getScanner().nextLine();
            if(exitString != null && exitString.equals(s))
                return Optional.empty();

            try {
                T result = dataInputUI.convertString(s);
                return Optional.of(result);
            }
            catch (UserInputException e) {
                printText(e.getLocalizedMessageId(), e.getLocalizedMessageArgs());
            }
        }
    }

    @Override
    public void printText(String messageCode, Object[] args) {
        getPrintStream().println(getText(messageCode, args));
    }

    @Override
    public String getText(String messageCode, Object[] args) {
        return messageSource.getMessage(messageCode, args, locale);
    }
}
