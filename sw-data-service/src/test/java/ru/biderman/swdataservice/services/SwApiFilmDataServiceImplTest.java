package ru.biderman.swdataservice.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;
import ru.biderman.swdataservice.services.dto.FilmCharacterDto;
import ru.biderman.swdataservice.services.dto.FilmDto;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static ru.biderman.swdataservice.services.TestConsts.*;

@ExtendWith(SpringExtension.class)
@DisplayName("Сервис по получению данных из SwApi")
class SwApiFilmDataServiceImplTest {
    private RestTemplate restTemplate;
    private SwApiDataServiceImpl swApiFilmDataService;

    @BeforeEach
    void init() {
        FallbackFilmDtoService fallbackService = mock(FallbackFilmDtoService.class);
        restTemplate = mock(RestTemplate.class);
        swApiFilmDataService = new SwApiDataServiceImpl(restTemplate, fallbackService);
    }

    @DisplayName("должен возвращать данные о фильме")
    @Test
    void shouldGetFilmData() {
        FilmDto dto = mock(FilmDto.class);
        when(restTemplate.getForObject(String.format("https://swapi.co/api/films/%d/", FILM_ID), FilmDto.class))
                .thenReturn(dto);

        assertThat(swApiFilmDataService.findById(FILM_ID))
                .get()
                .isEqualTo(dto);
    }

    @DisplayName("должен возвращать данные о персонажах")
    @Test
    void shouldGetCharactersData() {
        FilmCharacterDto characterDto = new FilmCharacterDto();
        characterDto.setName(CHARACTER_NAME);

        when(restTemplate.getForObject(CHARACTER_URL, FilmCharacterDto.class)).thenReturn(characterDto);

        assertThat(swApiFilmDataService.getCharacters(Collections.singletonList(CHARACTER_URL)))
                .extracting("name")
                .containsExactly(CHARACTER_NAME);
    }

}