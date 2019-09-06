package ru.biderman.studenttest.dao;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import ru.biderman.studenttest.domain.Question;
import ru.biderman.studenttest.domain.VariantAnswer;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

class QuestionDaoImplTest {
    private static QuestionDao questionDao;

    @BeforeAll
    static void initTestQuestionDao() {
        Resource resource = new ClassPathResource("questions.csv");
        questionDao = new QuestionDaoImpl(resource);
    }

    @Test
    void getExistedQuestion() {
        Optional<Question> question = questionDao.getQuestion(0);
        assertThat(question).hasValueSatisfying(q -> {
            assertThat(q.getText()).isEqualTo("Корректный вопрос");
            assertThat(q.getVariants()).containsSequence("Вариант 1", "Вариант 2", "Вариант 3");
            assertTrue(q.checkAnswer(new VariantAnswer(1)));
        });
    }

    @Test
    void getNonExistedQuestion() {
        assertThat(questionDao.getQuestion(1)).isEmpty();
    }
}