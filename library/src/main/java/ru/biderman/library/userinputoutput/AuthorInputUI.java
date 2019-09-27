package ru.biderman.library.userinputoutput;

import org.springframework.context.MessageSource;
import ru.biderman.library.domain.Author;
import ru.biderman.library.userinputoutput.exceptions.DuplicateAuthorUIException;
import ru.biderman.library.userinputoutput.exceptions.NoSuchAuthorException;
import ru.biderman.library.userinputoutput.exceptions.UserInputException;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

class AuthorInputUI implements DataInputUI<Author> {
    private final Map<Long, Author> authorMap;

    AuthorInputUI(Map<Long, Author> authorMap) {
        this.authorMap = authorMap;
    }

    @Override
    public String getPrompt(MessageSource messageSource, Locale locale) {
        return messageSource.getMessage("shell.author-prompt", null, locale);
    }

    @Override
    public Author convertString(String s) throws UserInputException {
        long id;
        try {
            id = Long.parseLong(s);
        }
        catch (NumberFormatException e) {
            id = -1;
        }

        if (id >= 0) { // считаем, что если ввели число, то это id
            Author result = authorMap.get(id);
            if (result != null)
                return result;
            else
                throw new NoSuchAuthorException();
        }
        else {
            List<Author> resultList = authorMap.values().stream()
                    .filter(author -> s.equals(author.getSurname()))
                    .collect(Collectors.toList());

            switch (resultList.size()) {
                case 0:
                    throw new NoSuchAuthorException();

                case 1:
                    return resultList.get(0);

                default:
                    throw new DuplicateAuthorUIException(resultList);
            }
        }
    }
}
