package ru.biderman.librarywebbackend.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import ru.biderman.librarywebbackend.domain.Book;
import ru.biderman.librarywebbackend.rest.dto.BookDto;
import ru.biderman.librarywebbackend.services.BookService;
import ru.biderman.librarywebbackend.services.exceptions.BookNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @GetMapping("/book")
    public List<BookDto> getAllBooks() {
        return bookService.getAllBooks().stream()
                .map(BookDto::createFromBook)
                .collect(Collectors.toList());
    }

    @PostMapping("/book")
    public ResponseEntity<?> createBook(@RequestBody BookDto bookDto, UriComponentsBuilder builder) {
        Book book = BookDto.createFromDto(bookDto);
        book.setId(0);
        Book addedBook = bookService.createBook(book);
        UriComponents uriComponents = builder.path("/book/{id}").buildAndExpand(addedBook.getId());
        return ResponseEntity
                .created(uriComponents.toUri())
                .build();
    }

    @PutMapping("/book/{id}")
    public void updateBook(@PathVariable("id") long id, @RequestBody BookDto bookDto)
            throws BookNotFoundException {
        Book book = BookDto.createFromDto(bookDto);
        book.setId(id);
        bookService.updateBook(book);
    }

    @DeleteMapping("/book/{id}")
    public void deleteBook(@PathVariable("id") long id) throws BookNotFoundException{
        bookService.deleteById(id);
    }

    @ExceptionHandler(BookNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void bookNotFoundHandler() {

    }
}
