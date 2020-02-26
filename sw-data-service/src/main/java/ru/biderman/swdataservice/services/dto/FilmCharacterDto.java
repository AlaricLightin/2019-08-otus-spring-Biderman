package ru.biderman.swdataservice.services.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class FilmCharacterDto {
    @JsonProperty("name")
    private String name;
}
