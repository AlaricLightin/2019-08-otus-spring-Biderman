package ru.biderman.studenttest.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class VariantQuestionTest {

    @Test
    void checkAnswer() {
        Question question = new VariantQuestion("Тестовый вопрос", null, 1);
        assertTrue(question.checkAnswer(new VariantAnswer(1)));
        assertFalse(question.checkAnswer(new VariantAnswer(2)));
    }
}