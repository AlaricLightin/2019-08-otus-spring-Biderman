package ru.biderman.studenttest.dao;

import java.util.Optional;

/**
 * Класс для валидации и преобразования значений, введённых с клавиатуры
 * @param <T> результирующий класс, который нужно получить после преобразования
 */
public abstract class InputChecker<T> {
    /**
     * Если проверка не пройдена, возвращается строка с сообщением об ошибке. Если проверка пройдена - отсутствующее значение.
     */
    public abstract Optional<String> getCheckError();

    /**
     * Возвращает уже преобразованное значение. Если пользователь отказался от ввода - отсутствующее значение.
     */
    public abstract Optional<T> getResult();
}
