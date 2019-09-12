package ru.biderman.studenttest.service;

import org.springframework.stereotype.Service;
import ru.biderman.studenttest.domain.Answer;
import ru.biderman.studenttest.domain.Question;

import java.util.List;

@Service
public class TestServiceImpl implements TestService{
    private final QuestionService questionService;
    private final AnswerService answerService;

    TestServiceImpl(QuestionService questionService, AnswerService answerService) {
        this.questionService = questionService;
        this.answerService = answerService;
    }

    @Override
    public int test(int questionCount) throws NotEnoughQuestionException, TestCanceledException{
//        Optional<String> name = nameService.getName();
//        if (!name.isPresent())
//            return CANCELED;

        List<Question> questions = questionService.getQuestions(questionCount);
        if (questions.size() < questionCount)
            throw new NotEnoughQuestionException();

        List<Answer> answers = answerService.getAnswers(questions);
        if (answers.size() < questionCount)
            throw new TestCanceledException();

        int result = 0;
        for(int i = 0; i < questions.size(); i++) {
            if (questions.get(i).checkAnswer(answers.get(i)))
                result++;
        }

        return result;
        //return String.format("Студент %s правильно ответил на %d вопросов из %d.", name.get(), result, questionCount);
    }
}
