package ru.biderman.studenttest.service;

import ru.biderman.studenttest.domain.Question;

import java.util.List;

interface QuestionService {
    List<Question> getQuestions(int questionCount) throws NotEnoughQuestionException;
}
