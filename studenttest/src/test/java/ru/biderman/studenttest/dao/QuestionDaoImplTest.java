package ru.biderman.studenttest.dao;

import org.junit.jupiter.api.Test;
import ru.biderman.studenttest.domain.Question;
import ru.biderman.studenttest.domain.UserInterfaceProperties;
import ru.biderman.studenttest.domain.VariantAnswer;
import ru.biderman.studenttest.domain.VariantQuestion;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class QuestionDaoImplTest {
    private static final int EXISTING_QUESTION_NUMBER = 0;
    private static final int NON_EXISTING_QUESTION_NUMBER = 1;

    private QuestionDao createQuestionDao(String localeString) {
        QuestionsSourceProperties questionsSourceProperties = mock(QuestionsSourceProperties.class);
        when(questionsSourceProperties.getFilePrefix()).thenReturn("classpath:questions");

        UserInterfaceProperties userInterfaceProperties = mock(UserInterfaceProperties.class);
        when(userInterfaceProperties.getLocale()).thenReturn(new Locale(localeString));

        return new QuestionDaoImpl(questionsSourceProperties, userInterfaceProperties);
    }

    @Test
    void getExistedQuestion() throws QuestionDaoException {
        final String QUESTION_TEXT = "Корректный вопрос";
        final String VARIANT1 = "Вариант 1";
        final String VARIANT2 = "Вариант 2";
        final String VARIANT3 = "Вариант 3";
        final int CORRECT_ANSWER = 1;

        QuestionDao questionDao = createQuestionDao("ru_RU");
        Question question = questionDao.getQuestion(EXISTING_QUESTION_NUMBER);
        assertThat(question)
                .isInstanceOf(VariantQuestion.class)
                .hasFieldOrPropertyWithValue("text", QUESTION_TEXT);

        assertThat(((VariantQuestion) question).getVariants()).containsSequence(VARIANT1, VARIANT2, VARIANT3);
        assertTrue(question.checkAnswer(new VariantAnswer(CORRECT_ANSWER)));
    }

    @Test
    void getNonExistedQuestion() {
        QuestionDao questionDao = createQuestionDao("ru_RU");
        assertThrows(QuestionNotFoundException.class, () -> questionDao.getQuestion(NON_EXISTING_QUESTION_NUMBER));
    }

    @Test
    void getEnglishQuestions() throws QuestionDaoException {
        final String QUESTION_TEXT = "Question in English";
        final String VARIANT1 = "Variant 1";
        final String VARIANT2 = "Variant 2";
        final int CORRECT_ANSWER = 1;

        QuestionDao questionDao = createQuestionDao("en_US");
        Question question = questionDao.getQuestion(EXISTING_QUESTION_NUMBER);
        assertThat(question)
                .isInstanceOf(VariantQuestion.class)
                .hasFieldOrPropertyWithValue("text", QUESTION_TEXT);

        assertThat(((VariantQuestion) question).getVariants()).containsSequence(VARIANT1, VARIANT2);
        assertTrue(question.checkAnswer(new VariantAnswer(CORRECT_ANSWER)));
    }
}