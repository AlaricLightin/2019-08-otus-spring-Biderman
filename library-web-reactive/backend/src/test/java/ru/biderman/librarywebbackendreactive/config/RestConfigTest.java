package ru.biderman.librarywebbackendreactive.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.biderman.librarywebbackendreactive.domain.Author;
import ru.biderman.librarywebbackendreactive.domain.Book;
import ru.biderman.librarywebbackendreactive.dto.BookDto;
import ru.biderman.librarywebbackendreactive.exceptions.BookNotFoundException;
import ru.biderman.librarywebbackendreactive.repositories.AuthorRepository;
import ru.biderman.librarywebbackendreactive.repositories.BookRepository;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {RestConfig.class})
@DisplayName("Rest-конфигурация ")
class RestConfigTest {
    @Autowired
    private RouterFunction route;

    @MockBean
    private AuthorRepository authorRepository;

    @MockBean
    private BookRepository bookRepository;

    private WebTestClient client;

    private static final Author TEST_AUTHOR1 = new Author("1", "Surname-1", "Name-1");
    private static final Author TEST_AUTHOR2 = new Author("2", "Surname-2", "Name-2");

    @BeforeEach
    void initWebClient() {
        client = WebTestClient
                .bindToRouterFunction(route)
                .build();
    }

    @DisplayName("должна возвращать всех авторов")
    @Test
    void shouldGetAllAuthors() {
        List<Author> authorList = Arrays.asList(TEST_AUTHOR1, TEST_AUTHOR2);
        when(authorRepository.findAll()).thenReturn(Flux.fromIterable(authorList));

        client
                .get()
                .uri("/author").accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Author.class)
                .isEqualTo(authorList);
    }

    @DisplayName("должен возвращать все жанры")
    @Test
    void shouldGetAllGenres() {
        List<String> genreList = Arrays.asList("Genre-1", "Genre-2");
        when(bookRepository.findAllGenres()).thenReturn(Flux.fromIterable(genreList));

        client
                .get()
                .uri("/genre").accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(List.class)
                .isEqualTo(genreList);
    }

    @DisplayName("должна возвращать все книги")
    @Test
    void shouldGetAllBooks() {
        Book book1 = new Book("100", Arrays.asList(TEST_AUTHOR1, TEST_AUTHOR2), "Title-1", Collections.singleton("Genre-1"));
        Book book2 = new Book("200", Collections.emptyList(), "Title-2", Collections.emptySet());
        List<Book> bookList = Arrays.asList(book1, book2);

        when(bookRepository.findAll()).thenReturn(Flux.fromIterable(bookList));

        client
                .get()
                .uri("/book").accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(BookDto.class)
                .contains(BookDto.createFromBook(book1), BookDto.createFromBook(book2));
    }

    @DisplayName("должна добавлять книгу")
    @Test
    void shouldAddBook() {
        BookDto bookDto = new BookDto("", Arrays.asList("101", "102"), "Title", Arrays.asList("Genre-1", "Genre-2"));
        Book sendBook = BookDto.createFromDto(bookDto);
        sendBook.setId(null);

        Book receivedBook = BookDto.createFromDto(bookDto);
        String newId = "1001";
        receivedBook.setId(newId);

        when(bookRepository.save(eq(sendBook))).thenReturn(Mono.just(receivedBook));

        client
                .post()
                .uri("/book").body(Mono.just(bookDto), BookDto.class)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().valueEquals("Location", "/book/" + newId);
    }

    @DisplayName("должна давать BadRequest при ошибочном post-запросе")
    @Test
    void shouldSendBadRequestIfErrorPost() {
        client
                .post()
                .uri("/book")
                .exchange()
                .expectStatus().isBadRequest();
    }

    @DisplayName("должна обновлять книгу")
    @Test
    void shouldUpdateBook() {
        String id = "1001";
        BookDto bookDto = new BookDto(id, Arrays.asList("101", "102"), "Title", Arrays.asList("Genre-1", "Genre-2"));
        Book book = BookDto.createFromDto(bookDto);

        when(bookRepository.updateIfExists(eq(book))).thenReturn(Mono.just(book));
        client
                .put()
                .uri("/book/" + id).body(Mono.just(bookDto), BookDto.class)
                .exchange()
                .expectStatus().isOk();
    }

    @DisplayName("должна сообщать об ошибке, если обновляемой книги нет")
    @Test
    void shouldSendErrorIfUpdatedBookAbsent() {
        String id = "1001";
        BookDto bookDto = new BookDto(id, Arrays.asList("101", "102"), "Title", Arrays.asList("Genre-1", "Genre-2"));
        Book book = BookDto.createFromDto(bookDto);

        when(bookRepository.updateIfExists(eq(book))).thenReturn(Mono.error(BookNotFoundException::new));
        client
                .put()
                .uri("/book/" + id).body(Mono.just(bookDto), BookDto.class)
                .exchange()
                .expectStatus().isNotFound();
    }

    @DisplayName("должна удалять книгу")
    @Test
    void shouldDeleteBook() {
        String id = "1001";

        when(bookRepository.deleteIfExists(id)).thenReturn(Mono.empty());

        client
                .delete()
                .uri("/book/" + id)
                .exchange()
                .expectStatus().isOk();
    }

    @DisplayName("должна возвращать ошибку, если книги нет")
    @Test
    void shouldSendErrorIfDeletedBookAbsent() {
        String id = "1001";

        when(bookRepository.deleteIfExists(id)).thenReturn(Mono.error(BookNotFoundException::new));

        client
                .delete()
                .uri("/book/" + id)
                .exchange()
                .expectStatus().isNotFound();
    }
}