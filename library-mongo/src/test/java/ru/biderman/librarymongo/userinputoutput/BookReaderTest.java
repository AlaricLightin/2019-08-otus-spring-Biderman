package ru.biderman.librarymongo.userinputoutput;

import org.junit.jupiter.api.Test;
import ru.biderman.librarymongo.domain.Author;
import ru.biderman.librarymongo.domain.Book;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class BookReaderTest {

    @SuppressWarnings("unchecked")
    @Test
    void shouldGetBook() {
        final String BOOK_TITLE = "Book Title";
        Author author = new Author("1", "Surname", "Name");
        String genre = "Some genre";
        UserInterface userInterface = mock(UserInterface.class);
        when(userInterface.readValue(any(), any())).thenReturn(
                Optional.of(author),
                Optional.empty(),
                Optional.of(BOOK_TITLE),
                Optional.of(genre),
                Optional.empty()
        );

        BookReader bookReader = new BookReaderImpl(userInterface);
        Book book = bookReader.getBook(Collections.emptyList());
        assertThat(book).hasFieldOrPropertyWithValue("title", BOOK_TITLE);
        assertThat(book.getAuthorList()).containsOnly(author);
        assertThat(book.getGenres()).containsOnly(genre);

        verify(userInterface).printText("shell.all-authors-prompt");
        verify(userInterface).printText("shell.all-genres-prompt");
    }

}