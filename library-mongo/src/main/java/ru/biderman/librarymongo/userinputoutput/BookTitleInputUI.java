package ru.biderman.librarymongo.userinputoutput;

import org.springframework.context.MessageSource;
import ru.biderman.librarymongo.userinputoutput.exceptions.EmptyBookTitleException;
import ru.biderman.librarymongo.userinputoutput.exceptions.UserInputException;

import java.util.Locale;

class BookTitleInputUI implements DataInputUI<String>{
    @Override
    public String getPrompt(MessageSource messageSource, Locale locale) {
        return messageSource.getMessage("shell.book-title-prompt", null, locale);
    }

    @Override
    public String convertString(String s) throws UserInputException {
        if (!s.isEmpty())
            return s;
        else
            throw new EmptyBookTitleException();
    }
}
