package ru.biderman.librarywebclassic.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.biderman.librarywebclassic.domain.Author;
import ru.biderman.librarywebclassic.services.AuthorService;
import ru.biderman.librarywebclassic.services.exceptions.AuthorNotFoundException;
import ru.biderman.librarywebclassic.services.exceptions.DeleteAuthorException;

@SuppressWarnings("SameReturnValue")
@Controller
public class AuthorController {
    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping("/authors")
    public String getAuthors(Model model) {
        model.addAttribute("authors", authorService.getAllAuthors());
        model.addAttribute("usedAuthorIdList", authorService.getUsedAuthorIdList());
        return "authors";
    }

    @GetMapping("/authors/edit")
    public String getAuthorEditForm(@RequestParam("id") long id, Model model) throws AuthorNotFoundException {
        Author author = id != 0 ? authorService.findById(id) : Author.createNewAuthor("", "");
        model.addAttribute("author", author);
        return "author-edit";
    }

    @PostMapping("/authors/edit")
    public String postAuthorEditForm(@ModelAttribute Author author) throws AuthorNotFoundException{
        if (author.getId() != 0)
            authorService.updateAuthor(author.getId(), author);
        else
            authorService.addAuthor(author);
        return "redirect:/authors";
    }

    @GetMapping("/authors/delete")
    public String getAuthorDeleteForm(@RequestParam("id") long id, Model model) throws AuthorNotFoundException {
        Author author = authorService.findById(id);
        model.addAttribute("id", author.getId());
        model.addAttribute("stringValue", ViewUtils.getAuthorString(author));
        return "delete-author";
    }

    @PostMapping("/authors/delete")
    public String deleteAuthor(@RequestParam("id") long id) throws DeleteAuthorException {
        authorService.deleteById(id);
        return "redirect:/authors";
    }

    @ExceptionHandler(DeleteAuthorException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String deleteAuthorError(Model model) {
        model.addAttribute("messageId", "error.author-delete");
        return "error";
    }
}
