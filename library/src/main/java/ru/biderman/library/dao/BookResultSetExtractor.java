package ru.biderman.library.dao;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import ru.biderman.library.domain.Author;
import ru.biderman.library.domain.Book;
import ru.biderman.library.domain.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

class BookResultSetExtractor implements ResultSetExtractor<List<Book>> {
    private final Map<Long, Author> authorMap;
    private final Map<Long, Genre> genreMap;

    BookResultSetExtractor(Map<Long, Author> authorMap, Map<Long, Genre> genreMap) {
        this.authorMap = authorMap;
        this.genreMap = genreMap;
    }

    @Override
    public List<Book> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
        Map<Long, TempBook> tempBookMap = new HashMap<>();
        while (resultSet.next()) {
            long id = resultSet.getLong("books.id");
            TempBook tempBook = tempBookMap.get(id);
            if (tempBook == null) {
                tempBook = new TempBook(id, resultSet.getString("books.title"));
                tempBookMap.put(id, tempBook);
            }

            tempBook.addAuthorId(resultSet.getLong("book_authors.author_id"));
            tempBook.addGenreId(resultSet.getLong("book_genres.genre_id"));
        }

        return tempBookMap.values().stream()
                .map(this::createBook)
                .collect(Collectors.toList());
    }

    private Book createBook(TempBook tempBook) {
        return new Book(tempBook.getId(),
                tempBook.getAuthorIdList().stream()
                        .map(authorMap::get)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList()),
                tempBook.getTitle(),
                tempBook.getGenreIdList().stream()
                        .map(genreMap::get)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList())
        );
    }

    private static class TempBook {
        private final long id;
        private final String title;
        private final ArrayList<Long> authorIdList = new ArrayList<>();
        private final ArrayList<Long> genreIdList = new ArrayList<>();

        TempBook(long id, String title) {
            this.id = id;
            this.title = title;
        }

        long getId() {
            return id;
        }

        String getTitle() {
            return title;
        }

        ArrayList<Long> getAuthorIdList() {
            return authorIdList;
        }

        ArrayList<Long> getGenreIdList() {
            return genreIdList;
        }

        void addAuthorId(long id) {
            if (!authorIdList.contains(id))
                authorIdList.add(id);
        }

        void addGenreId(long id) {
            if(!genreIdList.contains(id))
                genreIdList.add(id);
        }
    }
}
