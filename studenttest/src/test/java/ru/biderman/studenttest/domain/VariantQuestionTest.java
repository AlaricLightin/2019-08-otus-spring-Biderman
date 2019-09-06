package ru.biderman.studenttest.domain;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class VariantQuestionTest {

    @Test
    public void checkAnswer() {
        Question question = new VariantQuestion("Тестовый вопрос", null, 1);
        assertTrue(question.checkAnswer(new VariantAnswer(1)));
        assertFalse(question.checkAnswer(new VariantAnswer(2)));
    }
}