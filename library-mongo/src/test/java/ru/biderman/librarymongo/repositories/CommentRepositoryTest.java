package ru.biderman.librarymongo.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.test.annotation.DirtiesContext;
import ru.biderman.librarymongo.domain.Book;
import ru.biderman.librarymongo.domain.Comment;

import java.time.ZonedDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий по работе с книгами ")
@DataMongoTest
@ComponentScan({"ru.biderman.librarymongo.repositories", "ru.biderman.librarymongo.config"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class CommentRepositoryTest {
    @Autowired
    CommentRepository commentRepository;

    @Autowired
    MongoOperations mongoOperations;

    @DisplayName("должен добавлять комментарий")
    @Test
    void shouldAddComment() {
        List<Book> bookList = mongoOperations.findAll(Book.class);
        assertThat(bookList.size()).isGreaterThan(0);

        Book book = bookList.get(0);
        final String user = "User";
        final String commentText = "comment text";
        ZonedDateTime dateTime = ZonedDateTime.now();
        Comment comment = new Comment(user, dateTime, commentText, book);
        commentRepository.save(comment);

        Comment addedComment = mongoOperations.findById(comment.getId(), Comment.class);
        assertThat(addedComment)
                .hasFieldOrPropertyWithValue("user", user)
                .hasFieldOrPropertyWithValue("text", commentText)
                .satisfies(c -> assertThat(c.getBook()).hasFieldOrPropertyWithValue("id", book.getId()))
                .satisfies(c -> assertThat(c.getDateTime()).isEqualTo(dateTime));
    }
}