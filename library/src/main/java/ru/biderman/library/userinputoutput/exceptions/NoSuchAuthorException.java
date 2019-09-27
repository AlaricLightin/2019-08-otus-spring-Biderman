package ru.biderman.library.userinputoutput.exceptions;

public class NoSuchAuthorException extends UserInputException {
    @Override
    public String getLocalizedMessageId() {
        return "shell.error.no-such-author";
    }

    @Override
    public Object[] getLocalizedMessageArgs() {
        return new Object[0];
    }
}
