package ru.biderman.studenttest.dao;

import ru.biderman.studenttest.domain.Question;

public interface QuestionDao {
    Question getQuestion(int num) throws QuestionDaoException;
}
