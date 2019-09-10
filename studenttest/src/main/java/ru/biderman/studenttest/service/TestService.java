package ru.biderman.studenttest.service;

interface TestService {
    /**
     * Основная процедура тестирования
     * @param questionCount количество вопросов
     * @return количество правильных ответов
     */
    int test(int questionCount) throws NotEnoughQuestionException, TestCanceledException;
}
