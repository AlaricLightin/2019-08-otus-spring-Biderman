package ru.biderman.swdataservice.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.biderman.swdataservice.services.dto.FilmCharacterDto;
import ru.biderman.swdataservice.services.dto.FilmDto;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static ru.biderman.swdataservice.services.TestConsts.*;

@ExtendWith(SpringExtension.class)
@DisplayName("Сервис получения и преобразования данных")
class FilmDataServiceImplTest {
    private DataGetterService dataGetterService;
    private FilmDataServiceImpl filmDataService;

    @BeforeEach
    void init() {
        dataGetterService = mock(DataGetterService.class);
        filmDataService = new FilmDataServiceImpl(dataGetterService);
    }

    @DisplayName("должен получать данные о фильме")
    @Test
    void shouldGetData() {
        FilmDto filmDto = new FilmDto(FILM_TITLE, FILM_DIRECTOR, RELEASE_DATE);
        List<String> characterUrlList = Collections.singletonList(CHARACTER_URL);
        filmDto.setCharacterUrlList(characterUrlList);
        when(dataGetterService.findById(FILM_ID)).thenReturn(Optional.of(filmDto));

        FilmCharacterDto characterDto = new FilmCharacterDto();
        characterDto.setName(CHARACTER_NAME);
        when(dataGetterService.getCharacters(characterUrlList))
                .thenReturn(Collections.singletonList(characterDto));

        assertThat(filmDataService.findById(FILM_ID))
                .get()
                .hasFieldOrPropertyWithValue("title", FILM_TITLE)
                .hasFieldOrPropertyWithValue("director", FILM_DIRECTOR)
                .hasFieldOrPropertyWithValue("releaseDate", RELEASE_DATE)
                .satisfies(film -> assertThat(film.getCharacterList())
                        .extracting("name")
                        .containsExactly(CHARACTER_NAME)
                );
    }
}