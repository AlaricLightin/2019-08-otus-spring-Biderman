package ru.biderman.librarywebbackendreactive.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.biderman.librarywebbackendreactive.domain.Author;
import ru.biderman.librarywebbackendreactive.domain.Book;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Объект BookDto ")
class BookDtoTest {

    @DisplayName("должен создаваться из Book")
    @Test
    void shouldCreateFromBook() {
        final String id = "100";
        Author author1 = new Author("101", "Author1", "Name");
        Author author2 = new Author("102", "Author2", "Name2");
        List<Author> authorList = Arrays.asList(author1, author2);
        final String title = "Title";
        String genre = "Genre-text";

        Book book = new Book(id, authorList, title, Collections.singleton(genre));
        assertThat(BookDto.createFromBook(book))
                .hasFieldOrPropertyWithValue("id", id)
                .hasFieldOrPropertyWithValue("title", title)
                .satisfies(b -> assertThat(b.getAuthors()).containsOnly(
                        author1.getId(),
                        author2.getId()
                ))
                .satisfies(b -> assertThat(b.getGenres()).containsOnly(genre));
    }

    @DisplayName("Book должен создаваться из BookDto")
    @Test
    void shouldCreateBookFromDto() {
        final String bookId = "100";
        final String authorId1 = "1000";
        final String authorId2 = "1001";
        final String genre = "Genre-text";
        final String title = "Title";
        BookDto dto = new BookDto(bookId, Arrays.asList(authorId1, authorId2), title,
                Collections.singletonList(genre));

        assertThat(BookDto.createFromDto(dto))
                .hasFieldOrPropertyWithValue("id", bookId)
                .hasFieldOrPropertyWithValue("title", title)
                .satisfies(book -> assertThat(book.getAuthorList())
                        .extracting("id")
                        .containsOnly(authorId1, authorId2))
                .satisfies(book -> assertThat(book.getGenres())
                        .containsOnly(genre));
    }
}