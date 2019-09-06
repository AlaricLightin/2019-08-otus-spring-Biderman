package ru.biderman.studenttest.service;

import org.springframework.stereotype.Service;
import ru.biderman.studenttest.dao.QuestionDao;
import ru.biderman.studenttest.domain.Question;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class QuestionServiceImpl implements QuestionService {
    private final QuestionDao questionDao;

    QuestionServiceImpl(QuestionDao questionDao) {
        this.questionDao = questionDao;
    }

    @Override
    public List<Question> getQuestions(int questionCount) {
        return IntStream.rangeClosed(0, questionCount - 1)
                .mapToObj(questionDao::getQuestion)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }
}
