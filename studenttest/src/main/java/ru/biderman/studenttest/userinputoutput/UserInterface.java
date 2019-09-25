package ru.biderman.studenttest.userinputoutput;

import java.util.Optional;

/**
 * Интерфейс для пользовательского ввода-вывода
 */
public interface UserInterface {
    /**
     * Считывает с клавиатуры значение типа Т
     * @param dataInputUI объект для проверки корректности пользовательского ввода и преобразования в нужный тип
     * @param <T> тип результата
     * @return преобразованное значение или Optional.empty в случае, если пользователь ничего не ввёл
     */
    <T> Optional<T> readValue(DataInputUI<T> dataInputUI);

    /**
     * Выводит сообщение пользователю
     * @param messageCode код строки из файлов локализации
     * @param args перечень аргументов
     */
    void printText(String messageCode, Object[] args);

    default void printText(String messageCode) {
        printText(messageCode, null);
    }

    /**
     * Возвращает текстовое сообщение для пользователя (например, для работы со SpringShell)
     * @param messageCode код строки из файлов локализации
     * @param args перечень аргументов
     */
    String getText(String messageCode, Object[] args);

}
