package ru.biderman.studenttest.service;

import org.junit.jupiter.api.Test;
import ru.biderman.studenttest.domain.Answer;
import ru.biderman.studenttest.domain.Question;
import ru.biderman.studenttest.domain.VariantAnswer;
import ru.biderman.studenttest.domain.VariantQuestion;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TestServiceImplTest {

    private TestService createTestService(List<Question> questions, List<Answer> answers) throws NotEnoughQuestionException {
        QuestionService questionService = mock(QuestionService.class);
        when(questionService.getQuestions(anyInt())).thenReturn(questions);

        AnswerService answerService = mock(AnswerService.class);
        when(answerService.getAnswers(any())).thenReturn(answers);

        return new TestServiceImpl(questionService, answerService);
    }

    @Test
    void test1() throws NotEnoughQuestionException, TestCanceledException{
        Question question1 = new VariantQuestion("Тестовый вопрос 1", Arrays.asList("1", "2"), 2);
        Question question2 = new VariantQuestion("Тестовый вопрос 2", Arrays.asList("1", "2"), 2);

        TestService testService = createTestService(
                Arrays.asList(question1, question2),
                Arrays.asList(new VariantAnswer(1), new VariantAnswer(2)));

        assertEquals(1, testService.test(2));
    }

    @Test
    void testCanceled() throws NotEnoughQuestionException {
        Question question1 = new VariantQuestion("Тестовый вопрос 1", Arrays.asList("1", "2"), 2);
        Question question2 = new VariantQuestion("Тестовый вопрос 2", Arrays.asList("1", "2"), 2);

        TestService testService = createTestService(
                Arrays.asList(question1, question2),
                Collections.singletonList(new VariantAnswer(1)));

        assertThrows(TestCanceledException.class, () -> testService.test(2));
    }
}