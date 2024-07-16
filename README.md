# java-filmorate
Template repository for Filmorate project.\
## ER-Diagram:
![alt text](er-diagram.png)

### Query example
Find all user:
```sqlite-psql
SELECT * FROM users;
```
Find user by ID:
```sqlite-psql
SELECT * FROM users WHERE user_id = ?;
```
Create user:
```sqlite-psql
INSERT INTO users (user_name,email,login,birthday) VALUES (?,?,?,?);
```
Update user:
```sqlite-psql
UPDATE users SET user_name = ? WHERE user_id = ?;
```
Add friend:
```sqlite-psql
INSERT INTO friends (user_id,friend_id,status_id) VALUES (?,?,?);
```
Remove friend:
```sqlite-psql
DELETE FROM friends WHERE user_id = ? AND friend_id = ?;
```
Find all friend:
```sqlite-psql
SELECT friend_id FROM friends WHERE user_id = ?;
```
Find common friend:
```sqlite-psql
SELECT friend_id FROM friends WHERE user_id=? AND friend_id IN (SELECT friend_id FROM friends WHER user_id=?) ;
```
Find all film:
```sqlite-psql
SELECT f.*, g.genre_id FROM films f JOIN film_by_genre g ON f.film_id = g.film_id ;
```
Find film by name:
```sqlite-psql
SELECT f.*, g.genre_id FROM films f JOIN film_by_genre g ON f.film_id = g.film_id WHERE f.film_name = ?;
```
Create film:
```sqlite-psql
INSERT INTO films (film_name,description,release_date,duration,mpa_id) VALUES (?,?,?,?,?);
INSERT INTO film_by_genre (film_id,genre_id) VALUES (?,?);
INSERT INTO film_by_genre (film_id,genre_id) VALUES (?,?);
```
Update film:
```sqlite-psql
UPDATE films SET description = ? WHERE film_id = ?;
```
Add like:
```sqlite-psql
INSERT INTO likes (film_id,user_id) VALUES (?,?);
```
Remove like:
```sqlite-psql
DELETE FROM likes WHERE film_id = ? AND user_id = ?;
```
Find popular film:
```sqlite-psql
SELECT * FROM films f JOIN likes l ON f.film_id = l.film_id GROUP BY f.film_id ORDER BY COUNT(l.user_id) DESC LIMIT ?;
```
