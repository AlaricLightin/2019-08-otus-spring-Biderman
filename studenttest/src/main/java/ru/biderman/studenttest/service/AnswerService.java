package ru.biderman.studenttest.service;

import ru.biderman.studenttest.domain.Answer;
import ru.biderman.studenttest.domain.Question;

import java.util.List;

public interface AnswerService {
    List<Answer> getAnswers(List<Question> questions);
}
