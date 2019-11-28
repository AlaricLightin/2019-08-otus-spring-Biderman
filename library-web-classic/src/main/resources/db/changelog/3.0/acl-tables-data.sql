-- noinspection SqlResolveForFile

INSERT INTO acl_sid (id, principal, sid) VALUES
(1, true, 'admin'),
(2, false, 'ROLE_USER'),
(3, false, 'ROLE_ADULT');

INSERT INTO acl_class (id, class) VALUES
(1, 'ru.biderman.librarywebclassic.domain.Book');
