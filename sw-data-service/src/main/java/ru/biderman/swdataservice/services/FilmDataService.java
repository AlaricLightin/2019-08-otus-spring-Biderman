package ru.biderman.swdataservice.services;

import ru.biderman.swdataservice.model.Film;

import java.util.Optional;

public interface FilmDataService {
    Optional<Film> findById(int id);
}
