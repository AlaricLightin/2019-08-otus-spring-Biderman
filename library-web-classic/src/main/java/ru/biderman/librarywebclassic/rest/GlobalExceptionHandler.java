package ru.biderman.librarywebclassic.rest;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.biderman.librarywebclassic.services.exceptions.AuthorNotFoundException;

@SuppressWarnings("SameReturnValue")
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(AuthorNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String authorNotFound(Model model) {
        model.addAttribute("messageId", "error.author-not-found");
        return "error";
    }
}
