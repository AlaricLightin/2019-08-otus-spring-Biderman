package ru.biderman.studenttest.domain;

public interface Question {
    String getText();
    boolean checkAnswer(Answer answer);
}
