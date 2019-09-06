package ru.biderman.studenttest.domain;

import java.util.List;

public class VariantQuestion implements Question{
    private final String text;
    private final List<String> variants;
    private final int answerNum;

    public VariantQuestion(String text, List<String> variants, int answerNum) {
        this.text = text;
        this.variants = variants;
        this.answerNum = answerNum;
    }

    public String getText() {
        return text;
    }

    public List<String> getVariants() {
        return variants;
    }

    public boolean checkAnswer(Answer answer) {
        assert answer instanceof VariantAnswer;
        return answerNum == ((VariantAnswer) answer).getAnswerNum();
    }
}
