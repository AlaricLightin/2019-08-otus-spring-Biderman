package ru.biderman.studenttest.userinputoutput.exceptions;

public class InvalidVariantAnswerException extends UserInputException {
    private final int maxAnswer;

    public InvalidVariantAnswerException(int maxAnswer) {
        this.maxAnswer = maxAnswer;
    }

    @Override
    public String getLocalizedMessageId() {
        return "error.variant-answer";
    }

    @Override
    public Object[] getLocalizedMessageArgs() {
        return new Integer[]{maxAnswer};
    }
}
