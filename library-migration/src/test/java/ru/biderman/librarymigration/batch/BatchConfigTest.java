package ru.biderman.librarymigration.batch;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.MetaDataInstanceFactory;
import org.springframework.batch.test.StepScopeTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.test.annotation.DirtiesContext;
import ru.biderman.librarymigration.domain.Author;
import ru.biderman.librarymigration.domain.Book;
import ru.biderman.librarymigration.domain.Genre;
import ru.biderman.librarymigration.domainmongo.AuthorMongo;
import ru.biderman.librarymigration.domainmongo.BookMongo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@DisplayName("Пакетная обработка")
class BatchConfigTest {
    @Autowired
    ItemReader<Author> authorItemReader;

    @Autowired
    ItemReader<Book> bookItemReader;

    @Autowired
    ItemProcessor<Author, AuthorMongo> authorItemProcessor;

    @Autowired
    ItemProcessor<Book, BookMongo> bookItemProcessor;

    @Autowired
    JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private MongoOperations mongoOperations;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public JobLauncherTestUtils jobLauncherTestUtils() {
            return new JobLauncherTestUtils();
        }
    }

    private static final Author AUTHOR1 = new Author(1, "Ivanov", "Ivan Ivanovich");
    private static final Author AUTHOR2 = new Author( 2, "Smith", "John" );
    private static final Author AUTHOR3 = new Author(3, "Another", "Author");
    private static final int AUTHOR_COUNT = 3;
    private static final String BOOK_TITLE = "Book Name";
    private static final String GENRE_STRING = "Test genre 0";

    @Nested
    class StepTest {
        private StepExecution stepExecution;
        @BeforeEach
        void initStepExecution() {
            stepExecution = MetaDataInstanceFactory.createStepExecution(new JobParametersBuilder().toJobParameters());
        }

        @DisplayName("должна получать авторов")
        @Test
        void shouldReadAuthors() throws Exception{
            StepScopeTestUtils.doInStepScope(stepExecution, () -> {
                List<Author> authorList = new ArrayList<>();
                Author author;
                do {
                    author = authorItemReader.read();
                    if (author != null)
                        authorList.add(author);
                }
                while (author != null);

                assertThat(authorList).containsOnly(AUTHOR1, AUTHOR2, AUTHOR3);
                return null;
            });
        }

        @DisplayName("должна получать книги")
        @Test
        void shouldReadBooks() throws Exception {
            StepScopeTestUtils.doInStepScope(stepExecution, () -> {
                Book book = bookItemReader.read();
                assertThat(book)
                        .hasFieldOrPropertyWithValue("title", BOOK_TITLE)
                        .satisfies(b -> assertThat(b.getAuthorList()).containsOnly(AUTHOR1, AUTHOR2));
                return null;
            });
        }

        @DisplayName("должна обрабатывать автора")
        @Test
        void shouldProcessAuthor() throws Exception{
            StepScopeTestUtils.doInStepScope(stepExecution, () -> {
                assertThat(authorItemProcessor.process(AUTHOR1))
                        .hasFieldOrPropertyWithValue("surname", AUTHOR1.getSurname())
                        .hasFieldOrPropertyWithValue("otherNames", AUTHOR1.getOtherNames())
                        .hasFieldOrPropertyWithValue("oldId", AUTHOR1.getId());

                return null;
            });
        }

        @DisplayName("должна преобразовывать книгу")
        @Test
        @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
        void shouldProcessBook() throws Exception {
            Genre genre = new Genre(11, "Genre");
            Book book = new Book(101,
                    Arrays.asList(AUTHOR1, AUTHOR2),
                    BOOK_TITLE,
                    Collections.singleton(genre)
            );

            mongoOperations.save(new AuthorMongo(null, AUTHOR1.getSurname(), AUTHOR1.getOtherNames(), AUTHOR1.getId()));
            mongoOperations.save(new AuthorMongo(null, AUTHOR2.getSurname(), AUTHOR2.getOtherNames(), AUTHOR2.getId()));
            mongoOperations.save(new AuthorMongo(null, AUTHOR3.getSurname(), AUTHOR3.getOtherNames(), AUTHOR3.getId()));

            StepScopeTestUtils.doInStepScope(stepExecution, () -> {
                assertThat(bookItemProcessor.process(book))
                        .hasFieldOrPropertyWithValue("title", BOOK_TITLE)
                        .satisfies(b -> assertThat(b.getAuthorList())
                                .satisfies(authorMongo -> assertThat(authorMongo).extracting(AuthorMongo::getId).isNotNull())
                                .extracting("surname", "otherNames", "oldId")
                                .containsOnly(
                                        tuple(AUTHOR1.getSurname(), AUTHOR1.getOtherNames(), AUTHOR1.getId()),
                                        tuple(AUTHOR2.getSurname(), AUTHOR2.getOtherNames(), AUTHOR2.getId())
                                )
                        )
                        .satisfies(b -> assertThat(b.getGenres()).contains(genre.getText()));

                return null;
            });
        }
    }

    @DisplayName("должна делать всё")
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void shouldDoAll() throws Exception{
        JobExecution jobExecution = jobLauncherTestUtils.launchJob();

        assertEquals("COMPLETED", jobExecution.getExitStatus().getExitCode());
        List<AuthorMongo> authorMongoList = mongoOperations.findAll(AuthorMongo.class);
        assertThat(authorMongoList)
                .hasSize(AUTHOR_COUNT)
                .allSatisfy(authorMongo -> assertThat(authorMongo)
                        .hasFieldOrPropertyWithValue("oldId", 0L)
                );

        List<BookMongo> bookMongoList = mongoOperations.findAll(BookMongo.class);
        assertThat(bookMongoList).hasSize(1);
        assertThat(bookMongoList.get(0))
                .hasFieldOrPropertyWithValue("title", BOOK_TITLE)
                .satisfies(b -> assertThat(b.getAuthorList())
                        .extracting("surname", "otherNames")
                        .containsOnly(
                                tuple(AUTHOR1.getSurname(), AUTHOR1.getOtherNames()),
                                tuple(AUTHOR2.getSurname(), AUTHOR2.getOtherNames())
                        )
                )
                .satisfies(b -> assertThat(b.getGenres()).containsOnly(GENRE_STRING));
    }

}