package ru.biderman.library.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.biderman.library.domain.Book;
import ru.biderman.library.domain.Comment;
import ru.biderman.library.repositories.CommentRepository;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@DisplayName("Сервис по работе с комментариями ")
class CommentServiceImplTest {
    private CommentRepository commentRepository;
    private CommentServiceImpl commentService;

    @BeforeEach
    void init() {
        commentRepository = mock(CommentRepository.class);
        commentService = new CommentServiceImpl(commentRepository);
    }

    @DisplayName("должен добавлять комментарий")
    @Test
    void shouldAddComment() {
        Comment comment = mock(Comment.class);
        commentService.addComment(comment);
        verify(commentRepository).save(comment);
    }

    @DisplayName("должен удалять комментарий")
    @Test
    void shouldDeleteComment() {
        final long commentId = 1;
        commentService.deleteCommentById(commentId);
        verify(commentRepository).deleteById(commentId);
    }

    @DisplayName("должен возвращать комментарии по книге")
    @Test
    void shouldGetCommentsByBook() {
        Book book = mock(Book.class);
        Comment comment = mock(Comment.class);
        when(commentRepository.findByBook(book)).thenReturn(Collections.singletonList(comment));
        assertThat(commentService.getCommentsByBook(book)).containsOnly(comment);
    }
}