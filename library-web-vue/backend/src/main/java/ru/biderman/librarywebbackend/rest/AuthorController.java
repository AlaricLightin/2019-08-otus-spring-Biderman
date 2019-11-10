package ru.biderman.librarywebbackend.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.biderman.librarywebbackend.rest.dto.AuthorDto;
import ru.biderman.librarywebbackend.services.AuthorService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class AuthorController {
    private final AuthorService authorService;

    @GetMapping("/author")
    public List<AuthorDto> getAllAuthors() {
        return authorService.getAllAuthors().stream()
                .map(AuthorDto::createFromAuthor)
                .collect(Collectors.toList());
    }
}
