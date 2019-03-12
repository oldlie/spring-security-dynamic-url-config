-- init account
INSERT INTO  t_user
SELECT 1, '2019-3-11','2019-3-11','admin', '$2a$10$3zGUwNwzRdFNXm7LLD9i8O/vzySodVBksnvTUDk9Qc8XHsx0tZhrS' FROM dual
WHERE NOT EXISTS( SELECT 1 FROM t_user WHERE id = 1);
INSERT INTO  t_user
SELECT 2,'2019-3-11','2019-3-11','user', '$2a$10$3zGUwNwzRdFNXm7LLD9i8O/vzySodVBksnvTUDk9Qc8XHsx0tZhrS' FROM dual
WHERE NOT EXISTS( SELECT 1 FROM t_user WHERE id = 2);