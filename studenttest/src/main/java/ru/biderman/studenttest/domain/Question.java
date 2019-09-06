package ru.biderman.studenttest.domain;

import java.util.List;

public interface Question {
    String getText();
    List<String> getVariants();
    boolean checkAnswer(Answer answer);
}
