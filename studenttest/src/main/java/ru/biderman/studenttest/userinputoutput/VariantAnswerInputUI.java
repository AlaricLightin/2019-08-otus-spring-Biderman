package ru.biderman.studenttest.userinputoutput;

import org.springframework.context.MessageSource;
import ru.biderman.studenttest.domain.VariantAnswer;
import ru.biderman.studenttest.domain.VariantQuestion;
import ru.biderman.studenttest.userinputoutput.exceptions.InvalidVariantAnswerException;
import ru.biderman.studenttest.userinputoutput.exceptions.UserInputException;

import java.util.Locale;

public class VariantAnswerInputUI implements DataInputUI<VariantAnswer> {
    final static String VARIANT_FORMAT = "%d. %s";
    private final VariantQuestion question;
    private final int questionNum;

    public VariantAnswerInputUI(VariantQuestion question, int questionNum) {
        this.question = question;
        this.questionNum = questionNum;
    }

    @Override
    public String getPrompt(MessageSource messageSource, Locale locale) {
        StringBuilder prompt = new StringBuilder();
        prompt.append(messageSource.getMessage("prompt.question-number", new Integer[]{questionNum}, locale))
                .append(System.lineSeparator());

        prompt.append(question.getText())
                .append(System.lineSeparator());
        for(int j = 0; j < question.getVariants().size(); j++) {
            prompt.append(String.format(VARIANT_FORMAT, j + 1, question.getVariants().get(j)))
                    .append(System.lineSeparator());
        }
        return prompt.toString();
    }

    @Override
    public VariantAnswer convertString(String s) throws UserInputException {
        int resultNum;
        int answerCount = question.getVariants().size();

        try {
            resultNum = Integer.parseInt(s);
        }
        catch (NumberFormatException e) {
            throw new InvalidVariantAnswerException(answerCount);
        }

        if (resultNum >= 1 && resultNum <= answerCount)
            return new VariantAnswer(resultNum);
        else
            throw new InvalidVariantAnswerException(answerCount);
    }
}
