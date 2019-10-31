-- noinspection SqlResolveForFile

INSERT INTO genres(id, genre_text) values ( 1, 'Поэзия' );
INSERT INTO genres(id, genre_text) values ( 2, 'Роман' );
INSERT INTO genres(id, genre_text) values ( 3, 'Фантастика' );
INSERT INTO genres(id, genre_text) values ( 4, 'Мемуары' );
INSERT INTO genres(id, genre_text) values ( 5, 'Учебник' );
SELECT setval(pg_get_serial_sequence('genres', 'id'), 5);

INSERT INTO authors(id, surname, other_names) VALUES ( 1, 'Пушкин', 'Александр' );
INSERT INTO authors(id, surname, other_names) VALUES ( 2, 'Ильф', 'Илья' );
INSERT INTO authors(id, surname, other_names) VALUES ( 3, 'Петров', 'Евгений' );
INSERT INTO authors(id, surname, other_names) VALUES ( 4, 'Лермонтов', 'Михаил' );
SELECT setval(pg_get_serial_sequence('authors', 'id'), 4);

INSERT INTO books(id, title) VALUES ( 1, 'Евгений Онегин' );
INSERT INTO book_authors(book_id, author_id) VALUES ( 1, 1 );
INSERT INTO book_genres(book_id, genre_id) VALUES ( 1, 1 );
INSERT INTO book_genres(book_id, genre_id) VALUES ( 1, 2 );

INSERT INTO books(id, title) VALUES ( 2, 'Двенадцать стульев' );
INSERT INTO book_authors(book_id, author_id) VALUES ( 2, 2 );
INSERT INTO book_authors(book_id, author_id) VALUES ( 2, 3 );
INSERT INTO book_genres(book_id, genre_id) VALUES ( 2, 2 );

SELECT setval(pg_get_serial_sequence('books', 'id'), 2);