INSERT INTO genres(id, title) values ( 1, 'Test-genre' );
INSERT INTO genres(id, title) values ( 2, 'Genre for delete' );

INSERT INTO authors(id, surname, other_names) VALUES ( 1, 'Ivanov', 'Ivan Ivanovich' );
INSERT INTO authors(id, surname, other_names) VALUES ( 2, 'Smith', 'John' );
INSERT INTO authors(id, surname, other_names) VALUES ( 3, 'ForDelete', 'Author' );

INSERT INTO books(id, title) VALUES ( 1, 'Book Name' );
INSERT INTO book_authors(book_id, author_id) VALUES ( 1, 1 );
INSERT INTO book_authors(book_id, author_id) VALUES ( 1, 2 );
INSERT INTO book_genres(book_id, genre_id) VALUES ( 1, 1 );