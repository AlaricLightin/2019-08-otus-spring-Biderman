package ru.biderman.swdataservice.services;

import ru.biderman.swdataservice.services.dto.FilmCharacterDto;
import ru.biderman.swdataservice.services.dto.FilmDto;

import java.util.List;
import java.util.Optional;

interface DataGetterService {
    Optional<FilmDto> findById(int id);
    List<FilmCharacterDto> getCharacters(List<String> urlList);
}
