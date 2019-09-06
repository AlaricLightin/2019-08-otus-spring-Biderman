package ru.biderman.studenttest.dao;

import ru.biderman.studenttest.domain.Question;

import java.util.Optional;

public interface QuestionDao {
    Optional<Question> getQuestion(int num);
}
