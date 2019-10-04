package ru.biderman.library.dao;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.biderman.library.domain.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@SuppressWarnings({"SqlNoDataSourceInspection", "SqlResolve"})
@Repository
public class GenreDaoJdbc implements GenreDao {
    private final NamedParameterJdbcOperations jdbc;

    public GenreDaoJdbc(NamedParameterJdbcOperations jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public void addGenre(Genre genre) throws DaoException {
        Map<String, Object> params = Collections.singletonMap("title", genre.getTitle());
        try {
            jdbc.update("insert into genres(title) values(:title)", params);
        }
        catch (DataAccessException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void updateGenre(long id, String title) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("title", title);
        jdbc.update("update genres set title = :title where id = :id", params);
    }

    @Override
    public void deleteGenre(long id) throws DaoException {
        Map<String, Object> params = Collections.singletonMap("id", id);
        try {
            jdbc.update("delete from genres where id = :id", params);
        } catch (DataAccessException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public Map<Long, Genre> getAllGenres() {
        return jdbc.query("select * from genres", new GenresMapper()).stream()
                .collect(Collectors.toMap(Genre::getId, Function.identity()));
    }

    @Override
    public Genre getGenreById(long id) {
        Map<String, Object> params = Collections.singletonMap("id", id);
        try {
            return jdbc.queryForObject("select * from genres where id = :id", params, new GenresMapper());
        } catch (IncorrectResultSizeDataAccessException e) {
            return null;
        }
    }

    @Override
    public Genre getGenreByTitle(String title) {
        Map<String, Object> params = Collections.singletonMap("title", title);
        try {
            return jdbc.queryForObject("select * from genres where title = :title", params, new GenresMapper());
        } catch (IncorrectResultSizeDataAccessException e) {
            return null;
        }
    }

    private static class GenresMapper implements RowMapper<Genre> {
        @Override
        public Genre mapRow(ResultSet resultSet, int i) throws SQLException {
            long id = resultSet.getLong("id");
            String title = resultSet.getString("title");
            return new Genre(id, title);
        }
    }

    @Override
    public boolean isUsed(long id) {
        Map<String, Object> params = Collections.singletonMap("id", id);
        Integer count = jdbc.queryForObject("select count(*) from book_genres where genre_id=:id", params, Integer.class);
        return count != null && count > 0;
    }
}
