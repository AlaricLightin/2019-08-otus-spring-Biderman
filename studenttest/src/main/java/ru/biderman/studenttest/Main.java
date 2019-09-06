package ru.biderman.studenttest;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import ru.biderman.studenttest.service.TestService;

@Configuration
@ComponentScan
public class Main {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(Main.class);
        TestService testService = context.getBean(TestService.class);
        System.out.println("Тестирование");
        System.out.println("После ввода любого ответа нужно нажать клавишу Enter.");
        System.out.println("Для ответа на вопросы введите номер правильного ответа.");
        System.out.println("Для выхода в любой момент введите \"q\".");
        System.out.println();
        String result = testService.test(5);
        System.out.println(result);
    }
}
