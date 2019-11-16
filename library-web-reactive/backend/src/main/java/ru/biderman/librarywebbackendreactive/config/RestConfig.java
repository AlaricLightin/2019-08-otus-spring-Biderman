package ru.biderman.librarywebbackendreactive.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.util.UriComponentsBuilder;
import ru.biderman.librarywebbackendreactive.domain.Author;
import ru.biderman.librarywebbackendreactive.domain.Book;
import ru.biderman.librarywebbackendreactive.dto.BookDto;
import ru.biderman.librarywebbackendreactive.exceptions.BookNotFoundException;
import ru.biderman.librarywebbackendreactive.repositories.AuthorRepository;
import ru.biderman.librarywebbackendreactive.repositories.BookRepository;

import static org.springframework.web.reactive.function.BodyInserters.fromValue;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.*;

@Configuration
public class RestConfig {
    @Bean
    public RouterFunction<ServerResponse> composedRoutes(
            @Value("classpath:/public/index.html") Resource indexHtml,
            AuthorRepository authorRepository,
            BookRepository bookRepository) {
        return route()
                .GET("/", request -> ok().contentType(MediaType.TEXT_HTML).bodyValue(indexHtml))

                .GET("/author" ,
                        request -> ok().body(authorRepository.findAll(), Author.class))

                .GET("/book",
                        request -> ok().body(
                                bookRepository.findAll().map(BookDto::createFromBook), BookDto.class
                        ))

                .POST("/book", accept(MediaType.APPLICATION_JSON),
                        request -> request.bodyToMono(BookDto.class)
                                .doOnNext(dto -> dto.setId(null))
                                .map(BookDto::createFromDto)
                                .flatMap(bookRepository::save)
                                .map(Book::getId)
                                .flatMap(s ->
                                        created(
                                                UriComponentsBuilder.newInstance().path("/book/{id}").build(s)
                                        ).build())

                                .switchIfEmpty(badRequest().build())
                )

                .PUT("/book/{id}", accept(MediaType.APPLICATION_JSON),
                        request ->
                                request.bodyToMono(BookDto.class)
                                        .doOnNext(dto -> dto.setId(request.pathVariable("id")))
                                        .map(BookDto::createFromDto)
                                        .flatMap(bookRepository::updateIfExists)
                                        .then(ok().build())
                                        .onErrorResume(BookNotFoundException.class, err -> notFound().build())
                )

                .DELETE("/book/{id}",
                        request ->
                                bookRepository.deleteIfExists(request.pathVariable("id"))
                                        .then(ok().build())
                                        .onErrorResume(BookNotFoundException.class, err -> notFound().build())
                )

                .GET("/genre",
                        request -> bookRepository.findAllGenres()
                                .collectList()
                                .flatMap(list -> ok().contentType(MediaType.APPLICATION_JSON).body(fromValue(list))))

                .build();
    }
}
