package ru.biderman.studenttest.dao;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.biderman.studenttest.domain.Question;
import ru.biderman.studenttest.domain.VariantAnswer;
import ru.biderman.studenttest.domain.VariantQuestion;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class QuestionDaoImplTest {
    private static QuestionDao questionDao;
    private static final int EXISTING_QUESTION_NUMBER = 0;
    private static final int NON_EXISTING_QUESTION_NUMBER = 1;

    @BeforeAll
    static void initTestQuestionDao() {
        questionDao = new QuestionDaoImpl("classpath:questions", new Locale("ru-RU"));
    }

    @Test
    void getExistedQuestion() throws QuestionDaoException {
        final String QUESTION_TEXT = "Корректный вопрос";
        final String VARIANT1 = "Вариант 1";
        final String VARIANT2 = "Вариант 2";
        final String VARIANT3 = "Вариант 3";
        final int CORRECT_ANSWER = 1;

        Question question = questionDao.getQuestion(EXISTING_QUESTION_NUMBER);
        assertThat(question)
                .isInstanceOf(VariantQuestion.class)
                .hasFieldOrPropertyWithValue("text", QUESTION_TEXT);

        assertThat(((VariantQuestion) question).getVariants()).containsSequence(VARIANT1, VARIANT2, VARIANT3);
        assertTrue(question.checkAnswer(new VariantAnswer(CORRECT_ANSWER)));
    }

    @Test
    void getNonExistedQuestion() {
        assertThrows(QuestionNotFoundException.class, () -> questionDao.getQuestion(NON_EXISTING_QUESTION_NUMBER));
    }
}