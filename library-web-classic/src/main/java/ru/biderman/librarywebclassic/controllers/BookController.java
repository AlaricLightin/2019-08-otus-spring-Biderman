package ru.biderman.librarywebclassic.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.biderman.librarywebclassic.domain.Book;
import ru.biderman.librarywebclassic.services.DatabaseService;
import ru.biderman.librarywebclassic.services.exceptions.BookNotFoundException;

import java.util.Collections;

@SuppressWarnings("SameReturnValue")
@Controller
public class BookController {
    private final DatabaseService databaseService;

    public BookController(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    @GetMapping("/")
    public String getBooks(Model model) {
        model.addAttribute("books", databaseService.getAllBooks());
        return "books";
    }

    @GetMapping("/books/edit")
    public String editBook(@RequestParam("id") long id, Model model) throws BookNotFoundException {
        Book book = id != 0 ? databaseService.getBookById(id)
                : Book.createNewBook(Collections.emptyList(), "", Collections.emptySet());
        model.addAttribute("book", book);
        model.addAttribute("allAuthors", databaseService.getAllAuthors());
        model.addAttribute("allGenres", databaseService.getAllGenres());
        return "book-edit";
    }

    @PostMapping("books/edit")
    public String postBookEditForm(@ModelAttribute("book") BookDto bookDto) {
        databaseService.saveBook(bookDto.getBook(), bookDto.isAdultOnly());
        return "redirect:/";
    }

    @GetMapping("/books/delete")
    public String deleteBookEditForm(@RequestParam("id") long id, Model model) throws BookNotFoundException{
        Book book = databaseService.getBookById(id);
        model.addAttribute("id", book.getId());
        model.addAttribute("stringValue", ViewUtils.getBookString(book));
        return "delete-book";
    }

    @PostMapping("/books/delete")
    public String deleteBook(@RequestParam("id") long id) {
        databaseService.deleteBookById(id);
        return "redirect:/";
    }

    @ExceptionHandler(BookNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String bookNotFoundError(Model model) {
        model.addAttribute("messageId", "error.book-not-found");
        return "error";
    }
}
