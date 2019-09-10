package ru.biderman.studenttest.service;

import org.springframework.stereotype.Service;
import ru.biderman.studenttest.domain.Answer;
import ru.biderman.studenttest.domain.Question;
import ru.biderman.studenttest.domain.VariantAnswer;
import ru.biderman.studenttest.domain.VariantQuestion;
import ru.biderman.studenttest.userinputoutput.UserInterface;
import ru.biderman.studenttest.userinputoutput.VariantAnswerInputUI;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AnswerServiceImpl implements AnswerService {
    private final UserInterface userInterface;

    AnswerServiceImpl(UserInterface userInterface) {
        this.userInterface = userInterface;
    }

    @Override
    public List<Answer> getAnswers(List<Question> questions) {
        List<Answer> result = new ArrayList<>();
        for(int i = 0; i < questions.size(); i++) {
            Question question = questions.get(i);
            assert question instanceof VariantQuestion;

            Optional<VariantAnswer> answer = userInterface.readValue(
                    new VariantAnswerInputUI((VariantQuestion) question, i + 1));

            if (!answer.map(result::add).orElse(false))
                break;
        }
        return result;
    }
}
