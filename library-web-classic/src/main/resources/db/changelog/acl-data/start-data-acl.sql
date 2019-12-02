-- noinspection SqlResolveForFile

INSERT INTO authors(id, surname, other_names) VALUES ( 5, 'де Сад', 'Донасьен Альфонс Франсуа' );
SELECT setval(pg_get_serial_sequence('authors', 'id'), 5);

INSERT INTO books(id, title) VALUES ( 3, 'Жюстина, или Злоключения добродетели' );
INSERT INTO book_authors(book_id, author_id) VALUES ( 3, 5 );
INSERT INTO book_genres(book_id, genre_id) VALUES ( 3, 2 );
SELECT setval(pg_get_serial_sequence('books', 'id'), 3);

INSERT INTO users(id, username, user_password, is_admin, is_adult) values ( 3, 'minor', '$2y$12$EVkA9zikPu2ZTubT0uYwHeIXJMAIYE./zT3p1hm6Z2NGnL/9H4GDa', false, false);

INSERT INTO acl_object_identity (id, object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting) VALUES
(1, 1, 1, NULL, 1, false),
(2, 1, 2, NULL, 1, false),
(3, 1, 3, NULL, 1, false);
SELECT setval(pg_get_serial_sequence('acl_object_identity', 'id'), 3);

INSERT INTO acl_entry (id, acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure) VALUES
(1, 1, 1, 2, 1, true, true, true),
(2, 2, 1, 2, 1, true, true, true),
(3, 3, 1, 3, 1, true, true, true);
SELECT setval(pg_get_serial_sequence('acl_entry', 'id'), 3);

SELECT setval(pg_get_serial_sequence('acl_sid', 'id'), 3);
SELECT setval(pg_get_serial_sequence('acl_class', 'id'), 1);