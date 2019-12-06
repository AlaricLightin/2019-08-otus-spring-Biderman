package ru.biderman.librarymigration.batch;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.builder.MongoItemWriterBuilder;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import ru.biderman.librarymigration.domain.Author;
import ru.biderman.librarymigration.domain.Book;
import ru.biderman.librarymigration.domain.Genre;
import ru.biderman.librarymigration.domainmongo.AuthorMongo;
import ru.biderman.librarymigration.domainmongo.BookMongo;
import ru.biderman.librarymigration.sqlrepositories.AuthorRepository;
import ru.biderman.librarymigration.sqlrepositories.BookRepository;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@EnableBatchProcessing
@Configuration
@RequiredArgsConstructor
public class BatchConfig {
    private final Logger logger = LoggerFactory.getLogger("Batch");

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job migrateDatabaseJob(Step migrateAuthorStep,
                                  Step migrateBookStep,
                                  Step removeOldIdStep) {
        return jobBuilderFactory
                .get("migrateDatabaseJob")
                .start(migrateAuthorStep)
                .next(migrateBookStep)
                .next(removeOldIdStep)
                .listener(new JobExecutionListener() {
                              @Override
                              public void beforeJob(JobExecution jobExecution) {
                                  logger.info("Начало миграции БД");
                              }

                              @Override
                              public void afterJob(JobExecution jobExecution) {
                                  logger.info("Конец миграции БД");
                              }
                          }
                )
                .build();
    }

    @Bean
    public Step migrateAuthorStep(ItemReader<Author> authorItemReader,
                                  ItemProcessor<Author, AuthorMongo> authorItemProcessor,
                                  ItemWriter<AuthorMongo> authorItemWriter) {
        return stepBuilderFactory
                .get("migrateAuthorStep")
                .<Author, AuthorMongo>chunk(5)
                .reader(authorItemReader)
                .processor(authorItemProcessor)
                .writer(authorItemWriter)
                .build();
    }

    @Bean
    public ItemReader<Author> authorItemReader(AuthorRepository authorRepository) {
        return new RepositoryItemReaderBuilder<Author>()
                .repository(authorRepository)
                .methodName("findAll")
                .sorts(Collections.emptyMap())
                .saveState(false)
                .build();
    }

    @Bean
    public ItemReader<Book> bookItemReader(BookRepository bookRepository) {
        return new RepositoryItemReaderBuilder<Book>()
                .repository(bookRepository)
                .methodName("findAll")
                .sorts(Collections.emptyMap())
                .saveState(false)
                .build();
    }

    @Bean
    public ItemProcessor<Author, AuthorMongo> authorItemProcessor() {
        return author -> new AuthorMongo(null, author.getSurname(), author.getOtherNames(), author.getId());
    }

    private List<AuthorMongo> getUpdatedAuthors(List<Long> oldIdList, MongoTemplate mongoTemplate) {
        Query query = new Query();
        query.addCriteria(Criteria.where("oldId").in(oldIdList));
        return mongoTemplate.find(query, AuthorMongo.class);
    }

    @Bean
    public ItemProcessor<Book, BookMongo> bookItemProcessor(MongoTemplate mongoTemplate) {
        return book -> new BookMongo(null,
                getUpdatedAuthors(book.getAuthorList().stream().map(Author::getId).collect(Collectors.toList()),
                        mongoTemplate),
                book.getTitle(),
                book.getGenres().stream()
                        .map(Genre::getText)
                        .collect(Collectors.toSet())
        );
    }

    @Bean
    public ItemWriter<AuthorMongo> authorItemWriter(MongoTemplate mongoTemplate) {
        return new MongoItemWriterBuilder<AuthorMongo>()
                .collection("authors")
                .template(mongoTemplate)
                .build();
    }

    @Bean
    public ItemWriter<BookMongo> bookItemWriter(MongoTemplate mongoTemplate) {
        return new MongoItemWriterBuilder<BookMongo>()
                .collection("books")
                .template(mongoTemplate)
                .build();
    }

    @Bean
    public Step migrateBookStep(ItemReader<Book> bookItemReader,
                                ItemProcessor<Book, BookMongo> bookItemProcessor,
                                ItemWriter<BookMongo> bookItemWriter) {
        return stepBuilderFactory
                .get("migrateBookStep")
                .<Book, BookMongo>chunk(5)
                .reader(bookItemReader)
                .processor(bookItemProcessor)
                .writer(bookItemWriter)
                .build();
    }

    @Bean
    public Tasklet removeOldIdTasklet(MongoTemplate mongoTemplate) {
        return (stepContribution, chunkContext) -> {
            mongoTemplate.updateMulti(new BasicQuery("{}"),
                    new Update().unset("oldId"),
                    AuthorMongo.class);
            return null;
        };
    }

    @Bean
    public Step removeOldIdStep(Tasklet removeOldIdTasklet) {
        return stepBuilderFactory
                .get("removeOldIdStep")
                .tasklet(removeOldIdTasklet)
                .build();
    }
}
