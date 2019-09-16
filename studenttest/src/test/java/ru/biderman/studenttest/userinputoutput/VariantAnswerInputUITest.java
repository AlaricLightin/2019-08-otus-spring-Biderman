package ru.biderman.studenttest.userinputoutput;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.context.MessageSource;
import ru.biderman.studenttest.domain.VariantQuestion;
import ru.biderman.studenttest.userinputoutput.exceptions.InvalidVariantAnswerException;
import ru.biderman.studenttest.userinputoutput.exceptions.UserInputException;

import java.util.Arrays;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class VariantAnswerInputUITest {
    private static final String QUESTION_TEXT = "Question Text";
    private static final String VARIANT1 = "Variant 1";
    private static final String VARIANT2 = "Variant 2";
    private static final int ANSWER_NUM = 1;
    private static final int QUESTION_NUM = 1;

    private static final VariantQuestion question =
            new VariantQuestion(QUESTION_TEXT, Arrays.asList(VARIANT1, VARIANT2), ANSWER_NUM);
    private static final VariantAnswerInputUI inputUI = new VariantAnswerInputUI(question, QUESTION_NUM);

    @Test
    void getPrompt() {
        final String questionPrompt = "Q1";
        MessageSource messageSource = mock(MessageSource.class);
        when(messageSource.getMessage(
                eq("prompt.question-number"), eq(new Integer[]{QUESTION_NUM}), any())).thenReturn(questionPrompt);

        assertEquals(questionPrompt + System.lineSeparator()
                + QUESTION_TEXT + System.lineSeparator()
                + String.format(VariantAnswerInputUI.VARIANT_FORMAT, 1, VARIANT1) + System.lineSeparator()
                + String.format(VariantAnswerInputUI.VARIANT_FORMAT, 2, VARIANT2) + System.lineSeparator(),

                inputUI.getPrompt(messageSource, new Locale("ru-RU")));
    }

    @ParameterizedTest
    @ValueSource(strings = {"1", "2"})
    void convertStringWithSuccess(String s) throws UserInputException {
        assertEquals(Integer.parseInt(s), inputUI.convertString(s).getAnswerNum());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "-1", "0", "3"})
    void convertStringWithException(String s) {
        assertThrows(InvalidVariantAnswerException.class, () -> inputUI.convertString(s));
    }
}