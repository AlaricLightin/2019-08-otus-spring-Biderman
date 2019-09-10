package ru.biderman.studenttest.service;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import ru.biderman.studenttest.domain.Answer;
import ru.biderman.studenttest.domain.VariantAnswer;
import ru.biderman.studenttest.domain.VariantQuestion;
import ru.biderman.studenttest.userinputoutput.UserInterface;
import ru.biderman.studenttest.userinputoutput.VariantAnswerInputUI;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class AnswerServiceImplTest {

    @Test
    void getAnswers() {
        final String questionString1 = "Question 1";
        final String questionString2 = "Question 2";
        VariantQuestion question1 = new VariantQuestion(questionString1, null, 1);
        VariantQuestion question2 = new VariantQuestion(questionString2, null, 2);
        Answer answer1 = new VariantAnswer(1);
        Answer answer2 = new VariantAnswer(2);

        UserInterface userInterface = mock(UserInterface.class);

        when(userInterface.readValue(any()))
                .thenReturn(Optional.of(answer1))
                .thenReturn(Optional.of(answer2));

        AnswerService answerService = new AnswerServiceImpl(userInterface);
        List<Answer> answers = answerService.getAnswers(Arrays.asList(question1, question2));
        assertThat(answers).contains(answer1, answer2);
        ArgumentCaptor<VariantAnswerInputUI> argument = ArgumentCaptor.forClass(VariantAnswerInputUI.class);
        verify(userInterface, times(2)).readValue(argument.capture());
        assertThat(argument.getAllValues().get(0)).isEqualToComparingFieldByField(
                new VariantAnswerInputUI(question1, 1));
        assertThat(argument.getAllValues().get(1)).isEqualToComparingFieldByField(
                new VariantAnswerInputUI(question2, 2));
    }

    @Test
    void getOnlyOneAnswer() {
        final String questionString1 = "Question 1";
        final String questionString2 = "Question 2";
        VariantQuestion question1 = new VariantQuestion(questionString1, null, 1);
        VariantQuestion question2 = new VariantQuestion(questionString2, null, 2);
        Answer answer1 = new VariantAnswer(1);

        UserInterface userInterface = mock(UserInterface.class);

        when(userInterface.readValue(any()))
                .thenReturn(Optional.of(answer1))
                .thenReturn(Optional.empty());

        AnswerService answerService = new AnswerServiceImpl(userInterface);
        List<Answer> answers = answerService.getAnswers(Arrays.asList(question1, question2));
        assertThat(answers).contains(answer1);
        ArgumentCaptor<VariantAnswerInputUI> argument = ArgumentCaptor.forClass(VariantAnswerInputUI.class);
        verify(userInterface, times(2)).readValue(argument.capture());
        assertThat(argument.getAllValues().get(0)).isEqualToComparingFieldByField(
                new VariantAnswerInputUI(question1, 1));
        assertThat(argument.getAllValues().get(1)).isEqualToComparingFieldByField(
                new VariantAnswerInputUI(question2, 2));
    }
}