package ru.biderman.librarywebbackend.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.biderman.librarywebbackend.domain.Author;
import ru.biderman.librarywebbackend.domain.Book;
import ru.biderman.librarywebbackend.domain.Genre;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class BookDto {
    private long id;
    private List<Long> authors;
    private String title;
    private List<Long> genres;

    public static BookDto createFromBook(Book book) {
        return new BookDto(
                book.getId(),
                book.getAuthorList().stream().map(Author::getId).collect(Collectors.toList()),
                book.getTitle(),
                book.getGenres().stream().map(Genre::getId).collect(Collectors.toList()));
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
                dto.getGenres().stream()
                        .map(id -> {
                            Genre genre = new Genre();
                            genre.setId(id);
                            return genre;
                        })
                        .collect(Collectors.toSet())
        );
    }
}
