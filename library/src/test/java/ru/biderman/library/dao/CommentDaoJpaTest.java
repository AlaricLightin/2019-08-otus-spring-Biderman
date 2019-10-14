package ru.biderman.library.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.biderman.library.domain.Book;
import ru.biderman.library.domain.Comment;
import ru.biderman.library.testutils.TestData;

import java.time.ZonedDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@DisplayName("Dao для работы с комментариями ")
@DataJpaTest
@ExtendWith(SpringExtension.class)
@EntityScan(basePackageClasses = {Comment.class, Book.class})
@Import(CommentDaoJpa.class)
class CommentDaoJpaTest {
    @Autowired
    TestEntityManager testEntityManager;

    @Autowired
    CommentDaoJpa commentDaoJpa;

    @DisplayName("должен добавлять комментарий")
    @Test
    void shouldAddComment() {
        Book book = testEntityManager.find(Book.class, TestData.EXISTING_BOOK_ID);
        assertNotNull(book);
        final String user = "User";
        final String commentText = "comment text";
        ZonedDateTime dateTime = ZonedDateTime.now();
        Comment comment = new Comment(user, dateTime, commentText, book);
        commentDaoJpa.addComment(comment);
        assertThat(comment.getId()).isGreaterThan(0);

        Comment addedComment = testEntityManager.find(Comment.class, comment.getId());
        assertThat(addedComment).isEqualToComparingFieldByField(comment);
    }

    @DisplayName("должен удалять комментарий")
    @Test
    void shouldDeleteComment() {
        commentDaoJpa.deleteCommentById(TestData.EXISTING_COMMENT_ID);
        Comment comment = testEntityManager.find(Comment.class, TestData.EXISTING_COMMENT_ID);
        assertNull(comment);
    }

    @DisplayName("должен удалять все комментарии, соответствующие книге")
    @Test
    void shouldDeleteAllCommentsByBook() {
        Book book = testEntityManager.find(Book.class, TestData.EXISTING_BOOK_ID);
        assertNotNull(book);

        commentDaoJpa.deleteAllCommentsByBook(book);
        Comment comment = testEntityManager.find(Comment.class, TestData.EXISTING_COMMENT_ID);
        assertNull(comment);
    }

    @DisplayName("должен возвращать все комментарии для книги")
    @Test
    void shouldGetCommentsByBook() {
        Book book = testEntityManager.find(Book.class, TestData.EXISTING_BOOK_ID);
        assertNotNull(book);

        List<Comment> comments = commentDaoJpa.getCommentsByBook(book);
        assertThat(comments)
                .extracting("user", "text")
                .containsOnly(tuple(TestData.EXISTING_COMMENT_USER, TestData.EXISTING_COMMENT_TEXT));
    }
}