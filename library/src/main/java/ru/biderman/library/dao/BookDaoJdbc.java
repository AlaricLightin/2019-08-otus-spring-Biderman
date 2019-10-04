package ru.biderman.library.dao;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.biderman.library.domain.Book;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"SqlNoDataSourceInspection", "SqlResolve"})
@Repository
public class BookDaoJdbc implements BookDao {
    private final NamedParameterJdbcOperations jdbc;
    private final AuthorDao authorDao;
    private final GenreDao genreDao;

    public BookDaoJdbc(NamedParameterJdbcOperations jdbc, AuthorDao authorDao, GenreDao genreDao) {
        this.jdbc = jdbc;
        this.authorDao = authorDao;
        this.genreDao = genreDao;
    }

    @Override
    public void addBook(Book book) {
        Map<String, Object> bookParams = Collections.singletonMap("title", book.getTitle());
        jdbc.update("insert into books(title) values (:title)", bookParams);
        Long newId = jdbc.getJdbcOperations().queryForObject("select scope_identity()", Long.class);
        book.getAuthorList().forEach(author -> {
            Map<String, Object> params = new HashMap<>();
            params.put("book_id", newId);
            params.put("author_id", author.getId());
            jdbc.update("insert into book_authors(book_id, author_id) values(:book_id, :author_id)", params);
        });
        book.getGenreList().forEach(genre -> {
            Map<String, Object> params = new HashMap<>();
            params.put("book_id", newId);
            params.put("genre_id", genre.getId());
            jdbc.update("insert into book_genres(book_id, genre_id) values(:book_id, :genre_id)", params);

        });
    }

    @Override
    public void deleteBook(long id) {
        Map<String, Object> params = Collections.singletonMap("id", id);
        jdbc.update("delete from book_authors where book_id = :id", params);
        jdbc.update("delete from book_genres where book_id = :id", params);
        jdbc.update("delete from books where id = :id", params);
    }

    @Override
    public List<Book> getAllBooks() {
        return jdbc.query(
                "select books.id, books.title, book_authors.author_id, book_genres.genre_id " +
                        "from (books left join book_authors on books.id = book_authors.book_id) " +
                        "left join book_genres on books.id = book_genres.book_id",
                new BookResultSetExtractor(authorDao.getAllAuthors(), genreDao.getAllGenres())
        );
    }

    @Override
    public Book getBookById(long id) {
        List<Book> books = jdbc.query(
                "select books.id, books.title, book_authors.author_id, book_genres.genre_id " +
                        "from (books left join book_authors on books.id = book_authors.book_id) " +
                        "left join book_genres on books.id = book_genres.book_id " +
                        "where books.id = :id",
                Collections.singletonMap("id", id),
                new BookResultSetExtractor(authorDao.getAllAuthors(), genreDao.getAllGenres())
        );
        if (books != null && books.size() > 0)
            return books.get(0);
        else
            return null;
    }
}
