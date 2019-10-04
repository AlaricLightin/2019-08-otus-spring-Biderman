CREATE TABLE IF NOT EXISTS books (
    id IDENTITY NOT NULL PRIMARY KEY,
    title VARCHAR (255) NOT NULL
);

CREATE TABLE IF NOT EXISTS authors (
    id IDENTITY NOT NULL PRIMARY KEY,
    surname VARCHAR (255) NOT NULL,
    other_names VARCHAR (255)
);

CREATE TABLE IF NOT EXISTS genres (
    id IDENTITY NOT NULL PRIMARY KEY,
    title VARCHAR (32) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS book_authors (
    book_id BIGINT NOT NULL,
    author_id BIGINT NOT NULL,
    FOREIGN KEY (book_id) REFERENCES books(id),
    FOREIGN KEY (author_id) REFERENCES authors(id)
);

CREATE TABLE IF NOT EXISTS book_genres (
    book_id BIGINT NOT NULL,
    genre_id BIGINT NOT NULL,
    FOREIGN KEY (book_id) REFERENCES books(id),
    FOREIGN KEY (genre_id) REFERENCES genres(id)
);