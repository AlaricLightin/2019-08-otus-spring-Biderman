package ru.biderman.swdataservice.services;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.biderman.swdataservice.services.dto.FilmCharacterDto;
import ru.biderman.swdataservice.services.dto.FilmDto;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
class SwApiDataServiceImpl implements DataGetterService {
    private final RestTemplate restTemplate;
    private final FallbackFilmDtoService fallbackService;

    @HystrixCommand(fallbackMethod = "getFilmFromFallbackService")
    @Override
    public Optional<FilmDto> findById(int id) {
        String url = String.format("https://swapi.co/api/films/%d/", id);
        FilmDto filmDto = restTemplate.getForObject(url, FilmDto.class);
        return Optional.ofNullable(filmDto);
    }

    @HystrixCommand(defaultFallback = "getEmptyList", commandProperties= {
            @HystrixProperty(name="execution.isolation.thread.timeoutInMilliseconds", value="10000")
    })
    @Override
    public List<FilmCharacterDto> getCharacters(List<String> urlList) {
        List<FilmCharacterDto> characterList = new ArrayList<>();
        return urlList.parallelStream()
                .map(url -> restTemplate.getForObject(url, FilmCharacterDto.class))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private Optional<FilmDto> getFilmFromFallbackService(int id) {
        return fallbackService.findById(id);
    }

    private List<FilmCharacterDto> getEmptyList() {
        return Collections.emptyList();
    }
}
