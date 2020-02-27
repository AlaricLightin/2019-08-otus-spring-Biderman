package ru.biderman.swdataservice.services;

import org.springframework.stereotype.Service;
import ru.biderman.swdataservice.services.dto.FilmDto;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Optional;

@Service
class FallbackFilmDtoService {
    private static final HashMap<Integer, FilmDto> filmMap = new HashMap<>();
    static {
        filmMap.put(1, new FilmDto("A New Hope", "George Lucas", LocalDate.of(1977, 5, 25)));
        filmMap.put(2, new FilmDto("The Empire Strikes Back", "Irvin Kershner", LocalDate.of(1980, 5, 17)));
        filmMap.put(3, new FilmDto("Return of the Jedi", "Richard Marquand", LocalDate.of(1983, 5, 25)));
        filmMap.put(4, new FilmDto("The Phantom Menace", "George Lucas", LocalDate.of(1999, 5, 19)));
        filmMap.put(5, new FilmDto("Attack of the Clones", "George Lucas", LocalDate.of(2002, 5, 16)));
        filmMap.put(6, new FilmDto("Revenge of the Sith", "George Lucas", LocalDate.of(2005, 5, 19)));
        filmMap.put(7, new FilmDto("The Force Awakens", "J. J. Abrams", LocalDate.of(2015, 12, 11)));
        filmMap.put(8, new FilmDto("The Last Jedi", "Rian Johnson", LocalDate.of(2017, 12, 9)));
        filmMap.put(9, new FilmDto("The Rise of Skywalker", "J. J. Abrams", LocalDate.of(2019, 12, 16)));
    }

    public Optional<FilmDto> findById(int id) {
        return Optional.ofNullable(filmMap.get(id));
    }
}
