package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.BaseRepository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
@Qualifier("filmRepository")
public class FilmRepository extends BaseRepository<Film> implements FilmStorage {

    private static final String INSERT_FILM_QUERY = "INSERT INTO films (film_name,description,release_date,duration,mpa_id) VALUES (?,?,?,?,?)";
    private static final String INSERT_FILMS_QUERY = "INSERT INTO film_by_genre (film_id,genre_id) VALUES (?,?)";
    private static final String INSERT_LIKES_QUERY = "INSERT INTO film_by_genre (film_id,genre_id) VALUES (?,?)";
    private static final String FIND_ALL_FILMS_QUERY = "SELECT f.*, m.mpa_name, fg.genre_id, g.genre_name FROM films f JOIN mpa m ON f.mpa_id  = m.mpa_id  LEFT JOIN film_by_genre fg ON f.film_id = fg.film_id LEFT JOIN genre g ON fg.genre_id = g.genre_id ORDER BY f.film_id";
    private static final String FIND_POPULAR_FILMS_QUERY = "SELECT f.*, m.mpa_name, fg.genre_id, g.genre_name FROM films f JOIN mpa m ON f.mpa_id  = m.mpa_id  LEFT JOIN film_by_genre fg ON f.film_id = fg.film_id LEFT JOIN genre g ON fg.genre_id = g.genre_id ORDER BY f.film_id";
    private static final String FIND_FILM_BY_ID_QUERY = "SELECT f.*, m.mpa_name, fg.genre_id, g.GENRE_NAME FROM films f JOIN mpa m ON f.mpa_id  = m.mpa_id  LEFT JOIN film_by_genre fg ON f.film_id = fg.film_id LEFT JOIN genre g ON fg.GENRE_ID = g.GENRE_ID WHERE f.film_id = ?";
    private static final String UPDATE_FILM_QUERY = "UPDATE films SET film_name = ?,description = ?,release_date = ?,duration = ?,mpa_id = ?  WHERE film_id = ?";
    private static final String DELETE_FILMS_QUERY = "DELETE FROM film_by_genre WHERE film_id = ?";

    private final FilmExtractor filmExtractor;

    public FilmRepository(JdbcTemplate jdbc, RowMapper<Film> mapper, FilmExtractor filmExtractor) {
        super(jdbc, mapper);
        this.filmExtractor = filmExtractor;
    }

    @Override
    public Film createFilm(Film film) {
        long id = insert(INSERT_FILM_QUERY,
                film.getName(),
                film.getDescription(),
                Timestamp.valueOf(film.getReleaseDate().atStartOfDay()),
                film.getDuration(),
                film.getMpa().getId()
        );
        film.setId(id);
        if (film.getGenres() != null) {
            for (Genre genre : film.getGenres()) {
                add(INSERT_FILMS_QUERY, film.getId(), genre.getId());
            }
        }
        return film;
    }

    @Override
    public Collection<Film> findAllFilms() {
        return jdbc.query(FIND_ALL_FILMS_QUERY, filmExtractor);
    }

    @Override
    public Optional<Film> findFilm(long id) {
        try {
            List<Film> result = jdbc.query(FIND_FILM_BY_ID_QUERY, filmExtractor,id);
            System.out.println(result);
            return Optional.ofNullable(result.getFirst());
        } catch (EmptyResultDataAccessException ignored) {
            return Optional.empty();
        }
    }

    @Override
    public Film updateFilm(Film film) {
        update(UPDATE_FILM_QUERY,
                film.getName(),
                film.getDescription(),
                Timestamp.valueOf(film.getReleaseDate().atStartOfDay()),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId()
        );
        update(DELETE_FILMS_QUERY, film.getId());
        if (film.getGenres() != null) {
            for (Genre genre : film.getGenres()) {
                update(INSERT_FILMS_QUERY, film.getId(), genre.getId());
            }
        }
        return film;
    }

    @Override
    public void addLike(long id, long userId) {

    }

    @Override
    public void deleteLike(long id, long userId) {

    }

    @Override
    public Collection<Film> findPopularFilms(long count) {
        return null;
    }
}
