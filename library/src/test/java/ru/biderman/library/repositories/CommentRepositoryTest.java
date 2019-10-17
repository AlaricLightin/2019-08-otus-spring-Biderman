package ru.biderman.library.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.biderman.library.domain.Book;
import ru.biderman.library.domain.Comment;
import ru.biderman.library.testutils.TestData;

import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("Репозиторий для работы с комментариями ")
@DataJpaTest
class CommentRepositoryTest {
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @DisplayName("должен добавлять комментарий")
    @Test
    void shouldAddComment() {
        Book book = testEntityManager.find(Book.class, TestData.EXISTING_BOOK_ID);
        assertNotNull(book);
        final String user = "User";
        final String commentText = "comment text";
        ZonedDateTime dateTime = ZonedDateTime.now();
        Comment comment = new Comment(user, dateTime, commentText, book);
        commentRepository.save(comment);
        assertThat(comment.getId()).isGreaterThan(0);

        Comment addedComment = testEntityManager.find(Comment.class, comment.getId());
        assertThat(addedComment).isEqualToComparingFieldByField(comment);
    }
}