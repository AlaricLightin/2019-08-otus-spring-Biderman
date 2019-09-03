package ru.biderman.studenttest.service;

import ru.biderman.studenttest.domain.Answer;
import ru.biderman.studenttest.domain.Question;

import java.util.List;
import java.util.Optional;

public class TestServiceImpl implements TestService{
    private static final String CANCELED = "Тестирование прервано пользователем.";
    private static final String INTERNAL_ERROR = "Ошибка системы тестирования. Пожалуйста, обратитесь к разработчику.";

    private final NameService nameService;
    private final QuestionService questionService;
    private final AnswerService answerService;

    TestServiceImpl(NameService nameService, QuestionService questionService, AnswerService answerService) {
        this.nameService = nameService;
        this.questionService = questionService;
        this.answerService = answerService;
    }

    @Override
    public String test(int questionCount) {
        Optional<String> name = nameService.getName();
        if (!name.isPresent())
            return CANCELED;

        List<Question> questions = questionService.getQuestions(questionCount);
        if (questions.size() < questionCount)
            return INTERNAL_ERROR;

        List<Answer> answers = answerService.getAnswers(questions);
        if (answers.size() < questionCount)
            return CANCELED;

        int result = 0;
        for(int i = 0; i < questions.size(); i++) {
            if (questions.get(i).checkAnswer(answers.get(i)))
                result++;
        }

        return String.format("Студент %s правильно ответил на %d вопросов из %d.", name.get(), result, questionCount);
    }
}
