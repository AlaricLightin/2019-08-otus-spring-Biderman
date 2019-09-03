package ru.biderman.studenttest.dao;

import java.util.Optional;
import java.util.function.Function;

public interface UserInterface {
    <T> Optional<T> readValue(String prompt,
                              Function<String, InputChecker<T>> inputCheckerFunction);
}
