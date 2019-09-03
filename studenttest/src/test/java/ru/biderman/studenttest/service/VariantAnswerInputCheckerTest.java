package ru.biderman.studenttest.service;

import org.junit.Test;

import static org.junit.Assert.*;

public class VariantAnswerInputCheckerTest {

    @Test
    public void createVariantAnswerValue() {
        VariantAnswerInputChecker value1 = new VariantAnswerInputChecker("1", 2);
        assertTrue(value1.getResult().isPresent());
        assertFalse(value1.getCheckError().isPresent());
        assertEquals(1, value1.getResult().get().getAnswerNum());

        VariantAnswerInputChecker value2 = new VariantAnswerInputChecker("string", 2);
        assertFalse(value2.getResult().isPresent());
        assertTrue(value2.getCheckError().isPresent());
        assertEquals("Ответ должен быть числом от 1 до 2.", value2.getCheckError().get());

        VariantAnswerInputChecker value3 = new VariantAnswerInputChecker("3", 2);
        assertFalse(value3.getResult().isPresent());
        assertTrue(value3.getCheckError().isPresent());
        assertEquals("Ответ должен быть числом от 1 до 2.", value3.getCheckError().get());
    }

}