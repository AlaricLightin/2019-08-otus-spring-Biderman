-- noinspection SqlResolveForFile

INSERT INTO genres(id, genre_text) values ( 1, 'Test-genre' );
INSERT INTO genres(id, genre_text) values ( 2, 'Genre for delete' );

INSERT INTO authors(id, surname, other_names) VALUES ( 1, 'Ivanov', 'Ivan Ivanovich' );
INSERT INTO authors(id, surname, other_names) VALUES ( 2, 'Smith', 'John' );
INSERT INTO authors(id, surname, other_names) VALUES ( 3, 'ForDelete', 'Author' );

INSERT INTO books(id, title) VALUES ( 1, 'Book Name' );
INSERT INTO book_authors(book_id, author_id) VALUES ( 1, 1 );
INSERT INTO book_authors(book_id, author_id) VALUES ( 1, 2 );
INSERT INTO book_genres(book_id, genre_id) VALUES ( 1, 1 );

INSERT INTO books(id, title) VALUES ( 2, 'Book unavailable for minors');

INSERT INTO users(id, username, user_password, is_admin, is_adult) values ( 3, 'minor', '$2y$12$EVkA9zikPu2ZTubT0uYwHeIXJMAIYE./zT3p1hm6Z2NGnL/9H4GDa', false, false);

INSERT INTO acl_object_identity (id, object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting) VALUES
(1, 1, 1, NULL, 1, 0),
(2, 1, 2, NULL, 1, 0);

INSERT INTO acl_entry (id, acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure) VALUES
(1, 1, 1, 2, 1, 1, 1, 1),
(2, 2, 1, 3, 1, 1, 1, 1);
