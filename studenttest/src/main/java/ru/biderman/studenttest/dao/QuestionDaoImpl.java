package ru.biderman.studenttest.dao;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.LocalizedResourceHelper;
import org.springframework.stereotype.Repository;
import ru.biderman.studenttest.domain.Question;
import ru.biderman.studenttest.domain.UserInterfaceProperties;
import ru.biderman.studenttest.domain.VariantQuestion;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Repository
public class QuestionDaoImpl implements QuestionDao {
    private final static String EXTENSION = ".csv";
    private final QuestionsSourceProperties questionsSourceProperties;
    private final UserInterfaceProperties userInterfaceProperties;
    private List<Question> questions = null;

    QuestionDaoImpl(QuestionsSourceProperties questionsSourceProperties, UserInterfaceProperties userInterfaceProperties) {
        this.questionsSourceProperties = questionsSourceProperties;
        this.userInterfaceProperties = userInterfaceProperties;
    }

    private Resource getResource() {
        LocalizedResourceHelper helper = new LocalizedResourceHelper();
        return helper.findLocalizedResource(questionsSourceProperties.getFilePrefix(),
                EXTENSION,
                userInterfaceProperties.getLocale());
    }

    private List<Question> loadQuestions(Resource questionsResource) throws QuestionReadingException {
        try (
                InputStream inputStream = questionsResource.getInputStream()
        )
        {
            CSVParser csvParser = CSVParser.parse(inputStream, StandardCharsets.UTF_8, CSVFormat.DEFAULT);
            return csvParser.getRecords().stream()
                    .map(this::getQuestion)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        }
        catch (IOException e) {
            throw new QuestionReadingException(e);
        }
    }

    private Question getQuestion(CSVRecord csvRecord) {
        if (csvRecord.size() < 3)
            return null;

        String text = csvRecord.get(0);
        int answerNum;
        try {
            answerNum = Integer.parseInt(csvRecord.get(1));
        }
        catch (NumberFormatException e) {
            return null;
        }

        List<String> answerVariants = new ArrayList<>();
        for(int i = 2; i < csvRecord.size(); i++)
            answerVariants.add(csvRecord.get(i));

        return new VariantQuestion(text, Collections.unmodifiableList(answerVariants), answerNum);
    }

    @Override
    public Question getQuestion(int num) throws QuestionDaoException{
        if (questions == null) {
            synchronized (this) {
                if(questions == null)
                    questions = loadQuestions(getResource());
            }
        }

        if (num >= 0 && num < questions.size())
            return questions.get(num);
        else
            throw new QuestionNotFoundException();
    }
}
