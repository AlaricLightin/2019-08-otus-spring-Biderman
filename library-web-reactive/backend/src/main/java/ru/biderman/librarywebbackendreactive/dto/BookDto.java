package ru.biderman.librarywebbackendreactive.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.biderman.librarywebbackendreactive.domain.Author;
import ru.biderman.librarywebbackendreactive.domain.Book;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDto {
    private String id;
    private List<String> authors;
    private String title;
    private List<String> genres;

    public static BookDto createFromBook(Book book) {
        return new BookDto(
                book.getId(),
                book.getAuthorList().stream().map(Author::getId).collect(Collectors.toList()),
                book.getTitle(),
                new ArrayList<>(book.getGenres()));
    }

    public static Book createFromDto(BookDto dto) {
        return new Book(
                dto.getId(),
                dto.getAuthors().stream()
                        .map(id -> {
                            Author author = new Author();
                            author.setId(id);
                            return author;
                        })
                        .collect(Collectors.toList()),
                dto.getTitle(),
                new HashSet<>(dto.getGenres())
        );
    }
}
