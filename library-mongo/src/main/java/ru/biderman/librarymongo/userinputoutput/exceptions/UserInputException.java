package ru.biderman.librarymongo.userinputoutput.exceptions;

public abstract class UserInputException extends Exception {
    public abstract String getLocalizedMessageId();
    public abstract Object[] getLocalizedMessageArgs();
}
