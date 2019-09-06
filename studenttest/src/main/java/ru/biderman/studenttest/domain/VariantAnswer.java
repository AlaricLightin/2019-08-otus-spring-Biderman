package ru.biderman.studenttest.domain;

public class VariantAnswer implements Answer{
    private final int answerNum;

    public VariantAnswer(int answerNum) {
        this.answerNum = answerNum;
    }

    public int getAnswerNum() {
        return answerNum;
    }
}
