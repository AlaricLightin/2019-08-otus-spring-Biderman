package ru.biderman.library.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.biderman.library.dao.CommentDao;
import ru.biderman.library.domain.Book;
import ru.biderman.library.domain.Comment;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@DisplayName("Сервис по работе с комментариями ")
class CommentServiceImplTest {
    private CommentDao commentDao;
    private CommentServiceImpl commentService;

    @BeforeEach
    void init() {
        commentDao = mock(CommentDao.class);
        commentService = new CommentServiceImpl(commentDao);
    }

    @DisplayName("должен добавлять комментарий")
    @Test
    void shouldAddComment() {
        Comment comment = mock(Comment.class);
        commentService.addComment(comment);
        verify(commentDao).addComment(comment);
    }

    @DisplayName("должен удалять комментарий")
    @Test
    void shouldDeleteComment() {
        final long commentId = 1;
        commentService.deleteCommentById(commentId);
        verify(commentDao).deleteCommentById(commentId);
    }

    @DisplayName("должен возвращать комментарии по книге")
    @Test
    void shouldGetCommentsByBook() {
        Book book = mock(Book.class);
        Comment comment = mock(Comment.class);
        when(commentDao.getCommentsByBook(book)).thenReturn(Collections.singletonList(comment));
        assertThat(commentService.getCommentsByBook(book)).containsOnly(comment);
    }
}