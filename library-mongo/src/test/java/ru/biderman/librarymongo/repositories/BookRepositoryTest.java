package ru.biderman.librarymongo.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.annotation.DirtiesContext;
import ru.biderman.librarymongo.domain.Author;
import ru.biderman.librarymongo.domain.Book;
import ru.biderman.librarymongo.domain.Comment;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static ru.biderman.librarymongo.testutils.TestData.BOOK_WITH_COMMENT_TITLE;

@DisplayName("Репозиторий по работе с книгами ")
@DataMongoTest
@ComponentScan({"ru.biderman.librarymongo.repositories",
        "ru.biderman.librarymongo.config",
        "ru.biderman.librarymongo.events"})
class BookRepositoryTest {
    @Autowired
    BookRepository bookRepository;
    @Autowired
    MongoOperations mongoOperations;
    private final static String AUTHOR_SURNAME = "Пушкин";
    private final static String GENRE1 = "Поэзия";
    private final static String GENRE2 = "Роман";
    private final static String TITLE = "Борис Годунов";

    @DisplayName("должен добавлять книгу")
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void shouldAddBook() {
        Query query = new Query();
        query.addCriteria(Criteria.where("surname").is(AUTHOR_SURNAME));
        Author author = mongoOperations.findOne(query, Author.class);
        assertNotNull(author);

        Book newBook = Book.createNewBook(Collections.singletonList(author), TITLE, Collections.singleton(GENRE1));
        bookRepository.save(newBook);

        Book book = mongoOperations.findById(newBook.getId(), Book.class);
        assertThat(book)
                .hasFieldOrPropertyWithValue("title", TITLE)
                .satisfies(b -> assertThat(b.getAuthorList()).extracting("id").containsOnly(author.getId()))
                .satisfies(b -> assertThat(b.getGenres()).containsOnly(GENRE1));
    }

    @DisplayName("должен удалять книгу с комментариями")
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void shouldDeleteBookWithComments() {
        assertThat(mongoOperations.findAll(Book.class)).hasSize(2);
        assertThat(mongoOperations.findAll(Comment.class)).hasSize(1);

        Query query = new Query();
        query.addCriteria(Criteria.where("title").is(BOOK_WITH_COMMENT_TITLE));
        Book book = mongoOperations.findOne(query, Book.class);
        assertThat(book).isNotNull();

        bookRepository.delete(book);

        assertThat(mongoOperations.findAll(Book.class)).hasSize(1);
        assertThat(mongoOperations.findAll(Comment.class)).hasSize(0);
    }

    @DisplayName("должен менять жанры")
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void shouldUpdateGenres() {
        final String NEW_GENRE = "Стихи";
        bookRepository.updateGenre(GENRE1, NEW_GENRE);

        Query query = new Query();
        query.addCriteria(Criteria.where("genres").all(NEW_GENRE));
        List<Book> bookList = mongoOperations.find(query, Book.class);
        assertThat(bookList).hasSize(1);
        assertThat(bookList.get(0).getGenres()).containsOnly(GENRE2, NEW_GENRE);
    }

    @DisplayName("должен возвращать, когда автор используется")
    @Test
    void shouldCheckWhenAuthorUsed() {
        Query query = new Query();
        query.addCriteria(Criteria.where("surname").is(AUTHOR_SURNAME));
        Author author = mongoOperations.findOne(query, Author.class);
        assertNotNull(author);

        assertThat(bookRepository.isAuthorUsed(author.getId())).isTrue();
    }

    @DisplayName("должен возвращать, когда автор не используется")
    @Test
    void shouldCheckWhenAuthorNotUsed() {
        assertThat(bookRepository.isAuthorUsed("incorrect id")).isFalse();
    }
}