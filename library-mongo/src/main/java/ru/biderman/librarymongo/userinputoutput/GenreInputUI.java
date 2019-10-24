package ru.biderman.librarymongo.userinputoutput;

import org.springframework.context.MessageSource;

import java.util.Locale;

class GenreInputUI implements DataInputUI<String> {
    @Override
    public String getPrompt(MessageSource messageSource, Locale locale) {
        return messageSource.getMessage("shell.genre-prompt", null, locale);
    }

    @Override
    public String convertString(String s) {
        return s;
    }
}
