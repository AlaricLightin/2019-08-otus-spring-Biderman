package ru.biderman.librarywebbackend.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.biderman.librarywebbackend.rest.dto.GenreDto;
import ru.biderman.librarywebbackend.services.GenreService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class GenreController {
    private final GenreService genreService;

    @GetMapping("/genre")
    public List<GenreDto> getAllGenres() {
        return genreService.getAllGenres().stream()
                .map(GenreDto::createFromGenre)
                .collect(Collectors.toList());
    }
}
