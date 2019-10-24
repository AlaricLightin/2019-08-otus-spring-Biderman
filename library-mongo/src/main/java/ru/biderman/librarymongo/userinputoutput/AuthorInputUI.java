package ru.biderman.librarymongo.userinputoutput;

import org.springframework.context.MessageSource;
import ru.biderman.librarymongo.domain.Author;
import ru.biderman.librarymongo.userinputoutput.exceptions.DuplicateAuthorUIException;
import ru.biderman.librarymongo.userinputoutput.exceptions.NoSuchAuthorException;
import ru.biderman.librarymongo.userinputoutput.exceptions.UserInputException;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

class AuthorInputUI implements DataInputUI<Author> {
    private final Map<String, Author> authorMap;

    AuthorInputUI(Map<String, Author> authorMap) {
        this.authorMap = authorMap;
    }

    @Override
    public String getPrompt(MessageSource messageSource, Locale locale) {
        return messageSource.getMessage("shell.author-prompt", null, locale);
    }

    @Override
    public Author convertString(String s) throws UserInputException {

        Author result = authorMap.get(s);
        if (result != null) {
            return result;
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
