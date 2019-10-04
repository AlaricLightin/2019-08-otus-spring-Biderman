DROP TABLE IF EXISTS books;
CREATE TABLE books (
                       id IDENTITY NOT NULL PRIMARY KEY,
                       title VARCHAR (255) NOT NULL
);

DROP TABLE IF EXISTS authors;
CREATE TABLE authors (
                         id IDENTITY NOT NULL PRIMARY KEY,
                         surname VARCHAR (255) NOT NULL,
                         other_names VARCHAR (255)
);

DROP TABLE IF EXISTS genres;
CREATE TABLE genres (
                        id IDENTITY NOT NULL PRIMARY KEY,
                        title VARCHAR (32) NOT NULL UNIQUE
);

DROP TABLE IF EXISTS book_authors;
CREATE TABLE book_authors (
                              book_id BIGINT NOT NULL,
                              author_id BIGINT NOT NULL,
                              FOREIGN KEY (book_id) REFERENCES books(id),
                              FOREIGN KEY (author_id) REFERENCES authors(id)
);

DROP TABLE IF EXISTS book_genres;
CREATE TABLE book_genres (
                             book_id BIGINT NOT NULL,
                             genre_id BIGINT NOT NULL,
                             FOREIGN KEY (book_id) REFERENCES books(id),
                             FOREIGN KEY (genre_id) REFERENCES genres(id)
);