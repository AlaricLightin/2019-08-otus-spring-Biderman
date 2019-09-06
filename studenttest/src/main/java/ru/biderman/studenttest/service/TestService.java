package ru.biderman.studenttest.service;

public interface TestService {
    /**
     * Основная процедура тестирования
     * @param questionCount количество вопросов
     * @return результат тестирования в виде строки
     */
    String test(int questionCount);
}
