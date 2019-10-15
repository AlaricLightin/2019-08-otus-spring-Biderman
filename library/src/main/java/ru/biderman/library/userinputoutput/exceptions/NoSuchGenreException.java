package ru.biderman.library.userinputoutput.exceptions;

public class NoSuchGenreException extends UserInputException {
    @Override
    public String getLocalizedMessageId() {
        return "shell.error.no-such-genre";
    }

    @Override
    public Object[] getLocalizedMessageArgs() {
        return new Object[0];
    }
}
