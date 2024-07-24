merge into mpa (mpa_id, mpa_name) values (1,'G');
merge into mpa (mpa_id, mpa_name) values (2,'PG');
merge into mpa (mpa_id, mpa_name) values (3,'PG-13');
merge into mpa (mpa_id, mpa_name) values (4,'R');
merge into mpa (mpa_id, mpa_name) values (5,'NC-17');



merge into friend_status (status_id,status) values (1,'подтвержден');
merge into friend_status (status_id, status) values (2,'неподтвержден');

merge into genre values (1,'Комедия');
merge into genre values (2,'Драма');
merge into genre values (3,'Мультфильм');
merge into genre values (4,'Триллер');
merge into genre values (5,'Документальный');
merge into genre values (6,'Боевик');



INSERT INTO users (user_name,email,login,birthday) VALUES ('testName1','testEmail1','testLogin1','2020-11-23');
INSERT INTO users (user_name,email,login,birthday) VALUES ('testName2','testEmail2','testLogin2','2020-11-23');
INSERT INTO users (user_name,email,login,birthday) VALUES ('testName3','testEmail3','testLogin3','2020-11-23');

MERGE INTO friends (user_id,friend_id,status_id) VALUES (1, 3, 1);
MERGE INTO friends (user_id,friend_id,status_id) VALUES (3, 1, 2);
MERGE INTO friends (user_id,friend_id,status_id) VALUES (2, 3, 1);
MERGE INTO friends (user_id,friend_id,status_id) VALUES (3, 2, 1);
MERGE INTO friends (user_id,friend_id,status_id) VALUES (1, 2, 1);
MERGE INTO friends (user_id,friend_id,status_id) VALUES (2, 1, 1);

INSERT INTO films (film_name,description,release_date,duration,mpa_id)
VALUES ('testFilm1','testDescription1', '2020-11-23',150, 1);
INSERT INTO films (film_name,description,release_date,duration,mpa_id)
VALUES ('testFilm2','testDescription2', '2020-11-23',150, 1);

MERGE INTO film_by_genre (film_id,genre_id) VALUES (1,1);
MERGE INTO film_by_genre (film_id,genre_id) VALUES (2,1);
MERGE INTO film_by_genre (film_id,genre_id) VALUES (2,3);

MERGE INTO likes (film_id,user_id) VALUES (1,1);
MERGE INTO likes (film_id,user_id) VALUES (2,1);
MERGE INTO likes (film_id,user_id) VALUES (2,2);
