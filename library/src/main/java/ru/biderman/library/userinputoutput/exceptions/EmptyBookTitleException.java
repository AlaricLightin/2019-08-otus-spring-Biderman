package ru.biderman.library.userinputoutput.exceptions;

public class EmptyBookTitleException extends UserInputException {
    @Override
    public String getLocalizedMessageId() {
        return "shell.error.empty-book-title";
    }

    @Override
    public Object[] getLocalizedMessageArgs() {
        return new Object[0];
    }
}
