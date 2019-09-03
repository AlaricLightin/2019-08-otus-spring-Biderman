package ru.biderman.studenttest.service;

import ru.biderman.studenttest.domain.Question;

import java.util.List;

public interface QuestionService {
    List<Question> getQuestions(int questionCount);
}
