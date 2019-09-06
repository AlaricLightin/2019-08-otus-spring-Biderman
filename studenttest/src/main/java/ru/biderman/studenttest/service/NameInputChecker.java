package ru.biderman.studenttest.service;

import ru.biderman.studenttest.dao.InputChecker;

import java.util.Optional;

class NameInputChecker extends InputChecker<String> {
    private final static String ERROR_STRING = "Введите имя и фамилию через пробел.";
    private final String checkError;
    private final String result;

    NameInputChecker(String s) {
        String[] nameArray = s.split(" ");
        if (nameArray.length < 2) {
            result = null;
            checkError = ERROR_STRING;
        }
        else {
            result = s;
            checkError = null;
        }
    }

    @Override
    public Optional<String> getCheckError() {
        return Optional.ofNullable(checkError);
    }

    @Override
    public Optional<String> getResult() {
        return Optional.ofNullable(result);
    }
}
