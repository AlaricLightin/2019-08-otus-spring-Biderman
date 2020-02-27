package ru.biderman.swdataservice.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Data
@NoArgsConstructor
public class Film {
    private String title;
    private String director;
    private LocalDate releaseDate;
    private List<FilmCharacter> characterList;

    public Film(String title, String director, LocalDate releaseDate) {
        this.title = title;
        this.director = director;
        this.releaseDate = releaseDate;
        characterList = Collections.emptyList();
    }
}
