package ru.biderman.studenttest.service;

import org.junit.Test;
import ru.biderman.studenttest.dao.QuestionDao;
import ru.biderman.studenttest.domain.Question;
import ru.biderman.studenttest.domain.VariantQuestion;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class QuestionServiceImplTest {

    @Test
    public void getQuestions() {
        QuestionDao questionDao = mock(QuestionDao.class);
        Question question1 = new VariantQuestion("Текст вопроса 1", Arrays.asList("1", "2"), 1);
        Question question2 = new VariantQuestion("Текст вопроса 2", Arrays.asList("1", "2"), 2);
        when(questionDao.getQuestion(0)).thenReturn(Optional.of(question1));
        when(questionDao.getQuestion(1)).thenReturn(Optional.of(question2));
        when(questionDao.getQuestion(2)).thenReturn(Optional.empty());

        QuestionService questionService = new QuestionServiceImpl(questionDao);

        List<Question> questions = questionService.getQuestions(2);
        assertEquals(2, questions.size());
        assertEquals(question1, questions.get(0));
        assertEquals(question2, questions.get(1));

        questions = questionService.getQuestions(3);
        assertEquals(2, questions.size());

        questions = questionService.getQuestions(-2);
        assertEquals(0, questions.size());
    }
}