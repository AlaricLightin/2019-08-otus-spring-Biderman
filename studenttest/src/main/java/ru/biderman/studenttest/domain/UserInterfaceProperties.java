package ru.biderman.studenttest.domain;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@ConfigurationProperties("user-interface")
public class UserInterfaceProperties {
    private Locale locale;
    private int questionsCount;

    public Locale getLocale() {
        return locale;
    }

    public int getQuestionsCount() {
        return questionsCount;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public void setQuestionsCount(int questionsCount) {
        this.questionsCount = questionsCount;
    }
}
