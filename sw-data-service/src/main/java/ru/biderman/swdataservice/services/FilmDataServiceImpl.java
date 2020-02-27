package ru.biderman.swdataservice.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.biderman.swdataservice.model.Film;
import ru.biderman.swdataservice.model.FilmCharacter;
import ru.biderman.swdataservice.services.dto.FilmCharacterDto;
import ru.biderman.swdataservice.services.dto.FilmDto;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilmDataServiceImpl implements FilmDataService {
    private final DataGetterService dataGetterService;

    @Override
    public Optional<Film> findById(int id) {
        Optional<FilmDto> filmDto = dataGetterService.findById(id);
        return filmDto.map(this::filmToModel);
    }

    private Film filmToModel(FilmDto filmDto) {
        Film result = new Film();
        result.setTitle(filmDto.getTitle());
        result.setDirector(filmDto.getDirector());
        result.setReleaseDate(filmDto.getReleaseDate());
        List<FilmCharacterDto> characterDtos = dataGetterService
                .getCharacters(filmDto.getCharacterUrlList());
        List<FilmCharacter> characterList = characterDtos
                .stream()
                .map(this::characterToModel)
                .collect(Collectors.toList());
        result.setCharacterList(characterList);
        return result;
    }

    private FilmCharacter characterToModel(FilmCharacterDto characterDto) {
        FilmCharacter filmCharacter = new FilmCharacter();
        filmCharacter.setName(characterDto.getName());
        return filmCharacter;
    }
}
