package ru.biderman.library.dao;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.biderman.library.domain.Author;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@SuppressWarnings({"SqlNoDataSourceInspection", "SqlResolve"})
@Repository
public class AuthorDaoJdbc implements AuthorDao{
    private final NamedParameterJdbcOperations jdbc;

    public AuthorDaoJdbc(NamedParameterJdbcOperations jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public void addAuthor(Author author) {
        Map<String, Object> params = new HashMap<>();
        params.put("surname", author.getSurname());
        params.put("other_names", author.getOtherNames());
        jdbc.update("insert into authors(surname, other_names) values(:surname, :other_names)", params);
    }

    @Override
    public void updateAuthor(long id, Author author) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("surname", author.getSurname());
        params.put("other_names", author.getOtherNames());
        jdbc.update("update authors set surname = :surname, other_names = :other_names where id = :id", params);
    }

    @Override
    public void deleteAuthor(long id) throws DaoException{
        Map<String, Object> params = Collections.singletonMap("id", id);
        try {
            jdbc.update("delete from authors where id = :id", params);
        } catch (DataAccessException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public Map<Long, Author> getAllAuthors() {
        return jdbc.query("select * from authors", new AuthorsMapper()).stream()
                .collect(Collectors.toMap(Author::getId, Function.identity()));
    }

    @Override
    public Author getAuthorById(long id) {
        Map<String, Object> params = Collections.singletonMap("id", id);
        try {
            return jdbc.queryForObject("select * from authors where id = :id", params, new AuthorsMapper());
        } catch (IncorrectResultSizeDataAccessException e) {
            return null;
        }
    }

    @Override
    public boolean isUsed(long id) {
        Map<String, Object> params = Collections.singletonMap("id", id);
        Integer count = jdbc.queryForObject("select count(*) from book_authors where author_id=:id", params, Integer.class);
        return count != null && count > 0;
    }

    private static class AuthorsMapper implements RowMapper<Author> {
        @Override
        public Author mapRow(ResultSet resultSet, int i) throws SQLException {
            long id = resultSet.getLong("id");
            String surname = resultSet.getString("surname");
            String otherNames = resultSet.getString("other_names");
            return new Author(id, surname, otherNames);
        }
    }
}
