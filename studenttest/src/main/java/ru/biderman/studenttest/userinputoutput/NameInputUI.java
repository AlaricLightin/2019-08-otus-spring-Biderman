package ru.biderman.studenttest.userinputoutput;

import org.springframework.context.MessageSource;
import ru.biderman.studenttest.userinputoutput.exceptions.InvalidNameException;
import ru.biderman.studenttest.userinputoutput.exceptions.UserInputException;

import java.util.Locale;

public class NameInputUI implements DataInputUI<String> {
    final static String PROMPT_ID = "prompt.name";

    @Override
    public String getPrompt(MessageSource messageSource, Locale locale) {
        return messageSource.getMessage(PROMPT_ID, null, locale);
    }

    @Override
    public String convertString(String s) throws UserInputException {
        assert s != null;

        String[] nameArray = s.split(" ");
        if (nameArray.length >= 2)
            return s;
        else
            throw new InvalidNameException();
    }
}
