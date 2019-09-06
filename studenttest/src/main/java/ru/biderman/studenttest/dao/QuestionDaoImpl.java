package ru.biderman.studenttest.dao;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.core.io.Resource;
import ru.biderman.studenttest.domain.Question;
import ru.biderman.studenttest.domain.VariantQuestion;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class QuestionDaoImpl implements QuestionDao {
    private final List<Question> questions;

    QuestionDaoImpl(Resource questionsResource) {
        questions = loadQuestions(questionsResource);
    }

    private List<Question> loadQuestions(Resource questionsResource) {
        List<CSVRecord> recordList = null;
        try (
                InputStream inputStream = questionsResource.getInputStream()
        )
        {
            CSVParser csvParser = CSVParser.parse(inputStream, StandardCharsets.UTF_8, CSVFormat.DEFAULT);
            recordList = csvParser.getRecords();
        }
        catch (IOException e) {
            System.out.println("Не удалось прочитать файл с вопросами.");
            e.printStackTrace();
        }

        if (recordList != null)
            return recordList.stream()
                    .map(this::getQuestion)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        else
            return Collections.emptyList();
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
    public Optional<Question> getQuestion(int num) {
        if (num >= 0 && num < questions.size())
            return Optional.of(questions.get(num));
        else
            return Optional.empty();
    }
}
