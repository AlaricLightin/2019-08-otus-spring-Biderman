package ru.biderman.studenttest.service;

import org.springframework.stereotype.Service;
import ru.biderman.studenttest.dao.QuestionDao;
import ru.biderman.studenttest.dao.QuestionDaoException;
import ru.biderman.studenttest.domain.Question;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class QuestionServiceImpl implements QuestionService {
    private final QuestionDao questionDao;

    QuestionServiceImpl(QuestionDao questionDao) {
        this.questionDao = questionDao;
    }

    @Override
    public List<Question> getQuestions(int questionCount) throws NotEnoughQuestionException{
        ArrayList<Question> result = new ArrayList<>();
        try {
            for (int i = 0; i < questionCount; i++) {
                result.add(questionDao.getQuestion(i));
            }

            return Collections.unmodifiableList(result);
        }
        catch (QuestionDaoException e) {
            throw new NotEnoughQuestionException();
        }
    }
}
