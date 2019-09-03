package ru.biderman.studenttest.service;

import org.junit.Test;
import ru.biderman.studenttest.domain.Answer;
import ru.biderman.studenttest.domain.Question;
import ru.biderman.studenttest.domain.VariantAnswer;
import ru.biderman.studenttest.domain.VariantQuestion;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestServiceImplTest {

    private TestService createTestService(String userName, List<Question> questions, List<Answer> answers) {
        NameService nameService = mock(NameService.class);
        when(nameService.getName()).thenReturn(Optional.ofNullable(userName));

        QuestionService questionService = mock(QuestionService.class);
        when(questionService.getQuestions(anyInt())).thenReturn(questions);

        AnswerService answerService = mock(AnswerService.class);
        when(answerService.getAnswers(any())).thenReturn(answers);

        return new TestServiceImpl(nameService, questionService, answerService);
    }

    @Test
    public void test1() {
        Question question1 = new VariantQuestion("Тестовый вопрос 1", Arrays.asList("1", "2"), 2);
        Question question2 = new VariantQuestion("Тестовый вопрос 2", Arrays.asList("1", "2"), 2);

        TestService testService = createTestService("Пользователь",
                Arrays.asList(question1, question2),
                Arrays.asList(new VariantAnswer(1), new VariantAnswer(2)));

        assertEquals("Студент Пользователь правильно ответил на 1 вопросов из 2.", testService.test(2));
    }

    @Test
    public void testCanceled() {
        Question question1 = new VariantQuestion("Тестовый вопрос 1", Arrays.asList("1", "2"), 2);
        Question question2 = new VariantQuestion("Тестовый вопрос 2", Arrays.asList("1", "2"), 2);

        TestService testService = createTestService("Пользователь",
                Arrays.asList(question1, question2),
                Collections.singletonList(new VariantAnswer(1)));

        assertEquals("Тестирование прервано пользователем.", testService.test(2));
    }

    @Test
    public void testNameCanceled() {
        TestService testService = createTestService(null,
                Collections.emptyList(),
                Collections.emptyList());

        assertEquals("Тестирование прервано пользователем.", testService.test(2));
    }
}