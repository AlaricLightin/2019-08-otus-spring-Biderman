package ru.biderman.library.userinputoutput;

import org.springframework.context.MessageSource;
import ru.biderman.library.domain.Genre;
import ru.biderman.library.userinputoutput.exceptions.NoSuchGenreException;
import ru.biderman.library.userinputoutput.exceptions.UserInputException;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

class GenreInputUI implements DataInputUI<Genre> {
    private final Map<Long, Genre> genreMap;

    GenreInputUI(Map<Long, Genre> genreMap) {
        this.genreMap = genreMap;
    }

    @Override
    public String getPrompt(MessageSource messageSource, Locale locale) {
        return messageSource.getMessage("shell.genre-prompt", null, locale);
    }

    @Override
    public Genre convertString(String s) throws UserInputException {
        long id;
        try {
            id = Long.parseLong(s);
        }
        catch (NumberFormatException e) {
            id = -1;
        }

        if (id >= 0) { // считаем, что если ввели число, то это id
            Genre result = genreMap.get(id);
            if (result != null)
                return result;
            else
                throw new NoSuchGenreException();
        }
        else {
            List<Genre> resultList = genreMap.values().stream()
                    .filter(author -> s.equals(author.getText()))
                    .collect(Collectors.toList());

            if (resultList.size() > 0)
                return resultList.get(0);
            else
                throw new NoSuchGenreException();
        }
    }
}
