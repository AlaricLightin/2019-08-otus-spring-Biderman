package ru.biderman.swdataservice.services.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Data
@NoArgsConstructor
public class FilmDto {
    @JsonProperty("title")
    private String title;

    @JsonProperty("director")
    private String director;

    @JsonProperty("release_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate releaseDate;

    @JsonProperty("characters")
    private List<String> characterUrlList;

    public FilmDto(String title, String director, LocalDate releaseDate) {
        this.title = title;
        this.director = director;
        this.releaseDate = releaseDate;
        this.characterUrlList = Collections.emptyList();
    }

}
