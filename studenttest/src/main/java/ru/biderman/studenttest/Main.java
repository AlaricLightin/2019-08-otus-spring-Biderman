package ru.biderman.studenttest;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.biderman.studenttest.service.TestRunner;

public class Main {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(SpringConfig.class);
        TestRunner testRunner = context.getBean(TestRunner.class);
        testRunner.run();
    }
}
