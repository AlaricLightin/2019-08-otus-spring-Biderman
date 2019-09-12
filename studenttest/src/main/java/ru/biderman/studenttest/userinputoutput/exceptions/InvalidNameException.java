package ru.biderman.studenttest.userinputoutput.exceptions;

public class InvalidNameException extends UserInputException {
    @Override
    public String getLocalizedMessageId() {
        return "prompt.name";
    }

    @Override
    public Object[] getLocalizedMessageArgs() {
        return new Object[0];
    }
}
