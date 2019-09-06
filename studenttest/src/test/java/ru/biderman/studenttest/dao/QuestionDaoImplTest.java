package ru.biderman.studenttest.dao;

import org.junit.Test;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import ru.biderman.studenttest.domain.Question;
import ru.biderman.studenttest.domain.VariantAnswer;

import java.util.Optional;

import static org.junit.Assert.*;

public class QuestionDaoImplTest {

    @Test
    public void getQuestion() {
        Resource resource = new FileSystemResource("src/test/resource/questions.csv");
        QuestionDao questionDao = new QuestionDaoImpl(resource);
        Optional<Question> questionOptional = questionDao.getQuestion(0);
        assertTrue(questionOptional.isPresent());

        Question question = questionOptional.get();
        assertEquals("Корректный вопрос", question.getText());
        assertEquals(3, question.getVariants().size());
        assertEquals("Вариант 2", question.getVariants().get(1));
        assertTrue(question.checkAnswer(new VariantAnswer(1)));

        assertFalse(questionDao.getQuestion(1).isPresent());
    }
}