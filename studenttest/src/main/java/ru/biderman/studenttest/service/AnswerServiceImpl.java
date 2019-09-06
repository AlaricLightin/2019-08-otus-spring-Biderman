package ru.biderman.studenttest.service;

import org.springframework.stereotype.Service;
import ru.biderman.studenttest.dao.UserInterface;
import ru.biderman.studenttest.domain.Answer;
import ru.biderman.studenttest.domain.Question;
import ru.biderman.studenttest.domain.VariantAnswer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AnswerServiceImpl implements AnswerService {
    private final UserInterface userInterface;

    public AnswerServiceImpl(UserInterface userInterface) {
        this.userInterface = userInterface;
    }

    @Override
    public List<Answer> getAnswers(List<Question> questions) {
        List<Answer> result = new ArrayList<>();
        for(int i = 0; i < questions.size(); i++) {
            StringBuilder prompt = new StringBuilder();
            prompt.append(String.format("Вопрос N%d", i + 1))
                    .append(System.lineSeparator());
            Question question = questions.get(i);
            prompt.append(question.getText())
                    .append(System.lineSeparator());
            for(int j = 0; j < question.getVariants().size(); j++) {
                prompt.append(String.format("%d. %s", j + 1, question.getVariants().get(j)))
                    .append(System.lineSeparator());
            }

            Optional<VariantAnswer> answer = userInterface.readValue(
                    prompt.toString(),
                    s -> new VariantAnswerInputChecker(s, question.getVariants().size()));

            if (answer.isPresent())
                result.add(answer.get());
            else
                break;
        }
        return result;
    }
}
