package ru.biderman.studenttest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import ru.biderman.studenttest.service.TestRunner;

@SpringBootApplication
public class StudentTestApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(StudentTestApplication.class, args);
		TestRunner testRunner = context.getBean(TestRunner.class);
		testRunner.run();
	}

}
