package ru.biderman.librarywebclassic.rest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.biderman.librarywebclassic.domain.Book;
import ru.biderman.librarywebclassic.services.DatabaseService;

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
    public String editBook(@RequestParam("id") long id, Model model) {
        Book book = id != 0 ? databaseService.getBookById(id)
                : Book.createNewBook(Collections.emptyList(), "", Collections.emptySet());
        model.addAttribute("book", book);
        model.addAttribute("allAuthors", databaseService.getAllAuthors());
        model.addAttribute("allGenres", databaseService.getAllGenres());
        return "book-edit";
    }

    @PostMapping("books/edit")
    public String postBookEditForm(@ModelAttribute Book book) {
        databaseService.saveBook(book);
        return "redirect:/";
    }

    @GetMapping("/books/delete")
    public String deleteBook(@RequestParam("id") long id) {
        databaseService.deleteBookById(id);
        return "redirect:/";
    }
}
