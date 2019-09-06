package ru.biderman.studenttest.service;

import ru.biderman.studenttest.dao.InputChecker;
import ru.biderman.studenttest.domain.VariantAnswer;

import java.util.Optional;

class VariantAnswerInputChecker extends InputChecker<VariantAnswer> {
    private final static String ERROR_STRING = "Ответ должен быть числом от 1 до %d.";
    private final String checkError;
    private final VariantAnswer result;

    VariantAnswerInputChecker(String s, int answerCount) {
        int resultNum;

        try {
            resultNum = Integer.parseInt(s);
        }
        catch (NumberFormatException e) {
            resultNum = -1;
        }

        if (resultNum >= 1 && resultNum <= answerCount) {
            checkError = null;
            result = new VariantAnswer(resultNum);
        }
        else {
            checkError = String.format(ERROR_STRING, answerCount);
            result = null;
        }
    }

    @Override
    public Optional<String> getCheckError() {
        return Optional.ofNullable(checkError);
    }

    @Override
    public Optional<VariantAnswer> getResult() {
        return Optional.ofNullable(result);
    }
}
