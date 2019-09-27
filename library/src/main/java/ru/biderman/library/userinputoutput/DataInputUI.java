package ru.biderman.library.userinputoutput;

import org.springframework.context.MessageSource;
import ru.biderman.library.userinputoutput.exceptions.UserInputException;

import java.util.Locale;

/**
 * Интерфейс для доступа к функциям, необходимым, чтобы считать какие-то данные
 * @param <T> тип получаемых данных
 */
interface DataInputUI<T> {
    /**
     * @return подсказка, выводимая пользователю
     */
    String getPrompt(MessageSource messageSource, Locale locale);

    /**
     * Преобразовывает строку в нужный тип
     * @throws UserInputException если происходит неверный ввод
     */
    T convertString(String s) throws UserInputException;
}
