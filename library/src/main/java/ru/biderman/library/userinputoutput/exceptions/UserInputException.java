package ru.biderman.library.userinputoutput.exceptions;

public abstract class UserInputException extends Exception {
    public abstract String getLocalizedMessageId();
    public abstract Object[] getLocalizedMessageArgs();
}
